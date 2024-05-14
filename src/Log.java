import java.util.List;
import java.util.logging.*;

public class Log implements Runnable {
    private static final Logger logger = Logger.getLogger(Log.class.getName());
    private List<Reserva> reservasCanceladas;
    private List<Reserva> reservasVerificadas;
    private long tiempoInicio;


    public Log(List<Reserva> reservasCanceladas, List<Reserva> reservasVerificadas){

        this.reservasCanceladas = reservasCanceladas;
        this.reservasVerificadas = reservasVerificadas;

        try {
            FileHandler fileHandler = new FileHandler("log.txt");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tiempoInicio = System.currentTimeMillis(); // Guardar el tiempo de inicio
    }

    @Override
    public void run() {
        while (SistemaDeReservas.hilosVerificando.get() > 0 ){

                logger.info("-----------------------STARTED LOG ITERATION-----------------------------------------");
                logger.info("Tamaño de la lista de Reservas Canceladas: " + reservasCanceladas.size());
                logger.info("Tamaño de la lista de Reservas Verificadas: " + reservasVerificadas.size());
                logger.info("-----------------------ENDED LOG ITERATION-----------------------------------------");

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


                logger.info("Tamaño final de la lista de Reservas Canceladas:  " + reservasCanceladas.size());
                logger.info("Tamaño final de la lista de Reservas Verificadas: " + reservasVerificadas.size());


        int capacidad = reservasCanceladas.size() + reservasVerificadas.size();
        int ocupacionTotal = reservasVerificadas.size();
        long duracion = System.currentTimeMillis() - tiempoInicio; // Calcular la duración
        logger.info("Ocupación total: " + ocupacionTotal + " de " + capacidad + ", Duración del programa: " + duracion + " ms");
    }
}