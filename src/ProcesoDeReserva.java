import java.util.List;
import java.util.Random;

public class ProcesoDeReserva extends Proceso implements Runnable{
    int sleepReservaPendiente;
    List<Reserva> listaReservasPendientes;
    public ProcesoDeReserva(Asiento[][] asientos, List<Reserva> listaReservasPendientes, int sleepReservaPendiente) {
        super(asientos);
        this.sleepReservaPendiente = sleepReservaPendiente;
        this.listaReservasPendientes = listaReservasPendientes;

    }

    public void reservarAsientosAleatorios(int sleepReservaPendiente) {
     do {
        Random random = new Random();
        int filaAleatoria = random.nextInt(SistemaDeReserva.FILAS);
        int columnaAleatoria = random.nextInt(SistemaDeReserva.COLUMNAS); 
        synchronized (listaReservasPendientes) {
            Asiento asiento = getAsiento(filaAleatoria, columnaAleatoria); //syncronized: solo un hilo puede acceder a la vez
            Reserva reserva = new Reserva(EstadoReserva.PENDIENTE_DE_PAGO,filaAleatoria,columnaAleatoria,asiento);
            if(asiento.getEstado() == EstadoAsiento.LIBRE) {
                asiento.setEstado(EstadoAsiento.OCUPADO);
                synchronized(listaReservasPendientes){
                    listaReservasPendientes.add(reserva);
                }
                //System.out.printf("%s Reserva hecha en: Fila %d, Columna %d\n", Thread.currentThread().getName(),filaAleatoria, columnaAleatoria);
                try {
                    Thread.sleep(sleepReservaPendiente);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
     } while (hayAsientosLibres());
        
    }
    @Override
    public void run() {
        reservarAsientosAleatorios(sleepReservaPendiente);
    }
}
