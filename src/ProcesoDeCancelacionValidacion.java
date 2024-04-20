import java.util.Random;
import java.util.List;

public class ProcesoDeCancelacionValidacion implements Runnable{
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasCanceladas;
    int reservasChequeadas;
    int sleep_verificacion;

    public ProcesoDeCancelacionValidacion(List<Reserva> listaReservasConfirmadas,
            List<Reserva> listaReservasCanceladas, int sleep_verificacion) {
        this.listaReservasConfirmadas = listaReservasConfirmadas;
        this.listaReservasCanceladas = listaReservasCanceladas;
        this.sleep_verificacion = sleep_verificacion;
        this.reservasChequeadas = 0;
        SistemaDeReserva.sigueProcesoDeCancelacion = true;
    }

    public boolean debeSeguirProceso() {
        boolean quedanReservasPorProcesar = reservasChequeadas < listaReservasConfirmadas.size();
        return quedanReservasPorProcesar || SistemaDeReserva.sigueProcesoDePago;
    }

    public void run() {
        ProcesarReservaConfirmada();
        SistemaDeReserva.sigueProcesoDeCancelacion = false;
    }

    public void ProcesarReservaConfirmada() {
        while (debeSeguirProceso()) {
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
                            System.out.println(Thread.currentThread().getName() +" Chequeo hecho en: Fila " + reserva.getFila() + ", Columna " + reserva.getColumna() + " cancelada");
                            reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                            listaReservasConfirmadas.remove(reserva);
                            synchronized (listaReservasCanceladas) {
                                listaReservasCanceladas.add(reserva);
                            }
                        } else {
                            reservasChequeadas ++;
                            System.out.println(Thread.currentThread().getName() +" Chequeo hecho en: Fila " + reserva.getFila() + ", Columna " + reserva.getColumna() + " completada");
                            reserva.setCheck(true);
                        }

                    }

                } else {
                    //System.out.println("No hay reservas confirmadas");
                }
                try 
                {
                    Thread.sleep(sleep_verificacion); 
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
