import java.util.ArrayList;
import java.util.Random;
public class ProcesoVerificacion implements Runnable {
    ArrayList<Reserva> reservasConfirmadas;
    ArrayList<Reserva> reservasVerificadas;

    public ProcesoVerificacion(ArrayList<Reserva> reservasConfirmadas, ArrayList<Reserva> reservasVerificadas) {
        this.reservasConfirmadas = reservasConfirmadas;
        this.reservasVerificadas = reservasVerificadas;
    }

    public void intentarVerificar() {
        Reserva reserva = null;

        // confirmadas.isEmpty() ok
        // no chequeada ok
        // chequeada

        synchronized (reservasConfirmadas) {
            if (reservasConfirmadas.size() > 0) {
                int randomIndex = new Random().nextInt(reservasConfirmadas.size());
                reserva = reservasConfirmadas.get(randomIndex);

                if (reserva.getCheck()) {
                    reservasConfirmadas.remove(reserva);
                }
                try {
                    reservasConfirmadas.notifyAll();
                    reservasConfirmadas.wait(1);
                } catch (Exception e) {
                }
            }
        }
        if (reserva == null) {
            return;
        }
        else if(reserva.getCheck()){
            synchronized (reservasVerificadas) {
                reservasVerificadas.add(reserva);
                try {
                    reservasVerificadas.notifyAll();
                    reservasVerificadas.wait(1);
                } catch (Exception e) {
                }
            }
        }
    }
    public void run() {
        while (SistemaDeReservas.sigueProcesoDeCancelacionValidacion || !reservasConfirmadas.isEmpty()) {
            intentarVerificar();
        }
        System.out.println("se termina to");
    }
}
