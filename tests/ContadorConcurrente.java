public class ContadorConcurrente implements Runnable {
    private int contador = 0;

    @Override
    public void run() {
        for(int i = 0; i < 1000; i++){
            synchronized (this){
                contador++;
                System.out.println(Thread.currentThread().getName() + " " + contador);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getContador(){
        return contador;
    }

    public static void main(String[] args) {
        ContadorConcurrente contador = new ContadorConcurrente();

        Thread hilo1 = new Thread(contador);
        Thread hilo2 = new Thread(contador);

        hilo1.start();
        hilo2.start();
        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("El contador es: " + contador.getContador());
    }
}