import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//Proceso de Pago 
public class ProcesoPago implements Runnable {
    private RegistroReservas registroReservas;
    private static final int LEAST = 10;
	private static final int BOUND = 20; 
    private int reservaspend;

	public ProcesoPago (RegistroReservas registroReservas) 
	{ 
        this.registroReservas = registroReservas;
        reservaspend = Sistema.FILAS * Sistema.COLUMNAS;
	}
	public void run() 
	{
        procesarReservasPendientes();
    }

    public void procesarReservasPendientes() {
        
        while(reservaspend>0){

        synchronized(registroReservas){
            Random random = new Random();
            // Obtener una reserva aleatoria de la lista de reservas pendientes
        if (!registroReservas.getReservasPendientes().isEmpty()) {
            int indiceAleatorio = random.nextInt(registroReservas.getReservasPendientes().size());
            Reserva reserva = registroReservas.getReservasPendientes().get(indiceAleatorio);

            // Intentar pagar la reserva
            if (random.nextDouble() < 0.9) { // 90% de probabilidad de éxito
                System.out.println(Thread.currentThread().getName() + ": Reserva " + reserva.getFila() + " " + reserva.getColumna() + " pagada");
                // Eliminar la reserva de la lista de pendientes
                registroReservas.eliminarReservaPendiente(reserva);
                // Agregar la reserva a la lista de confirmadas
                registroReservas.agregarReservaConfirmada(reserva);
                reserva.setEstado(EstadoReserva.CONFIRMADA);
            } else {
                // 10% de probabilidad de fracaso
                System.out.println(Thread.currentThread().getName() + ": Reserva " + reserva.getFila() + " " + reserva.getColumna() + " no pagada");
                // Colocar el asiento en estado DESCARTADO
                reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                // Eliminar la reserva de la lista de pendientes
                registroReservas.eliminarReservaPendiente(reserva);
                // Agregar la reserva a la lista de canceladas
                registroReservas.agregarReservaCancelada(reserva);
                reserva.setEstado(EstadoReserva.CANCELADA);
            }
            reservaspend--;
            try 
            {
                Thread.sleep(ThreadLocalRandom.current().nextInt(LEAST, BOUND)); 
            }
            catch(Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("No hay reservas pendientes");
        }

        try
        {
            registroReservas.notifyAll(); 
            registroReservas.wait(1); 
            //el hilo actual espera un milisegundo antes de volver a intentar adquirir el bloqueo, lo que evita que un hilo monopolice el bloqueo por mucho tiempo.
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println();
    }
    }
}

}
