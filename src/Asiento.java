public class Asiento {
    private EstadoAsiento estado;
    private Reserva reserva;
    public Asiento() {
        this.estado = EstadoAsiento.LIBRE;
        this.reserva = new Reserva(this);
    }


    public synchronized void descartar() {
        this.estado = EstadoAsiento.DESCARTADO;
        System.out.println("Asiento descartado por el hilo: " + Thread.currentThread().getName());
    }

    public synchronized boolean reservar() {
        if(this.estado == EstadoAsiento.LIBRE){
            this.estado = EstadoAsiento.OCUPADO;
            this.reserva.setEstado(EstadoReserva.PENDIENTE);
            //System.out.println("Asiento reservado por el hilo: " + Thread.currentThread().getName());
            return true;
        }
        else{
            //System.out.println("Asiento ya ocupado");
            return false;
        }
    }

    public synchronized boolean confirmar() {
        if(this.reserva.getEstado() == EstadoReserva.PENDIENTE){
            this.reserva.setEstado(EstadoReserva.CONFIRMADA);
            System.out.println("Asiento confirmado por el hilo: " + Thread.currentThread().getName());
            return true;
        }
        else{
            System.out.println("No se puede confirmar un asiento que no está ocupado");
            return false;
        }
    }

    public synchronized boolean cancelar() {
        if(this.reserva.getEstado() == EstadoReserva.PENDIENTE){
            this.reserva.setEstado(EstadoReserva.CANCELADA);
            this.estado = EstadoAsiento.DESCARTADO;
            System.out.println("Asiento cancelado por el hilo: " + Thread.currentThread().getName());
            return true;
        }
        else{
            System.out.println("No se puede cancelar un asiento que no está ocupado");
            return false;
        }
    }

    public synchronized boolean checkin() {
        if(this.reserva.getEstado() == EstadoReserva.CONFIRMADA){
            this.reserva.setCheck(true);
            System.out.println("Asiento checkin por el hilo: " + Thread.currentThread().getName());
            return true;
        }
        else{
            System.out.println("No se puede hacer checkin a un asiento que no está confirmado");
            return false;
        }
    }

    public synchronized boolean failedCheckin() {
        if(this.reserva.getEstado() == EstadoReserva.CONFIRMADA){
            this.reserva.setEstado(EstadoReserva.CANCELADA);
            this.estado = EstadoAsiento.DESCARTADO;
            System.out.println("Asiento checkin fallido por el hilo: " + Thread.currentThread().getName());
            return true;
        }
        else{
            System.out.println("No se puede fallar checkin a un asiento que no está confirmado");
            return false;
        }
    }
    public synchronized boolean verificar() {
        if(this.reserva.getEstado() == EstadoReserva.CONFIRMADA && this.reserva.isCheck() == true){
            this.reserva.setEstado(EstadoReserva.VERIFICADA);
            System.out.println("Asiento verificado por el hilo: " + Thread.currentThread().getName());
            return true;
        }
        else{
            System.out.println("No se puede verificar un asiento que no está confirmado");
            return false;
        }
    }

    public Reserva getReserva() {
        return reserva;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public String toString() {
        switch (estado) {
            case LIBRE:
                return (String)"L";
            case OCUPADO:
                return (String)"O";
            case DESCARTADO:
                return (String)"D";
            default:
                return (String)"";
        }
    }

}

enum EstadoAsiento {
    LIBRE, OCUPADO, DESCARTADO
}

