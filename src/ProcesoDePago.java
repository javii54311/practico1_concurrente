import java.util.Random;
import java.util.List;
public class ProcesoDePago implements Runnable{
    List<Reserva> listaReservasPendientes;
    List<Reserva> listaReservasConfirmadas;
    List<Reserva> listaReservasCanceladas;
    int reservasProcesadas = 0;
    int sleep_pago;
	public ProcesoDePago (List<Reserva> listaReservasPendientes, List<Reserva> listaReservasConfirmadas, List<Reserva> listaReservasCanceladas, int sleep_pago) 
    { 
        this.listaReservasPendientes = listaReservasPendientes;
        this.listaReservasConfirmadas = listaReservasConfirmadas;
        this.listaReservasCanceladas = listaReservasCanceladas;
        this.sleep_pago = sleep_pago;
    }
	public void run() 
	{
        pagarAsientosAleatorios();
    }

    public void pagarAsientosAleatorios() {
        
        while(reservasProcesadas < SistemaDeReserva.CANTIDAD_ASIENTOS){
        Random random = new Random();
        synchronized(listaReservasPendientes) {
            // Obtener una reserva aleatoria de la lista de reservas pendientes
        if (!listaReservasPendientes.isEmpty()) {
            reservasProcesadas++;
            int indiceAleatorio = random.nextInt(listaReservasPendientes.size());
            Reserva reserva = listaReservasPendientes.get(indiceAleatorio);

            // Intentar pagar la reserva
            if (random.nextDouble() < 0.9) { // 90% de probabilidad de éxito
                synchronized (listaReservasConfirmadas) {
                    // Pagar la reserva
                    System.out.println(Thread.currentThread().getName() + ": Reserva " + reserva.getFila() + " " + reserva.getColumna() + " pagada");
                    listaReservasPendientes.remove(reserva);
                    listaReservasConfirmadas.add(reserva);
                    reserva.setEstado(EstadoReserva.CONFIRMADA);
                }
            } else {
                synchronized (listaReservasCanceladas) {
                // 10% de probabilidad de fracaso
                System.out.println(Thread.currentThread().getName() + ": Reserva " + reserva.getFila() + " " + reserva.getColumna() + " no pagada");
                // Colocar el asiento en estado DESCARTADO
                reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                listaReservasPendientes.remove(reserva);
                listaReservasCanceladas.add(reserva);
                reserva.setEstado(EstadoReserva.CANCELADA);
                }
            }
            try 
            {
                Thread.sleep(sleep_pago); 
            }
            catch(Exception e) {
                e.printStackTrace();
            }

        } else {
        //    System.out.println("No hay reservas pendientes");
        }

        // try
        // {
        //     registroReservas.notifyAll(); 
        //     registroReservas.wait(1); 
        //     //el hilo actual espera un milisegundo antes de volver a intentar adquirir el bloqueo, lo que evita que un hilo monopolice el bloqueo por mucho tiempo.
        // }
        // catch(Exception e){
        //     e.printStackTrace();
        // }
    }
    }
}

}
