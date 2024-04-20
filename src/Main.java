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
        // Iniciar los hilos
        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();
        hilo5.start();
        
        
    }
}