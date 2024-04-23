import java.util.List;
import java.util.Random;

public class ProcesoDeVerificacion implements Runnable {
    long SLEEP_VERIFICACION;
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasVerificadas;

    public ProcesoDeVerificacion(List<Reserva> listaReservasConfirmadas, List<Reserva> listaReservasVerificadas,
            long SLEEP_VERIFICACION) {
        this.listaReservasConfirmadas = listaReservasConfirmadas;
        this.listaReservasVerificadas = listaReservasVerificadas;
        this.SLEEP_VERIFICACION = SLEEP_VERIFICACION;
        SistemaDeReserva.sigueProcesoDeVerificacion = true;
    }

    @Override
    public void run() {
        while (SistemaDeReserva.sigueProcesoDeCancelacion || !listaReservasConfirmadas.isEmpty()){
            VerificarReserva();
        }
        // Cuando se cumpla la condición de salida, los procesos de verificación habrán terminado
        
        SistemaDeReserva.sigueProcesoDeVerificacion = false;
    }

    private void VerificarReserva() {
            synchronized (listaReservasConfirmadas) {
                if (!listaReservasConfirmadas.isEmpty()) {
                    Random random = new Random();
                    // Seleccionar una reserva aleatoria de la lista de confirmadas
                    int indiceAleatorio = random.nextInt(listaReservasConfirmadas.size());
                    Reserva reserva = listaReservasConfirmadas.get(indiceAleatorio);

                    if (reserva.getCheck()) {
                        // La reserva estaba chequeada. Se la mueve a la lista de verificadas.
                        listaReservasConfirmadas.remove(reserva);
                        reserva.setEstado(EstadoReserva.VERIFICADA);
                        synchronized (listaReservasVerificadas) {

                            listaReservasVerificadas.add(reserva);

                            try {
                                Thread.sleep(SLEEP_VERIFICACION);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            try
                            {
                                listaReservasVerificadas.notifyAll(); 
                                listaReservasVerificadas.wait(1); 
                            }
                            catch(Exception e){
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
