public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        SistemaDeReservas sistema = new SistemaDeReservas();

        Thread hilo1 = new Thread(sistema.getProcesoDeReserva());
        Thread hilo2 = new Thread(sistema.getProcesoDeReserva());
        Thread hilo3 = new Thread(sistema.getProcesoDeReserva());

        Thread hilo4 = new Thread(sistema.getProcesoDePago());
        Thread hilo5 = new Thread(sistema.getProcesoDePago());

        Thread hilo6 = new Thread(sistema.getProcesoDeCheckin());
        Thread hilo7 = new Thread(sistema.getProcesoDeCheckin());
        Thread hilo8 = new Thread(sistema.getProcesoDeCheckin());

        hilo1.start();
        hilo2.start();
        hilo3.start();

        hilo4.start();
        hilo5.start();

        hilo6.start();
        hilo7.start();
        hilo8.start();
        try {
            hilo1.join();
            hilo2.join();
            hilo3.join();

            hilo4.join();
            hilo5.join();

            hilo6.join();
            hilo7.join();
            hilo8.join();
        } catch (Exception e) {
            System.out.println("Error en el hilo principal");
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Tiempo de ejecución: " + totalTime + " milisegundos");
        sistema.mostrarEstadoAsientos();

    }

}