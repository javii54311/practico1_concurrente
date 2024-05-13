import java.util.ArrayList;
public class SistemaDeReservas {
    protected static final int FILAS = 62;
    protected static final int COLUMNAS = 3;
    private Asiento asientos[][] = new Asiento[FILAS][COLUMNAS];

    private ArrayList<Reserva> reservasPendientes;
    private ArrayList<Reserva> reservasConfirmadas;
    private ArrayList<Reserva> reservasCanceladas;

    private ProcesoDeReserva procesoDeReserva;
    private ProcesoDePago procesoDePago;
    protected static boolean sigueProcesoDePago;
    private ProcesoCancelacionValidacion procesoDeCancelacionValidacion;

    public SistemaDeReservas() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j] = new Asiento();
            }
        }
        reservasPendientes = new ArrayList<>();
        reservasConfirmadas = new ArrayList<>();
        reservasCanceladas = new ArrayList<>();

        this.sigueProcesoDePago = true;

        procesoDeReserva = new ProcesoDeReserva(asientos, reservasPendientes);
        procesoDePago = new ProcesoDePago(asientos, reservasPendientes, reservasConfirmadas, reservasCanceladas);
    }

    public ProcesoDeReserva getProcesoDeReserva() {
        return procesoDeReserva;
    }
    public ProcesoDePago getProcesoDePago() {
        return procesoDePago;
    }
}
