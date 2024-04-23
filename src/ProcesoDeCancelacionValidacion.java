import java.util.Random;
import java.util.List;

public class ProcesoDeCancelacionValidacion implements Runnable {
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasCanceladas;
    long SLEEP_CANCELACION;

    public ProcesoDeCancelacionValidacion(List<Reserva> listaReservasConfirmadas,
    List<Reserva> listaReservasCanceladas, long SLEEP_CANCELACION) {

        this.listaReservasConfirmadas = listaReservasConfirmadas;
        this.listaReservasCanceladas = listaReservasCanceladas;
        this.SLEEP_CANCELACION = SLEEP_CANCELACION;

        SistemaDeReserva.sigueProcesoDeCancelacion = true;
    }

    @Override
    public void run() {
        while (SistemaDeReserva.sigueProcesoDePago || !listaReservasConfirmadas.isEmpty()) {
            ProcesarReservaConfirmada();
        }
        // Cuando se cumpla la condición de salida, los procesos de cancelacion/validacion habran terminado
        SistemaDeReserva.sigueProcesoDeCancelacion = false;
    }

    private void ProcesarReservaConfirmada() {
        Random random = new Random();
        synchronized (listaReservasConfirmadas) {
            // Verificar si hay reservas confirmadas
            if (!listaReservasConfirmadas.isEmpty()) {
                // Seleccionar una reserva aleatoria de la lista de confirmadas
                int indiceAleatorio = random.nextInt(listaReservasConfirmadas.size());
                Reserva reserva = listaReservasConfirmadas.get(indiceAleatorio);

                if (reserva.getCheck()) {
                    // La reserva ya ha sido procesada
                } else {
                    if (random.nextDouble() < 0.1) { // 10% de probabilidad de que la reserva sea cancelada
                        reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                        listaReservasConfirmadas.remove(reserva);
                        synchronized (listaReservasCanceladas) {
                            listaReservasCanceladas.add(reserva);
                            try {
                                Thread.sleep(SLEEP_CANCELACION);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try
                            {
                                listaReservasCanceladas.notifyAll(); 
                                listaReservasCanceladas.wait(1); 
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                    } else {
                        // La reserva ha sido validada con un 90% de probabilidad
                        reserva.setCheck(true);

                        try {
                            Thread.sleep(SLEEP_CANCELACION);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
            try
            {
                listaReservasConfirmadas.notifyAll(); 
                listaReservasConfirmadas.wait(1); 
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }

    }

}
