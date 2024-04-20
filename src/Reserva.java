class Reserva {
    private EstadoReserva estado;
    private int fila;
    private int columna;
    private Asiento asiento;

    private boolean check;

    public Reserva() {
        this.estado = EstadoReserva.CREADA;
        this.check = false;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }
    public Asiento getAsiento() {
        return asiento;
    }
    public void setEstado(EstadoReserva estado) {

        this.estado = estado;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean getCheck() {
        return check;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}

// Enumeración para los estados de las reservas
enum EstadoReserva {
    CREADA,PENDIENTE_DE_PAGO, CANCELADA, CONFIRMADA, VERIFICADA
}