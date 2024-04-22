import java.util.List;
import java.util.Random;

public class ProcesoDeReserva implements Runnable{
    long sleepReservaPendiente;
    List<Reserva> listaReservasPendientes;
    private Asiento [][] asientos;

    public ProcesoDeReserva(Asiento[][] asientos, List<Reserva> listaReservasPendientes, long sleepReservaPendiente) {
        this.asientos = asientos;
        this.sleepReservaPendiente = sleepReservaPendiente;
        this.listaReservasPendientes = listaReservasPendientes;

    }

    public Asiento getAsiento(int fila, int columna) { // Método sincronizado 
       synchronized (asientos[fila][columna]) {
            return asientos[fila][columna];
       }    
    }

    public boolean hayAsientosLibres() { 
        for (int i = 0; i < SistemaDeReserva.FILAS; i++) {
            for (int j = 0; j < SistemaDeReserva.COLUMNAS; j++) {
                if (getAsiento(i, j).getEstado() == EstadoAsiento.LIBRE) {
                    return true;
                }
            }
        }
        return false;
    }

    public void reservarAsientoAleatorio() {
        Random random = new Random();
        int filaAleatoria = random.nextInt(SistemaDeReserva.FILAS);
        int columnaAleatoria = random.nextInt(SistemaDeReserva.COLUMNAS); 

        synchronized (listaReservasPendientes) {

            Asiento asiento = asientos[filaAleatoria][columnaAleatoria]; 
            Reserva reserva = new Reserva(EstadoReserva.PENDIENTE_DE_PAGO,filaAleatoria,columnaAleatoria, asiento);
            
            if(asiento.getEstado() == EstadoAsiento.LIBRE) {
                asiento.setEstado(EstadoAsiento.OCUPADO);
                listaReservasPendientes.add(reserva);
                //System.out.printf("%s Reserva hecha en: Fila %d, Columna %d\n", Thread.currentThread().getName(),filaAleatoria, columnaAleatoria);
                try {
                    Thread.sleep(sleepReservaPendiente);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
    @Override
    public void run() {
        while (hayAsientosLibres()){
            reservarAsientoAleatorio();
        }
        System.out.print("Terminado hilos de reserva\n");
    }
}
