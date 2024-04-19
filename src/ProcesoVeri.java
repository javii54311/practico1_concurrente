import java.util.Random;

public class ProcesoVeri implements Runnable {
    private RegistroReservas registroReservas;

	public ProcesoVeri (RegistroReservas registroReservas) 
	{ 
        this.registroReservas = registroReservas;
	}
	public void run() 
	{
        Verificar();
    }

    public void Verificar() {
        synchronized(this){

        // Verificar si hay reservas confirmadas
        if (!registroReservas.getReservasConfirmadas().isEmpty()) {
            // Seleccionar una reserva aleatoria de la lista de confirmadas
            Random random = new Random();
            int indiceAleatorio = random.nextInt(registroReservas.getReservasConfirmadas().size());
            Reserva reserva = registroReservas.getReservasConfirmadas().get(indiceAleatorio);

            // Verificar si esta checkeada
            if (reserva.getCheck()) {
                System.out
                        .println("La reserva " + reserva.getFila() + " " + reserva.getColumna() + " estaba checkeada");
                System.out.println("Se la marca como verificada.");
                registroReservas.eliminarReservaConfirmada(reserva);
                registroReservas.agregarReservaVerificada(reserva);
                reserva.setEstado(EstadoReserva.VERIFICADA);
            } else {
                // La reserva no estaba checkeada
                System.out.println(
                        "La reserva " + reserva.getFila() + " " + reserva.getColumna() + " no estaba checkeada");
            }
        } else {
            System.out.println("No hay reservas confirmadas");
        }
        System.out.println(Thread.currentThread().getName());
        }
    }
}
