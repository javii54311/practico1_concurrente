import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//Proceso de Reserva
public class ProcesoReserva implements Runnable {
    private Asiento[][] asientos;
    private RegistroReservas registroReservas;
    private static final int LEAST = 10;
	private static final int BOUND = 20; 
    private int asientoslibres;

	public ProcesoReserva (Asiento[][] asientos, RegistroReservas registroReservas) 
	{ 
        this.asientos = asientos;
        this.registroReservas = registroReservas;
        asientoslibres = Sistema.FILAS * Sistema.COLUMNAS;
	}
	public void run() 
	{
        reservarAsientoAleatorio();
	}

    // Método para intentar reservar un asiento aleatorio
    public void reservarAsientoAleatorio() {

        while(asientoslibres>0){

        synchronized(registroReservas){
            Random random = new Random();
            Reserva reserva = new Reserva();
    
            int filaAleatoria, columnaAleatoria;
            boolean asientoEncontrado = false;
        
        // Intentar reservar un asiento aleatorio hasta que se encuentre uno libre
        do {
            filaAleatoria = random.nextInt(Sistema.FILAS);
            columnaAleatoria = random.nextInt(Sistema.COLUMNAS);
            Asiento asiento = asientos[filaAleatoria][columnaAleatoria];
            if (asiento.getEstado() == EstadoAsiento.LIBRE) {
                asiento.setEstado(EstadoAsiento.OCUPADO);
                reserva.setEstado(EstadoReserva.PENDIENTE_DE_PAGO);
                reserva.setFila(filaAleatoria);
                reserva.setColumna(columnaAleatoria);
                reserva.setAsiento(asiento);
                registroReservas.agregarReservaPendiente(reserva);
                asientoEncontrado = true;
                asientoslibres--;
                System.out.printf(Thread.currentThread().getName() + ": Asiento reservado. Fila: %d, Columna: %d\n", filaAleatoria, columnaAleatoria);
            }
        } while (!asientoEncontrado);

        try 
        {
            Thread.sleep(ThreadLocalRandom.current().nextInt(LEAST, BOUND)); 
        }
        catch(Exception e) {
            e.printStackTrace();
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

        }
    } 
    }

}