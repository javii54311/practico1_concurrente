import java.util.List;
import java.util.Random;

public class ProcesoDeVerificacion implements Runnable {
    int sleep_verificacion;
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasVerificadas;

    public ProcesoDeVerificacion(List<Reserva> listaReservasConfirmadas, List<Reserva> listaReservasVerificadas,
            int sleep_verificacion) {
        this.listaReservasConfirmadas = listaReservasConfirmadas;
        this.listaReservasVerificadas = listaReservasVerificadas;
        this.sleep_verificacion = sleep_verificacion;
        SistemaDeReserva.sigueProcesoDeVerificacion = true;
    }

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
                            
                        }
                    }
                
                    try {
                        Thread.sleep(0,sleep_verificacion);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    }
}
