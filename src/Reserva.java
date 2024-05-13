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
    public boolean getCheck() {
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
}

enum EstadoReserva {
   NONATA, PENDIENTE, CONFIRMADA, CANCELADA, VERIFICADA
}
