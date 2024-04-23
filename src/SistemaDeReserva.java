import java.util.ArrayList;
import java.util.List;
public class SistemaDeReserva {
    // Caracteristicas del avion
    protected static final int FILAS = 31;
    protected static final int COLUMNAS = 6;
    protected static final int CANTIDAD_ASIENTOS = FILAS * COLUMNAS;

    // Tiempo de espera de cada proceso (en milisegundos)
    private final long SLEEP_PENDIENTE;
    private final long SLEEP_PAGO;
    private final long SLEEP_CANCELACION;
    private final long SLEEP_VERIFICACION;

    private Asiento[][] asientos;

    // Procesos
    private ProcesoDeReserva procesoDeReserva;

    private ProcesoDePago procesoDePago;
    protected static boolean sigueProcesoDePago;

    private ProcesoDeCancelacionValidacion procesoDeCancelacionValidacion;
    protected static boolean sigueProcesoDeCancelacion;

    private ProcesoDeVerificacion procesoDeVerificacion;
    protected static boolean sigueProcesoDeVerificacion;

    private Log logDeReservas;

    // Listas de reservas
    private List<Reserva> reservasPendientes;
    private List<Reserva> reservasConfirmadas;
    private List<Reserva> reservasCanceladas;
    private List<Reserva> reservasVerificadas;

    public SistemaDeReserva(long SLEEP_PENDIENTE, long SLEEP_PAGO, long SLEEP_CANCELACION, long SLEEP_VERIFICACION) {
        this.SLEEP_PENDIENTE = SLEEP_PENDIENTE;
        this.SLEEP_PAGO = SLEEP_PAGO;
        this.SLEEP_CANCELACION = SLEEP_CANCELACION;
        this.SLEEP_VERIFICACION = SLEEP_VERIFICACION;
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
        
        this.procesoDeReserva = new ProcesoDeReserva(asientos, reservasPendientes, this.SLEEP_PENDIENTE);
        
        this.procesoDePago = new ProcesoDePago(reservasPendientes, reservasConfirmadas, reservasCanceladas, this.SLEEP_PAGO);
        sigueProcesoDePago = true;

        this.procesoDeCancelacionValidacion = new ProcesoDeCancelacionValidacion(reservasConfirmadas, reservasCanceladas, this.SLEEP_CANCELACION);

        this.procesoDeVerificacion = new ProcesoDeVerificacion(reservasConfirmadas, reservasVerificadas, this.SLEEP_VERIFICACION);

        this.logDeReservas = new Log(reservasCanceladas, reservasVerificadas, reservasConfirmadas, reservasPendientes, this);
    }

    // Getters de los procesos. Se utilizan para crear los hilos en Main.java

    public ProcesoDeReserva getProcesoDeReserva() {
        return procesoDeReserva;
    }
    public ProcesoDePago getProcesoDePago() {
        return procesoDePago;
    }
    public ProcesoDeCancelacionValidacion getProcesoDeCancelacionValidacion() {
        return procesoDeCancelacionValidacion;
    }
    public ProcesoDeVerificacion getProcesoDeVerificacion() {
        return procesoDeVerificacion;
    }
    public Log getLogDeReservas() {
        return logDeReservas;
    }
}

