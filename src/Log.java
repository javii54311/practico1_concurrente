import java.util.List;
import java.util.logging.*;

public class Log implements Runnable {
    private static final Logger logger = Logger.getLogger(Log.class.getName());
    private List<Reserva> reservasCanceladas;
    private List<Reserva> reservasVerificadas;
    private List<Reserva> reservasConfirmadas;
    private List<Reserva> reservasPendientes;

    private long tiempoInicio;


    public Log(List<Reserva> reservasCanceladas, List<Reserva> reservasVerificadas,
               List<Reserva> reservasPendientes, List<Reserva> reservasConfirmadas){

        this.reservasCanceladas = reservasCanceladas;
        this.reservasVerificadas = reservasVerificadas;
        this.reservasPendientes = reservasPendientes;
        this.reservasConfirmadas = reservasConfirmadas;

        try {
            FileHandler fileHandler = new FileHandler("c400.txt", true);
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
                logger.info("Tamanio de la lista de Reservas Canceladas: " + reservasCanceladas.size());
                logger.info("Tamanio de la lista de Reservas Verificadas: " + reservasVerificadas.size());
                logger.info("Tamanio de la lista de Reservas Pendientes: " + reservasPendientes.size());
                logger.info("Tamanio de la lista de Reservas Confirmadas: " + reservasConfirmadas.size());
                logger.info("-----------------------ENDED LOG ITERATION-----------------------------------------");

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

                logger.info("-----------------------FINAL LOG ITERATION-------------------------------------------");
                logger.info("Tamanio final de la lista de Reservas Canceladas:  " + reservasCanceladas.size());
                logger.info("Tamanio final de la lista de Reservas Verificadas: " + reservasVerificadas.size());
                logger.info("Tamanio final de la lista de Reservas Pendientes:  " + reservasPendientes.size());
                logger.info("Tamanio final de la lista de Reservas Confirmadas: " + reservasConfirmadas.size());
                logger.info("-------------------------------------------------------------------------------------");


        int capacidad = reservasCanceladas.size() + reservasVerificadas.size();
        int ocupacionTotal = reservasVerificadas.size();
        long duracion = System.currentTimeMillis() - tiempoInicio; // Calcular la duración
        logger.info("Duracion del programa: " + duracion + " ms");
    }

}