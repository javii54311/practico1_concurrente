enum EstadoAsiento {
    LIBRE,
    OCUPADO,
    DESCARTADO
}

public class Asientos {
    
    private EstadoAsiento estado;

    public Asientos() {
        this.estado = EstadoAsiento.LIBRE;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }
}