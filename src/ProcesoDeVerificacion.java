import java.util.List;
import java.util.Random;

public class ProcesoDeVerificacion implements Runnable {
    int sleep_verification;
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasVerificadas;

    public ProcesoDeVerificacion(List<Reserva> listaReservasConfirmadas, List<Reserva> listaReservasVerificadas,
            int sleep_verification) {
        this.listaReservasConfirmadas = listaReservasConfirmadas;
        this.listaReservasVerificadas = listaReservasVerificadas;
        this.sleep_verification = sleep_verification;
        SistemaDeReserva.sigueProcesoDeVerificacion = true;
    }

    public void run() {
        VerificarReservas();
        SistemaDeReserva.sigueProcesoDeVerificacion = false;
    }

    private boolean debeSeguirProceso() {
        boolean debeContinuarProceso = SistemaDeReserva.sigueProcesoDeCancelacion || !listaReservasConfirmadas.isEmpty();
        return debeContinuarProceso;
    }

    public void VerificarReservas() {
        while (debeSeguirProceso())
            synchronized (listaReservasConfirmadas) {
                if (!listaReservasConfirmadas.isEmpty()) {
                    Random random = new Random();
                    int indiceAleatorio = random.nextInt(listaReservasConfirmadas.size());
                    Reserva reserva = listaReservasConfirmadas.get(indiceAleatorio);

                    if (reserva.getCheck()) {

                        //System.out.println("La reserva " + reserva.getFila() + " " + reserva.getColumna() + " estaba checkeada");
                        //System.out.println("Se la marca como verificada.");
                        listaReservasConfirmadas.remove(reserva);
                        reserva.setEstado(EstadoReserva.VERIFICADA);
                        synchronized (listaReservasVerificadas) {
                            listaReservasVerificadas.add(reserva);
                            try {
                                Thread.sleep(sleep_verification);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // La reserva no estaba checkeada
                        //System.out.println("La reserva " + reserva.getFila() + " " + reserva.getColumna() + " no estaba checkeada");
                    }
                } 
            }
    }
}
