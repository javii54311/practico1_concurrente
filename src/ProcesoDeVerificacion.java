import java.util.List;
import java.util.Random;

public class ProcesoDeVerificacion implements Runnable {
    long sleep_verificacion;
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasVerificadas;

    public ProcesoDeVerificacion(List<Reserva> listaReservasConfirmadas, List<Reserva> listaReservasVerificadas,
            long sleep_verificacion) {
        this.listaReservasConfirmadas = listaReservasConfirmadas;
        this.listaReservasVerificadas = listaReservasVerificadas;
        this.sleep_verificacion = sleep_verificacion;
        SistemaDeReserva.sigueProcesoDeVerificacion = true;
    }

    @Override
    public void run() {
        while (SistemaDeReserva.sigueProcesoDeCancelacion || !listaReservasConfirmadas.isEmpty()){
            VerificarReserva();
        }
        
        SistemaDeReserva.sigueProcesoDeVerificacion = false;
    }

    public void VerificarReserva() {
            synchronized (listaReservasConfirmadas) {
                if (!listaReservasConfirmadas.isEmpty()) {
                    Random random = new Random();
                    int indiceAleatorio = random.nextInt(listaReservasConfirmadas.size());
                    Reserva reserva = listaReservasConfirmadas.get(indiceAleatorio);

                    if (reserva.getCheck()) {
                        listaReservasConfirmadas.remove(reserva);
                        reserva.setEstado(EstadoReserva.VERIFICADA);
                        synchronized (listaReservasVerificadas) {
                            listaReservasVerificadas.add(reserva);

                            try {
                                Thread.sleep(sleep_verificacion);
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
