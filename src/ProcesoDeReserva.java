import java.util.List;
import java.util.Random;

public class ProcesoDeReserva implements Runnable{
    int sleepReservaPendiente;
    List<Reserva> listaReservasPendientes;
    private Asiento [][] asientos;

    public ProcesoDeReserva(Asiento[][] asientos, List<Reserva> listaReservasPendientes, int sleepReservaPendiente) {
        this.asientos = asientos;
        this.sleepReservaPendiente = sleepReservaPendiente;
        this.listaReservasPendientes = listaReservasPendientes;

    }

    public Asiento getAsiento(int fila, int columna) { // Método sincronizado 
        synchronized (asientos[fila][columna]) {
            return asientos[fila][columna];
        }    
    }

    public boolean hayAsientosLibres() { //sincronizado indirectamente
        for (int i = 0; i < SistemaDeReserva.FILAS; i++) {
            for (int j = 0; j < SistemaDeReserva.COLUMNAS; j++) {
                if (getAsiento(i, j).getEstado() == EstadoAsiento.LIBRE) {
                    return true;
                }
            }
        }
        return false;
    }

    public void reservarAsientoAleatorio(int sleepReservaPendiente) {
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
                    Thread.sleep(0,sleepReservaPendiente);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    @Override
    public void run() {
        while (hayAsientosLibres()){
            reservarAsientoAleatorio(sleepReservaPendiente);
        }
        
    }
}
