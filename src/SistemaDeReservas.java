import java.util.ArrayList;
public class SistemaDeReservas {
    protected static final int FILAS = 62;
    protected static final int COLUMNAS = 3;
    private Asiento asientos[][] = new Asiento[FILAS][COLUMNAS];

    private ArrayList<Reserva> reservasPendientes;
    private ArrayList<Reserva> reservasConfirmadas;
    private ArrayList<Reserva> reservasCanceladas;
    private ArrayList<Reserva> reservasVerificadas;

    private ProcesoDeReserva procesoDeReserva;
    private ProcesoDePago procesoDePago;
    protected static boolean sigueProcesoDePago;
    private ProcesoCancelacionValidacion procesoDeCancelacionValidacion;
    protected static boolean sigueProcesoDeCancelacionValidacion;

    public SistemaDeReservas() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j] = new Asiento();
            }
        }
        reservasPendientes = new ArrayList<>();
        reservasConfirmadas = new ArrayList<>();
        reservasCanceladas = new ArrayList<>();
        reservasVerificadas = new ArrayList<>();

        this.sigueProcesoDePago = true;

        procesoDeReserva = new ProcesoDeReserva(asientos, reservasPendientes);
        procesoDePago = new ProcesoDePago(asientos, reservasPendientes, reservasConfirmadas, reservasCanceladas);
        procesoDeCancelacionValidacion = new ProcesoCancelacionValidacion(reservasConfirmadas, reservasCanceladas);
    }

    public ProcesoDeReserva getProcesoDeReserva() {
        return procesoDeReserva;
    }
    public ProcesoDePago getProcesoDePago() {
        return procesoDePago;
    }
    public ProcesoCancelacionValidacion getProcesoDeCancelacionValidacion() {
        return procesoDeCancelacionValidacion;
    }
    public ArrayList<Reserva> getReservasPendientes() {
        return reservasPendientes;
    }
    public ArrayList<Reserva> getReservasConfirmadas() {
        return reservasConfirmadas;
    }
    public ArrayList<Reserva> getReservasCanceladas() {
        return reservasCanceladas;
    }
    public ArrayList<Reserva> getReservasVerificadas() {
        return reservasVerificadas;
    }
}
