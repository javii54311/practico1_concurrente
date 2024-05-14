public class Main {
    public static void ejecutar(SistemaDeReservas sistema, int nHilosReserva, int nHilosPago, int nHilosCheckin, int nHilosVerificacion){
        long startTime = System.currentTimeMillis();

        Thread[] hilosReserva = new Thread[nHilosReserva];
        Thread[] hilosPago = new Thread[nHilosPago];
        Thread[] hilosCheckin = new Thread[nHilosCheckin];
        Thread[] hilosVerificacion = new Thread[nHilosVerificacion];

        for (int i = 0; i < hilosReserva.length; i++) {
            hilosReserva[i] = new Thread(sistema.getProcesoDeReserva());
            hilosReserva[i].start();
        }

        for (int i = 0; i < hilosPago.length; i++) {
            hilosPago[i] = new Thread(sistema.getProcesoDePago());
            hilosPago[i].start();
        }

        for (int i = 0; i < hilosCheckin.length; i++) {
            hilosCheckin[i] = new Thread(sistema.getProcesoDeCheckin());
            hilosCheckin[i].start();
        }

        for (int i = 0; i < hilosVerificacion.length; i++) {
            hilosVerificacion[i] = new Thread(sistema.getProcesoDeVerificacion());
            hilosVerificacion[i].start();
        }

        Thread log = new Thread(sistema.getLog());
        log.start();

        try {
            for (Thread hilo : hilosReserva) {
                hilo.join();
            }

            for (Thread hilo : hilosPago) {
                hilo.join();
            }

            for (Thread hilo : hilosCheckin) {
                hilo.join();
            }

            for (Thread hilo : hilosVerificacion) {
                hilo.join();
            }

            log.join();
        } catch (Exception e) {
            System.out.println("Error en el hilo principal");
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Tiempo de ejecución: " + totalTime + " milisegundos");
        sistema.mostrarEstadoAsientos();
        sistema.diagnosticarFinal();
    };
    public static void main(String[] args) {
        int nHilosReserva = 3;
        int sleepReserva = 1;
        int waitReserva = 1;

        int nHilosPago = 2;
        int sleepPago = 1;
        int waitPago = 1;

        int nHilosCheckin = 3;
        int sleepCheckin = 1;
        int waitCheckin = 1;

        int nHilosVerificacion = 2;
        int sleepVerificacion = 1;
        int waitVerificacion = 1;

        SistemaDeReservas sistema = new SistemaDeReservas(sleepReserva, sleepPago, sleepCheckin, sleepVerificacion, waitReserva, waitPago, waitCheckin, waitVerificacion);
        ejecutar(sistema, nHilosReserva, nHilosPago, nHilosCheckin, nHilosVerificacion);
    }
}