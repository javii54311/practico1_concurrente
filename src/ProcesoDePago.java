import java.util.ArrayList;
import java.util.Random;

public class ProcesoDePago implements Runnable{
    private ArrayList<Reserva> reservasPendientes;
    private ArrayList<Reserva> reservasConfirmadas;
    private ArrayList<Reserva> reservasCanceladas;

    public ProcesoDePago(ArrayList<Reserva> reservasPendientes, ArrayList<Reserva> reservasConfirmadas, ArrayList<Reserva> reservasCanceladas) {
        this.reservasPendientes = reservasPendientes;
        this.reservasConfirmadas = reservasConfirmadas;
        this.reservasCanceladas = reservasCanceladas;
    }
    public boolean hayReservasParaPagar() {
        if(SistemaDeReservas.hilosReservando.get()==0 && reservasPendientes.size()==0){
            return false;
            
        }
        return true;
    }
    
    public void run() {
        SistemaDeReservas.hilosPagando.incrementAndGet();
        System.out.println("Proceso de pago iniciado");
        while (hayReservasParaPagar()) {
            intentarPagar();
        }
        System.out.println("No hay reservas pendientes para pagar, "+ Thread.currentThread().getName()+" finaliza");
        SistemaDeReservas.hilosPagando.decrementAndGet();
    }
    public void intentarPagar() {

        int randomIndex;
        Reserva reserva;

        //sincroniza para que randomIndex siempre sea un índice válido
        synchronized(reservasPendientes){
            if(reservasPendientes.size()>0){
                randomIndex = new Random().nextInt(reservasPendientes.size());
                reserva = reservasPendientes.get(randomIndex);
                try {
                    reservasPendientes.notifyAll();
                    reservasPendientes.wait(SistemaDeReservas.waitPago);
                } catch (Exception e) {
                }
            }
            else{
                try {
                    reservasPendientes.notifyAll();
                    reservasPendientes.wait(SistemaDeReservas.waitPago);
                } catch (Exception e) {
                }
                return;
            }
        }

        boolean sePudoConfirmar = false;
        boolean sePudoCancelar = false;

        if(Math.random() < 0.9){
            System.out.println("Reserva para pagar");
            sePudoConfirmar = reserva.getAsiento().confirmar();
            System.out.println("Reserva pagada");
        }
        else{
            sePudoCancelar = reserva.getAsiento().cancelar();
        }

        if(sePudoConfirmar || sePudoCancelar){
            synchronized (reservasPendientes){
                reservasPendientes.remove(reserva);
                    try {
                        reservasPendientes.notifyAll();
                        reservasPendientes.wait(SistemaDeReservas.waitPago);
                    } catch (Exception e) {
                }
            }
            if(sePudoConfirmar){
                synchronized (reservasConfirmadas){
                    reservasConfirmadas.add(reserva);
                
                    try {
                        reservasConfirmadas.notifyAll();
                        reservasConfirmadas.wait(SistemaDeReservas.waitPago);
                    } catch (Exception e) {
                    }
                }
            }
            if(sePudoCancelar){
                synchronized (reservasCanceladas){
                    reservasCanceladas.add(reserva);
                        try {
                            reservasCanceladas.notifyAll();
                            reservasCanceladas.wait(SistemaDeReservas.waitPago);
                        } catch (Exception e) {
                    }
                    }
                }
            
            try {
                Thread.sleep(SistemaDeReservas.sleepPago);
            } catch (Exception e) {
            }
        }



    }
}

