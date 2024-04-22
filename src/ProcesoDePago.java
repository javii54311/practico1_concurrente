import java.util.Random;
import java.util.List;

public class ProcesoDePago implements Runnable {
    List<Reserva> listaReservasPendientes;
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasCanceladas;
    int reservasProcesadas;
    long sleep_pago;
    boolean sigueProcesoActual;

    public ProcesoDePago(List<Reserva> pendientes, List<Reserva> confirmadas, List<Reserva> canceladas, long sleep_pago) {
        
        this.listaReservasPendientes = pendientes;
        this.listaReservasConfirmadas = confirmadas;
        this.listaReservasCanceladas = canceladas;
        this.reservasProcesadas = 0;
        this.sleep_pago = sleep_pago;
    }

    public void run() {
        while (reservasProcesadas < SistemaDeReserva.CANTIDAD_ASIENTOS) {
            pagarAsientoAleatorio();
        }
        SistemaDeReserva.sigueProcesoDePago = false;
    }

    public void pagarAsientoAleatorio() {
        Random random = new Random();
        synchronized (listaReservasPendientes) {
            // Obtener una reserva aleatoria de la lista de reservas pendientes
            if (!listaReservasPendientes.isEmpty()) {
                reservasProcesadas++;
                int indiceAleatorio = random.nextInt(listaReservasPendientes.size());
                Reserva reserva = listaReservasPendientes.get(indiceAleatorio);

                // Intentar pagar la reserva
                if (random.nextDouble() < 0.9) { // 90% de probabilidad de éxito
                    synchronized (listaReservasConfirmadas) {
                        listaReservasPendientes.remove(reserva);
                        listaReservasConfirmadas.add(reserva);
                        reserva.setEstado(EstadoReserva.CONFIRMADA);
                        try {
                            Thread.sleep(sleep_pago);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
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
                } else {
                    synchronized (listaReservasCanceladas) {
                        reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                        listaReservasPendientes.remove(reserva);
                        listaReservasCanceladas.add(reserva);
                        reserva.setEstado(EstadoReserva.CANCELADA);

                        try {
                            Thread.sleep(sleep_pago);
                        } catch (InterruptedException e) {
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
                }

            }
            try
            {
                listaReservasPendientes.notifyAll(); 
                listaReservasPendientes.wait(1);  
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}
