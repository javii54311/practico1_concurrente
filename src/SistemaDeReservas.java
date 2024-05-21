import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
public class SistemaDeReservas {
    protected static final int FILAS = 31;
    protected static final int COLUMNAS = 6;
    private Asiento asientos[][] = new Asiento[FILAS][COLUMNAS];

    private ArrayList<Reserva> reservasPendientes;
    private ArrayList<Reserva> reservasConfirmadas;
    private ArrayList<Reserva> reservasCanceladas;
    private ArrayList<Reserva> reservasVerificadas;

    private ProcesoDeReserva procesoDeReserva;
    private ProcesoDePago procesoDePago;
    private ProcesoDeCheckin procesoDeCheckin;
    private ProcesoDeVerificacion procesoDeVerificacion;
    private Log log;

    protected static AtomicInteger hilosReservando;
    protected static AtomicInteger hilosPagando;
    protected static AtomicInteger hilosCheckin;
    protected static AtomicInteger hilosVerificando;

    protected static long sleepReserva;
    protected static long sleepPago;
    protected static long sleepCheckin;
    protected static long sleepVerificacion;

    protected static long waitReserva;
    protected static long waitPago;
    protected static long waitCheckin;
    protected static long waitVerificacion;

    public SistemaDeReservas(long sleepReserva,long sleepPago,long sleepCheckin,long sleepVerificacion,long waitReserva,long waitPago,long waitCheckin,long waitVerificacion) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                asientos[i][j] = new Asiento();
            }
        }
        reservasPendientes = new ArrayList<>();
        reservasConfirmadas = new ArrayList<>();
        reservasCanceladas = new ArrayList<>();
        reservasVerificadas = new ArrayList<>();
        
        hilosReservando = new AtomicInteger(0);
        hilosPagando = new AtomicInteger(0);
        hilosCheckin = new AtomicInteger(0);
        hilosVerificando = new AtomicInteger(0);

        SistemaDeReservas.sleepReserva = sleepReserva;
        SistemaDeReservas.sleepPago = sleepPago;
        SistemaDeReservas.sleepCheckin = sleepCheckin;
        SistemaDeReservas.sleepVerificacion = sleepVerificacion;
        SistemaDeReservas.waitReserva = waitReserva;
        SistemaDeReservas.waitPago = waitPago;
        SistemaDeReservas.waitCheckin = waitCheckin;
        SistemaDeReservas.waitVerificacion = waitVerificacion;

        procesoDeReserva = new ProcesoDeReserva(asientos, reservasPendientes);
        procesoDePago = new ProcesoDePago(reservasPendientes, reservasConfirmadas, reservasCanceladas);
        procesoDeCheckin = new ProcesoDeCheckin(reservasConfirmadas, reservasCanceladas);
        procesoDeVerificacion = new ProcesoDeVerificacion(reservasConfirmadas, reservasVerificadas);
        log = new Log(reservasCanceladas, reservasVerificadas, reservasPendientes, reservasConfirmadas);
    }

    public ProcesoDeReserva getProcesoDeReserva() {
        return procesoDeReserva;
    }
    public ProcesoDePago getProcesoDePago() {
        return procesoDePago;
    }

    public void mostrarEstadoAsientos() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print("["+asientos[i][j].toString() + "-"+ asientos[i][j].getReserva().toString() + "] ");
                if((j+1)== (int)COLUMNAS/2){
                    System.out.print("  ");
                
                }
            }
            System.out.println();
        }
    }
    public boolean diagnosticarFinal() {
        System.out.println("Diagnóstico final del sistema de reservas");
        boolean flag1 = (reservasCanceladas.size() + reservasVerificadas.size())== (FILAS*COLUMNAS);
        System.out.println("Se procesaron todas (verficadas o canceladas)? " + flag1);
        if(flag1){
            System.out.println(reservasVerificadas.size()+" reservas verificadas de "+(reservasVerificadas.size()+reservasCanceladas.size()));
        }
        else{
            System.out.println("No todas las reservas han sido canceladas o verificadas");
            System.out.println("Reservas pendientes según lista: "+reservasPendientes.size());
            int countPend = 0;
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    if(asientos[i][j].getEstado() == EstadoAsiento.LIBRE){
                        countPend++;
                    }
                }
            }
            System.out.println("Reservas pendientes según asientos: "+countPend);

            System.out.println("Reservas confirmadas según lista: "+reservasConfirmadas.size());
            int countConf = 0;
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    if(asientos[i][j].getReserva().getEstado() == EstadoReserva.CONFIRMADA){
                        countConf++;
                    }
                }
            }
            System.out.println("Reservas confirmadas según asientos: "+countConf);

            System.out.println("Reservas canceladas según lista: "+reservasCanceladas.size());
            int countCanc = 0;
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    if(asientos[i][j].getReserva().getEstado() == EstadoReserva.CANCELADA){
                        countCanc++;
                    }
                }
            }
            System.out.println("Reservas canceladas según asientos: "+countCanc);

            System.out.println("Reservas verificadas según lista: "+reservasVerificadas.size());
            int countVerif = 0;
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    if(asientos[i][j].getReserva().getEstado() == EstadoReserva.VERIFICADA){
                        countVerif++;
                    }
                }
            }
            System.out.println("Reservas verificadas según asientos: "+countVerif);

        }

        boolean flag2 = (reservasPendientes.size() + reservasConfirmadas.size())== 0;
        System.out.println("No hay reservas pendientes ni confirmadas? " + flag2);
        if(flag1&&flag2){return true;}
        return false;
    }

    public ProcesoDeCheckin getProcesoDeCheckin() {
        return procesoDeCheckin;
    }

    public ProcesoDeVerificacion getProcesoDeVerificacion() {
        return procesoDeVerificacion;
    }
    public Log getLog() {
        return log;
    }
}

