import java.util.ArrayList;
import java.util.Random;
public class ProcesoCancelacionValidacion implements Runnable {

    private ArrayList<Reserva> reservasConfirmadas;
    private ArrayList<Reserva> reservasCanceladas;

    public ProcesoCancelacionValidacion(ArrayList<Reserva> reservasConfirmadas, ArrayList<Reserva> reservasCanceladas) {
        this.reservasConfirmadas = reservasConfirmadas;
        this.reservasCanceladas = reservasCanceladas;
    }

    public void intentarCancelar() {
        Reserva reserva = null;
        boolean probCancelacion = new Random().nextDouble() < 0.1;

        // confirmadas.isEmpty() anda
        // no chequeada -> cancela anda
        // no chequeada -> chequea anda
        // chequeada

        synchronized (reservasConfirmadas) {
            if (!reservasConfirmadas.isEmpty()) {
                int randomIndex = new Random().nextInt(reservasConfirmadas.size());
                reserva = reservasConfirmadas.get(randomIndex);

                if (!reserva.getCheck() && probCancelacion){
                    reservasConfirmadas.remove(reserva); //falta agregarla a canceladas
                }
                try {
                    reservasConfirmadas.notifyAll();
                    reservasConfirmadas.wait(1);
                } catch (Exception e) {
                }
            }
        }

        if (reserva == null || reserva.getCheck()){
            return;
        }


        else if (probCancelacion) {

            reserva.getAsiento().cancelar_validacion();

            synchronized (reservasCanceladas) {
                reservasCanceladas.add(reserva);
                try {
                    reservasCanceladas.notifyAll();
                    reservasCanceladas.wait(1);
                } catch (Exception e) {
                }
            }
        }
        else{
            reserva.setCheck(true);
        }

    }

    public void run() {
        while (SistemaDeReservas.sigueProcesoDePago /*|| !reservasConfirmadas.isEmpty()*/) {
            intentarCancelar();
        }
        System.out.println("proceso de canc/vali yendo a mimir");
        SistemaDeReservas.sigueProcesoDeCancelacionValidacion = false;
    }
}

