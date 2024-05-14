public class Reserva {
    private EstadoReserva estado;
    private boolean check;
    private Asiento asiento;
    public Reserva(Asiento asiento) {
        this.estado = EstadoReserva.PENDIENTE;
        this.check = false;
        this.asiento = asiento;
    }
    public EstadoReserva getEstado() {
        return estado;
    }
    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }
    public boolean isCheck() {
        return check;
    }
    public void setCheck(boolean check) {
        this.check = check;
    }
    public Asiento getAsiento() {
        return asiento;
    }
    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }
    public String toString() {
        switch (estado) {
            case NONATA:
                return "N";
            case PENDIENTE:
                return "P";
            case CONFIRMADA:
                return "C";
            case CANCELADA:
                return "X";
            case VERIFICADA:
                return "V";
        
            default:
                return "";
    }
    }
}

enum EstadoReserva {
   NONATA, PENDIENTE, CONFIRMADA, CANCELADA, VERIFICADA
}
