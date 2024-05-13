import java.util.ArrayList;
import java.util.Random;

public class ProcesoDePago implements Runnable{
    private Asiento asientos[][];
    private ArrayList<Reserva> reservasPendientes;
    private ArrayList<Reserva> reservasConfirmadas;
    private ArrayList<Reserva> reservasCanceladas;

    public ProcesoDePago(Asiento asientos[][], ArrayList<Reserva> reservasPendientes, ArrayList<Reserva> reservasConfirmadas, ArrayList<Reserva> reservasCanceladas) {
        this.asientos = asientos;
        this.reservasPendientes = reservasPendientes;
        this.reservasConfirmadas = reservasConfirmadas;
        this.reservasCanceladas = reservasCanceladas;
    }
    public boolean hayReservasPendientes() {
        for (int i = 0; i < SistemaDeReservas.FILAS; i++) {
            for (int j = 0; j < SistemaDeReservas.COLUMNAS; j++) {
                if(asientos[i][j].getReserva().getEstado() == EstadoReserva.NONATA || asientos[i][j].getReserva().getEstado() == EstadoReserva.PENDIENTE){
                    return true;
                }
            }
        }
        return false;
    }
    public void run() {
        System.out.println("Proceso de pago iniciado");
        while (hayReservasPendientes()) {
            intentarPagar();
        }

        System.out.println("No hay reservas pendientes para pagar, "+ Thread.currentThread().getName()+" finaliza");
    }
    public void intentarPagar() {
        Reserva reserva = null;

        synchronized(reservasPendientes){
            if(reservasPendientes.size()>0){
                int randomIndex = new Random().nextInt(reservasPendientes.size());
                reserva = reservasPendientes.get(randomIndex);
            }
        }
        if(reserva == null){
            return;
        }

        boolean sePudoConfirmar = false;
        boolean sePudoCancelar = false;

        if(new Random().nextDouble() < 0.9){
            System.out.println("Reserva para pagar");
            sePudoConfirmar = reserva.getAsiento().confirmar();
            System.out.println("Reserva pagada");
        }
        else{
            sePudoCancelar = reserva.getAsiento().cancelar_pago();
        }

        if(sePudoConfirmar || sePudoCancelar){
            synchronized (reservasPendientes){
                reservasPendientes.remove(reserva);
                    try {
                        reservasPendientes.notifyAll();
                        reservasPendientes.wait(1);
                    } catch (Exception e) {
                }
            }
            if(sePudoConfirmar){
                synchronized (reservasConfirmadas){
                    reservasConfirmadas.add(reserva);
                
                    try {
                        reservasConfirmadas.notifyAll();
                        reservasConfirmadas.wait(1);
                    } catch (Exception e) {
                    }
                }
            if(sePudoCancelar){
                synchronized (reservasCanceladas){
                    reservasCanceladas.add(reserva);
                        try {
                            reservasCanceladas.notifyAll();
                            reservasCanceladas.wait(1);
                        } catch (Exception e) {
                    }
                    }
                }
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
    }
}

