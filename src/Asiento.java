public class Asiento {
    private EstadoAsiento estado;
    private Reserva reserva;
    public Asiento() {
        this.estado = EstadoAsiento.LIBRE;
        this.reserva = new Reserva(this);
    }


    public synchronized void descartar() {
        this.estado = EstadoAsiento.DESCARTADO;
        //System.out.println("Asiento descartado por el hilo: " + Thread.currentThread().getName());
    }

    public synchronized boolean reservar() {
        if(this.estado == EstadoAsiento.LIBRE){
            this.estado = EstadoAsiento.OCUPADO;
            this.reserva.setEstado(EstadoReserva.PENDIENTE);
            //System.out.println("Asiento reservado por el hilo: " + Thread.currentThread().getName());
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                
            }
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
            //System.out.println("Asiento confirmado por el hilo: " + Thread.currentThread().getName());
            return true;
        }
        else{
            //System.out.println("No se puede confirmar un asiento que no está ocupado");
            return false;
        }
    }

    public synchronized boolean cancelar_pago() {
        if(this.reserva.getEstado() == EstadoReserva.PENDIENTE){
            this.reserva.setEstado(EstadoReserva.CANCELADA);
            this.estado = EstadoAsiento.DESCARTADO;
            //System.out.println("Asiento cancelado por el hilo: " + Thread.currentThread().getName());
            return true;
        }
        else{
            System.out.println("No se puede cancelar un asiento que no está ocupado");
            return false;
        }
    }

    public synchronized void cancelar_validacion() {
        this.reserva.setEstado(EstadoReserva.CANCELADA);
        this.estado = EstadoAsiento.DESCARTADO;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

}

enum EstadoAsiento {
    LIBRE, OCUPADO, DESCARTADO
}