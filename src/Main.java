public class Main {
    static class Contador {
        int valor;
    
         Contador(int valor) {
            this.valor = valor;
        }
        void increment() {
            this.valor++;
        }
    }
    public static void ejecutar(SistemaDeReservas sistema, int nHilosReserva, int nHilosPago, int nHilosCheckin, int nHilosVerificacion, Contador contEjExitosas){
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
        if(sistema.diagnosticarFinal()){contEjExitosas.increment();}
    };
    public static void main(String[] args) {
        //config
        int nHilosReserva = 3;
        long sleepReserva = 450; //ms
        long waitReserva = 1; //ms

        int nHilosPago = 2;
        long sleepPago = 450; //ms
        long waitPago = 1; //ms

        int nHilosCheckin = 3;
        long sleepCheckin = 450; //ms
        long waitCheckin = 1; //ms

        int nHilosVerificacion = 2;
        long sleepVerificacion = 450; //ms
        long waitVerificacion = 1; //ms

        SistemaDeReservas sistema = new SistemaDeReservas(sleepReserva, sleepPago, sleepCheckin, sleepVerificacion, waitReserva, waitPago, waitCheckin, waitVerificacion);
        
        
        Contador exitos = new Contador(0);
        for(int i = 0; i < 100; i++){
            ejecutar(sistema, nHilosReserva, nHilosPago, nHilosCheckin, nHilosVerificacion, exitos);
        }

        System.out.println("Ejecuciones exitosas: " + exitos.valor);
    
    }
}