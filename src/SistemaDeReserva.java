import java.util.Random;
public class SistemaDeReserva implements Runnable{
    private final int FILAS = 30;
    private final int COLUMNAS = 3;
    private Asiento[][] asientos;
    private RegistroReservas registroReservas;
    private ProcesoDeReserva procesoDeReserva;
    
    public SistemaDeReserva() {
        this.asientos = new Asiento[FILAS][COLUMNAS];
        this.registroReservas = new RegistroReservas();
        this.procesoDeReserva = new ProcesoDeReserva(asientos, registroReservas, FILAS, COLUMNAS);

        // Inicializar los asientos del avión
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j] = new Asiento();
            }
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

    public void reservarAsientoAleatorio() {
        boolean asientoEncontrado = false;
        // Intentar reservar un asiento aleatorio hasta que se encuentre uno libre
        do {
            Random random = new Random();
            int filaAleatoria = random.nextInt(FILAS);
            int columnaAleatoria = random.nextInt(COLUMNAS); 
            Asiento asiento = getAsiento(filaAleatoria, columnaAleatoria); 

            Reserva reserva = new Reserva();
            reserva.setEstado(EstadoReserva.PENDIENTE_DE_PAGO);
            reserva.setFila(filaAleatoria);
            reserva.setColumna(columnaAleatoria);
            reserva.setAsiento(asiento);

            asientoEncontrado = procesoDeReserva.reservarAsiento(asiento, reserva, filaAleatoria, columnaAleatoria); //Sincronizado: solo un hilo puede acceder a la vez

        } while (!asientoEncontrado && hayAsientosLibres());
    }
    
    public void pagarAsientoAleatorio(){
        Random random = new Random();
        boolean sePaga = random.nextDouble() < 0.9;
        // Obtener una reserva aleatoria de la lista de reservas pendientes
        if (!registroReservas.getReservasPendientes().isEmpty()) {

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
        else{
            System.out.println("No hay reservas pendientes");
        }
    }
    
    
    @Override
    public void run() {
            reservarAsientoAleatorio();

        
        System.out.println("Reservas realizadas por hilo de " + Thread.currentThread().getName());
    }

}

/*
Se ejecuta el programa hasta que se ocupan los asientos ya que no hay nada más implementado.

*/

