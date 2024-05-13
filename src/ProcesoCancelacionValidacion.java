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

        synchronized (reservasConfirmadas) {
            if (reservasConfirmadas.size() > 0) {
                int randomIndex = new Random().nextInt(reservasConfirmadas.size());
                reserva = reservasConfirmadas.get(randomIndex);

                if (reserva.getCheck()) {
                    try {
                        reservasConfirmadas.notifyAll();
                        reservasConfirmadas.wait(1);
                    } catch (Exception e) {
                    }
                    return;
                }
                reservasConfirmadas.remove(reserva); //nadie más la puede agarrar si ya la tome
                try {
                    reservasConfirmadas.notifyAll();
                    reservasConfirmadas.wait(1);
                } catch (Exception e) {
                }
            }
            if (reserva == null) {
                return;
            }

            if (new Random().nextDouble() < 0.1) {

                reserva.getAsiento().cancelar_validacion();

                // como quedo resuelto creo que siempre se puede cancelar,
                // por lo tanto este chequeo puede ser innecesario


                synchronized (reservasCanceladas) {
                    System.out.println("CANCELACION ----------------------");
                    reservasCanceladas.add(reserva);
                    try {
                        reservasCanceladas.notifyAll();
                        reservasCanceladas.wait(1);
                    } catch (Exception e) {
                    }
                }

            } else {
                reserva.setCheck(true);

                synchronized (reservasConfirmadas) {
                    reservasConfirmadas.add(reserva);
                    try {
                        reservasConfirmadas.notifyAll();
                        reservasConfirmadas.wait(1);
                    } catch (Exception e) {
                    }
                }
            }


        }
    }
    public void run () {
        while (SistemaDeReservas.sigueProcesoDePago /*|| !reservasConfirmadas.isEmpty()*/) {
            intentarCancelar();
        }
        SistemaDeReservas.sigueProcesoDeCancelacionValidacion = false;
    }
}
