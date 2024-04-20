public class Proceso {
    protected Asiento [][] asientos;
    public Proceso(Asiento[][] asientos) {
        this.asientos = asientos;
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
}
