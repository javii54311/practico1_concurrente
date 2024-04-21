import java.util.Random;
import java.util.List;

public class ProcesoDeCancelacionValidacion implements Runnable {
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasCanceladas;
    int reservasChequeadas;
    int sleep_cancelacion;

    public ProcesoDeCancelacionValidacion(List<Reserva> listaReservasConfirmadas,
            List<Reserva> listaReservasCanceladas, int sleep_cancelacion) {
        this.listaReservasConfirmadas = listaReservasConfirmadas;
        this.listaReservasCanceladas = listaReservasCanceladas;
        this.sleep_cancelacion = sleep_cancelacion;
        this.reservasChequeadas = 0;
        SistemaDeReserva.sigueProcesoDeCancelacion = true;
    }

    public void run() {
        while ((reservasChequeadas < listaReservasConfirmadas.size()) || SistemaDeReserva.sigueProcesoDePago) {
            ProcesarReservaConfirmada();
        }
        SistemaDeReserva.sigueProcesoDeCancelacion = false;
    }

    public void ProcesarReservaConfirmada() {
        Random random = new Random();
        synchronized (listaReservasConfirmadas) {
            // Verificar si hay reservas confirmadas
            if (!listaReservasConfirmadas.isEmpty()) {
                // Seleccionar una reserva aleatoria de la lista de confirmadas
                int indiceAleatorio = random.nextInt(listaReservasConfirmadas.size());
                Reserva reserva = listaReservasConfirmadas.get(indiceAleatorio);

                if (reserva.getCheck()) {

                } else {
                    if (random.nextDouble() < 0.1) {
                        reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                        listaReservasConfirmadas.remove(reserva);
                        synchronized (listaReservasCanceladas) {
                            listaReservasCanceladas.add(reserva);
                        }
                    } else {
                        reservasChequeadas++;
                        reserva.setCheck(true);
                    }

                }
                try {
                    Thread.sleep(0,sleep_cancelacion);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
