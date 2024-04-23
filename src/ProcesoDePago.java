import java.util.Random;
import java.util.List;

public class ProcesoDePago implements Runnable {
    List<Reserva> listaReservasPendientes;
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasCanceladas;
    int reservasProcesadas;
    long SLEEP_PAGO;
    boolean sigueProcesoActual;

    public ProcesoDePago(List<Reserva> pendientes, List<Reserva> confirmadas, List<Reserva> canceladas, long SLEEP_PAGO) {
        
        this.listaReservasPendientes = pendientes;
        this.listaReservasConfirmadas = confirmadas;
        this.listaReservasCanceladas = canceladas;
        this.reservasProcesadas = 0;
        this.SLEEP_PAGO = SLEEP_PAGO;
    }
    
    @Override
    public void run() {
        while (reservasProcesadas < SistemaDeReserva.CANTIDAD_ASIENTOS) {
            pagarAsientoAleatorio();
        }
        // Ya se han procesado todas las reservas pendientes.
        SistemaDeReserva.sigueProcesoDePago = false;
    }

    private void pagarAsientoAleatorio() {
        Random random = new Random();
        synchronized (listaReservasPendientes) {
            if (!listaReservasPendientes.isEmpty()) {
                // Una vez verificado que hay al menos una reserva pendiente, se procedera a resolverla.
                reservasProcesadas++;

                // Se selecciona aleatoriamente una reserva pendiente.
                int indiceAleatorio = random.nextInt(listaReservasPendientes.size());
                Reserva reserva = listaReservasPendientes.get(indiceAleatorio);

                // Intentar pagar la reserva.
                if (random.nextDouble() < 0.9) { // 90% de probabilidad de que la reserva sea pagada
                    synchronized (listaReservasConfirmadas) {
                        listaReservasPendientes.remove(reserva);
                        listaReservasConfirmadas.add(reserva);
                        reserva.setEstado(EstadoReserva.CONFIRMADA);

                        // Se simula el tiempo de espera.
                        try {
                            Thread.sleep(SLEEP_PAGO);
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
                    // Con un 10% de probabilidad, la reserva no se paga y se cancela.
                    synchronized (listaReservasCanceladas) {
                        reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                        listaReservasPendientes.remove(reserva);
                        listaReservasCanceladas.add(reserva);
                        reserva.setEstado(EstadoReserva.CANCELADA);

                        // Se simula el tiempo de espera.
                        try {
                            Thread.sleep(SLEEP_PAGO);
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
