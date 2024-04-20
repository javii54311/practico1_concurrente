import java.util.Random;
import java.util.ArrayList;
import java.util.List;
public class SistemaDeReserva {

    protected static final int FILAS = 3;
    protected static final int COLUMNAS = 3;
    private final int CANTIDAD_ASIENTOS = FILAS * COLUMNAS;
    private final int SLEEP_PENDIENTE = 1000;
    private final int SLEEP_PAGO = 500;
    private Asiento[][] asientos;
    private RegistroReservas registroReservas;
    private ProcesoDeReserva procesoDeReserva;
    private List<Reserva> reservasPendientes;
    private List<Reserva> reservasConfirmadas;
    private List<Reserva> reservasCanceladas;
    private List<Reserva> reservasVerificadas;

    public SistemaDeReserva() {
        this.asientos = new Asiento[FILAS][COLUMNAS];
        this.registroReservas = new RegistroReservas();
        this.reservasPendientes = new ArrayList<>();
        this.reservasConfirmadas = new ArrayList<>();
        this.reservasCanceladas = new ArrayList<>();
        this.reservasVerificadas = new ArrayList<>();
        this.procesoDeReserva = new ProcesoDeReserva(asientos, reservasPendientes, SLEEP_PENDIENTE);

        // Inicializar los asientos del avión
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j] = new Asiento();
            }
        }
    }
    
    public void addReservaPendiente(Reserva reserva) {
        synchronized (reservasPendientes) {
            System.out.printf("Reserva pendiente hecha por el hilo %s\n", Thread.currentThread().getName());
            this.reservasPendientes.add(reserva);
        }
    }

    public Asiento[][] getAsientos() { // Método sincronizado
        synchronized (asientos) {
            return asientos;
        }
    }
    
    public Asiento getAsiento(int fila, int columna) { // Método sincronizado 
        synchronized (asientos[fila][columna]) {
            return asientos[fila][columna];
        }    
    }

    public boolean hayAsientosLibres() { //sincronizado indirectamente
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (getAsiento(i, j).getEstado() == EstadoAsiento.LIBRE) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void pagarAsientosAleatorios(){

        int cont = 0;
        do{
        Random random = new Random();
        boolean sePaga = random.nextDouble() < 0.9;
        // Obtener una reserva aleatoria de la lista de reservas pendientes
        if (!registroReservas.getReservasPendientes().isEmpty()) {
            cont ++;
            int indiceAleatorio = random.nextInt(registroReservas.getReservasPendientes().size());
            Reserva reserva = registroReservas.getReservasPendientes().get(indiceAleatorio);

            // Intentar pagar la reserva
            if (sePaga) { 
                // Eliminar la reserva de la lista de pendientes
                registroReservas.eliminarReservaPendiente(reserva);
                // Agregar la reserva a la lista de confirmadas
                registroReservas.agregarReservaConfirmada(reserva);
            }
            else {
                // Colocar el asiento en estado DESCARTADO
                reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                // Eliminar la reserva de la lista de pendientes
                registroReservas.eliminarReservaPendiente(reserva);
                // Agregar la reserva a la lista de canceladas
                registroReservas.agregarReservaCancelada(reserva);
            }
        }
    }while(cont < CANTIDAD_ASIENTOS);
    }

    public ProcesoDeReserva getProcesoDeReserva() {
        return procesoDeReserva;
    }
}

/*
Se ejecuta el programa hasta que se ocupan los asientos ya que no hay nada más implementado.

*/

