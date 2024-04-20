import java.util.Random;
import java.util.ArrayList;
import java.util.List;
public class SistemaDeReserva {

    protected static final int FILAS = 3;
    protected static final int COLUMNAS = 3;
    protected static final int CANTIDAD_ASIENTOS = FILAS * COLUMNAS;
    private final int SLEEP_PENDIENTE = 5;
    private final int SLEEP_PAGO = 1500;
    private final int SLEEP_VERIFICACION = 1000;
    private final int SLEEP_CANCELACION = 1200;
    private Asiento[][] asientos;
    private ProcesoDeReserva procesoDeReserva;

    private ProcesoDePago procesoDePago;
    protected static boolean sigueProcesoDePago;

    private ProcesoDeCancelacionValidacion procesoDeCancelacionValidacion;

    private ProcesoDeVerificacion procesoDeVerificacion;

    private List<Reserva> reservasPendientes;
    private List<Reserva> reservasConfirmadas;
    private List<Reserva> reservasCanceladas;
    private List<Reserva> reservasVerificadas;

    public SistemaDeReserva() {

        this.asientos = new Asiento[FILAS][COLUMNAS];
        this.reservasPendientes = new ArrayList<>();
        this.reservasConfirmadas = new ArrayList<>();
        this.reservasCanceladas = new ArrayList<>();
        this.reservasVerificadas = new ArrayList<>();
        // Inicializar los asientos del avión
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j] = new Asiento();
            }
        }
        
        this.procesoDeReserva = new ProcesoDeReserva(asientos, reservasPendientes, SLEEP_PENDIENTE);
        
        sigueProcesoDePago = true;
        this.procesoDePago = new ProcesoDePago(reservasPendientes, reservasConfirmadas, reservasCanceladas, SLEEP_PAGO);
        
        this.procesoDeCancelacionValidacion = new ProcesoDeCancelacionValidacion(reservasConfirmadas, reservasCanceladas, SLEEP_CANCELACION);
        
        this.procesoDeVerificacion = new ProcesoDeVerificacion(reservasConfirmadas, reservasVerificadas, SLEEP_VERIFICACION);
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
    

    public ProcesoDeReserva getProcesoDeReserva() {
        return procesoDeReserva;
    }
    public ProcesoDePago getProcesoDePago() {
        return procesoDePago;
    }
    public ProcesoDeCancelacionValidacion getProcesoDeCancelacionValidacion() {
        return procesoDeCancelacionValidacion;
    }
}

/*
Se ejecuta el programa hasta que se ocupan los asientos ya que no hay nada más implementado.

*/
