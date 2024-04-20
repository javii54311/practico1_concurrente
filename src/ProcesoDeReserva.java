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
    public boolean reservarAsiento(Asiento asiento, Reserva reserva, int filaAleatoria, int columnaAleatoria) {
     synchronized (this) {
        if (asiento.getEstado() == EstadoAsiento.LIBRE) {
            registroReservas.agregarReservaPendiente(reserva);
            asiento.setEstado(EstadoAsiento.OCUPADO);
            System.out.printf("Asiento reservado por hilo de %s. Fila: %d, Columna: %d\n", Thread.currentThread().getName(),filaAleatoria, columnaAleatoria);
            return true;
        }
        return false;
     }
        
    }
}
