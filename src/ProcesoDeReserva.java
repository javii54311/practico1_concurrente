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

        // Seleccionar un asiento aleatorio
        Random random = new Random();
        int filaAleatoria = random.nextInt(SistemaDeReserva.FILAS);
        int columnaAleatoria = random.nextInt(SistemaDeReserva.COLUMNAS); 

        synchronized (listaReservasPendientes) {

            Asiento asiento = asientos[filaAleatoria][columnaAleatoria]; 
            Reserva reserva = new Reserva(EstadoReserva.PENDIENTE_DE_PAGO,filaAleatoria,columnaAleatoria, asiento);
            
            if(asiento.getEstado() == EstadoAsiento.LIBRE) {
                // El asiento seleccionado se encontraba libre.
                asiento.setEstado(EstadoAsiento.OCUPADO);
                listaReservasPendientes.add(reserva);

                // Se simula el tiempo de espera.
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
        // Ya se han reservado todos los asientos.
    }
}
