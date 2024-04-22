// Enumeración para los estados de las reservas
enum EstadoReserva {
    CREADA,PENDIENTE_DE_PAGO, CANCELADA, CONFIRMADA, VERIFICADA
}
class Reserva {

    private EstadoReserva estado;
    private int fila;
    private int columna;
    private Asiento asiento;

    private boolean check;


    public Reserva(EstadoReserva estado, int fila, int columna, Asiento asiento) {
        this.estado = estado;
        this.fila = fila;
        this.columna = columna;
        this.asiento = asiento;
        this.check = false;

    }
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
