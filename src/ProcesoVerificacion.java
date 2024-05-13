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
        if(reserva.getCheck()){
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
        while (SistemaDeReservas.sigueProcesoDeCancelacionValidacion) {
            intentarVerificar();
        }

    }
}
