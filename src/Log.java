import java.util.List;
import java.util.logging.*;

public class Log implements Runnable {
    private static final Logger logger = Logger.getLogger(Log.class.getName());
    private List<Reserva> listaReservasCanceladas;
    private List<Reserva> listaReservasVerificadas;
    private long tiempoInicio;

    public Log(List<Reserva> listaReservasCanceladas, List<Reserva> listaReservasVerificadas) {
        this.listaReservasCanceladas = listaReservasCanceladas;
        this.listaReservasVerificadas = listaReservasVerificadas;
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
        while (SistemaDeReserva.sigueProcesoDeVerificacion) {
            synchronized (listaReservasCanceladas) {
                synchronized (listaReservasVerificadas) {
                    logger.info("Tamaño de la listaReservas Canceladas: " + listaReservasCanceladas.size());
                    logger.info("Tamaño de la listaReservas Verificadas: " + listaReservasVerificadas.size());
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    synchronized (listaReservasCanceladas) {
        synchronized (listaReservasVerificadas) {
            logger.info("---------------------------------------------------------------------------");
            logger.info("Tamaño de la listaReservas Canceladas final: " + listaReservasCanceladas.size());
            logger.info("Tamaño de la listaReservas Verificadas final: " + listaReservasVerificadas.size());
        }
    }

        int capacidad = listaReservasCanceladas.size() + listaReservasVerificadas.size();
        int ocupacionTotal =  listaReservasVerificadas.size();
        long duracion = System.currentTimeMillis() - tiempoInicio; // Calcular la duración
        logger.info("Ocupación total: " + ocupacionTotal+ " de " + capacidad + ", Duración del programa: " + duracion + " ms");
    }
}