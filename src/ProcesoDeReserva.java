import java.util.Random;

public class ProcesoDeReserva {
    private final int FILAS;
    private final int COLUMNAS;
    private Asiento [][] asientos;
    private RegistroReservas registroReservas;
    public ProcesoDeReserva(Asiento[][] asientos, RegistroReservas registroReservas, int FILAS, int COLUMNAS) {
        this.asientos = asientos;
        this.registroReservas = registroReservas;
        this.FILAS = FILAS;
        this.COLUMNAS = COLUMNAS;
    }
    public Asiento getAsiento(int fila, int columna) { // Método sincronizado 
        synchronized (asientos[fila][columna]) {
            return asientos[fila][columna];
        }    
    }
    public boolean hayAsientosLibres() { //sincronizado indirectamente
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (getAsiento(i, j).getEstado() == EstadoAsiento.LIBRE) {
                    return true;
                }
            }
        }
        return false;
    }
    public void reservarAsiento(int sleepReservaPendiente) {
     do {
        Random random = new Random();
        int filaAleatoria = random.nextInt(FILAS);
        int columnaAleatoria = random.nextInt(COLUMNAS); 
        Asiento asiento = getAsiento(filaAleatoria, columnaAleatoria); //syncronized: solo un hilo puede acceder a la vez

        Reserva reserva = new Reserva(EstadoReserva.PENDIENTE_DE_PAGO,filaAleatoria,columnaAleatoria,asiento);
        synchronized (this) {
        if (asiento.getEstado() == EstadoAsiento.LIBRE) {
            registroReservas.agregarReservaPendiente(reserva);
            asiento.setEstado(EstadoAsiento.OCUPADO);
            System.out.printf("Asiento reservado por hilo de %s. Fila: %d, Columna: %d\n", Thread.currentThread().getName(),filaAleatoria, columnaAleatoria);
            try {
                Thread.sleep(sleepReservaPendiente);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        }
        
        } while (hayAsientosLibres());
        
    }
    
}
