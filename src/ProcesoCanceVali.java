import java.util.Random;

public class ProcesoCanceVali implements Runnable {
    private RegistroReservas registroReservas;

	public ProcesoCanceVali (RegistroReservas registroReservas) 
	{ 
        this.registroReservas = registroReservas;
	}
	public void run() 
	{
        ProcesarReservaConfirmada();
    }

    public void ProcesarReservaConfirmada() {
        Random random = new Random();
        synchronized(this){
        // Verificar si hay reservas confirmadas
        if (!registroReservas.getReservasConfirmadas().isEmpty()) {
            // Seleccionar una reserva aleatoria de la lista de confirmadas
            int indiceAleatorio = random.nextInt(registroReservas.getReservasConfirmadas().size());
            Reserva reserva = registroReservas.getReservasConfirmadas().get(indiceAleatorio);

            if (reserva.getCheck()) {
                System.out
                        .println("La reserva " + reserva.getFila() + " " + reserva.getColumna() + " estaba checkeada");
            } else {
                // Intentar cancelar la reserva con un 10% de probabilidad
                if (random.nextDouble() < 0.1) {
                    // Marcar el asiento asociado como DESCARTADO
                    System.out.println("Reserva " + reserva.getFila() + " " + reserva.getColumna() + " cancelada");
                    reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                    // Eliminar la reserva de la lista de confirmadas
                    registroReservas.eliminarReservaConfirmada(reserva);
                    // Agregar la reserva a la lista de canceladas
                    registroReservas.agregarReservaCancelada(reserva);
                } else {
                    System.out.println("Reserva " + reserva.getFila() + " " + reserva.getColumna() + " no cancelada");
                    System.out.println("Se la marca como chequeada");
                    reserva.setCheck(true);
                }
            }

        } else {
            System.out.println("No hay reservas confirmadas");
        }
        System.out.println(Thread.currentThread().getName());
    }    
    }


}
