// Enumeración para los estados de los asientos
enum EstadoAsiento {
    LIBRE, OCUPADO, DESCARTADO
}

// Clase Asiento
public class Asiento {
    
    private EstadoAsiento estado;

    public Asiento() {
        this.estado = EstadoAsiento.LIBRE;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        synchronized (this) {
        this.estado = estado;
        }
    }
}