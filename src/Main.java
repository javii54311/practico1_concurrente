public class Main {


    public void generarCorrida(long SLEEP_PENDIENTE, long SLEEP_PAGO, long SLEEP_CANCELACION, long SLEEP_VERIFICACION){
        // Crear un sistema de reserva
        SistemaDeReserva sistemaDeReserva = new SistemaDeReserva(SLEEP_PENDIENTE, SLEEP_PAGO, SLEEP_CANCELACION, SLEEP_VERIFICACION);

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
        try {
            hilo1.join();
            hilo2.join();
            hilo3.join();
            hilo4.join();
            hilo5.join();
            hilo6.join();
            hilo7.join();
            hilo8.join();
            hilo9.join();
            hilo10.join();
            hilo11.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        for(int i= 0; i<100; i++){
            Main main = new Main();
            main.generarCorrida(1, 1, 1, 1);
        }
        
    }
}