import java.util.Random;

//Proceso de Pago 
public class ProcesoPago implements Runnable {
    private RegistroReservas registroReservas;

	public ProcesoPago (RegistroReservas registroReservas) 
	{ 
        this.registroReservas = registroReservas;
	}
	public void run() 
	{
        procesarReservasPendientes();
    }

    public void procesarReservasPendientes() {
        Random random = new Random();
        synchronized(this){
        // Obtener una reserva aleatoria de la lista de reservas pendientes
        if (!registroReservas.getReservasPendientes().isEmpty()) {
            int indiceAleatorio = random.nextInt(registroReservas.getReservasPendientes().size());
            Reserva reserva = registroReservas.getReservasPendientes().get(indiceAleatorio);

            // Intentar pagar la reserva
            if (random.nextDouble() < 0.9) { // 90% de probabilidad de éxito
                System.out.println("Reserva " + reserva.getFila() + " " + reserva.getColumna() + " pagada");
                // Eliminar la reserva de la lista de pendientes
                registroReservas.eliminarReservaPendiente(reserva);
                // Agregar la reserva a la lista de confirmadas
                registroReservas.agregarReservaConfirmada(reserva);
                reserva.setEstado(EstadoReserva.CONFIRMADA);
            } else {
                // 10% de probabilidad de fracaso
                System.out.println("Reserva " + reserva.getFila() + " " + reserva.getColumna() + " no pagada");
                // Colocar el asiento en estado DESCARTADO
                reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                // Eliminar la reserva de la lista de pendientes
                registroReservas.eliminarReservaPendiente(reserva);
                // Agregar la reserva a la lista de canceladas
                registroReservas.agregarReservaCancelada(reserva);
                reserva.setEstado(EstadoReserva.CANCELADA);
            }
        } else {
            System.out.println("No hay reservas pendientes");
        }
        System.out.println(Thread.currentThread().getName());
    }
    }


}
