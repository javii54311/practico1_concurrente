public class Main {
    public static void main(String[] args) {

        // Crear un sistema de reserva
        SistemaDeReserva sistemaDeReserva = new SistemaDeReserva();

        // Crear hilos de reserva
        Thread hilo1 = new Thread(sistemaDeReserva.getProcesoDeReserva(), "Hilo de reserva 1");
        Thread hilo2 = new Thread(sistemaDeReserva.getProcesoDeReserva(), "Hilo de reserva 2");
        Thread hilo3 = new Thread(sistemaDeReserva.getProcesoDeReserva(), "Hilo de reserva 3");
        
        Thread hilo4 = new Thread(sistemaDeReserva.getProcesoDePago(), "Hilo de pago 1");
        Thread hilo5 = new Thread(sistemaDeReserva.getProcesoDePago(), "Hilo de pago 2");
        
        Thread hilo6 = new Thread(sistemaDeReserva.getProcesoDeCancelacionValidacion(), "Hilo de cancelacion 1");
        Thread hilo7 = new Thread(sistemaDeReserva.getProcesoDeCancelacionValidacion(), "Hilo de cancelacion 2");
        Thread hilo8 = new Thread(sistemaDeReserva.getProcesoDeCancelacionValidacion(), "Hilo de cancelacion 3");
        
        Thread hilo9 = new Thread(sistemaDeReserva.getProcesoDeVerificacion(), "Hilo de verificacion 1");
        Thread hilo10 = new Thread(sistemaDeReserva.getProcesoDeVerificacion(), "Hilo de verificacion 2");
        Thread hilo11 = new Thread(sistemaDeReserva.getLogDeReservas(), "Log");
        // Iniciar los hilos
        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();
        hilo5.start();
        hilo6.start();
        hilo7.start();
        hilo8.start();
        hilo9.start();
        hilo10.start();
        hilo11.start();
        
    }
}