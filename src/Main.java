public class Main {
    public static void main(String[] args) {
        SistemaDeReservas sistema = new SistemaDeReservas();

        Thread hilo1 = new Thread(sistema.getProcesoDeReserva());
        Thread hilo2 = new Thread(sistema.getProcesoDeReserva());
        Thread hilo3 = new Thread(sistema.getProcesoDeReserva());

        Thread hilo4 = new Thread(sistema.getProcesoDePago());
        Thread hilo5 = new Thread(sistema.getProcesoDePago());

        hilo1.start();
        hilo2.start();
        hilo3.start();

        hilo4.start();
        hilo5.start();

        try {
            hilo1.join();
            hilo2.join();
            hilo3.join();

            hilo4.join();
            hilo5.join();
        } catch (Exception e) {
            System.out.println("Error en el hilo principal");
        }

    }

}