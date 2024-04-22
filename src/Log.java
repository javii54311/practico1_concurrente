import java.util.List;
import java.util.logging.*;

public class Log implements Runnable {
    private static final Logger logger = Logger.getLogger(Log.class.getName());
    private List<Reserva> listaReservasCanceladas;
    private List<Reserva> listaReservasVerificadas;
    private List<Reserva> listaReservasConfirmadas;
    private List<Reserva> listaReservasPendientes;
    private long tiempoInicio;
    private SistemaDeReserva sistemaDeReserva;

    public Log(List<Reserva> listaReservasCanceladas, List<Reserva> listaReservasVerificadas,
               List<Reserva> listaReservasConfirmadas, List<Reserva> listaReservasPendientes,
               SistemaDeReserva sistemaDeReserva){
        this.sistemaDeReserva = sistemaDeReserva;
        this.listaReservasCanceladas = listaReservasCanceladas;
        this.listaReservasVerificadas = listaReservasVerificadas;
        this.listaReservasConfirmadas = listaReservasConfirmadas;
        this.listaReservasPendientes = listaReservasPendientes;

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
            synchronized (sistemaDeReserva) {
                // logger.info("Tamaño de la lista de Reservas Pendientes: " + listaReservasPendientes.size());
                // logger.info("Tamaño de la lista de Reservas Confirmadas: " + listaReservasConfirmadas.size());
                logger.info("Tamaño de la lista de Reservas Canceladas: " + listaReservasCanceladas.size());
                logger.info("Tamaño de la lista de Reservas Verificadas: " + listaReservasVerificadas.size());
                // if(listaReservasConfirmadas.size() == 1){
                //     logger.info("El check de la reserva es: " + listaReservasConfirmadas.get(0).getCheck());
                // }

                logger.info("-----------------------ENDED LOG ITERATION-----------------------------------------");

                try {
                    sistemaDeReserva.notifyAll();
                    sistemaDeReserva.wait(1);
                } catch (Exception e) {
                    e.printStackTrace();
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
                logger.info("Tamaño final de la lista de Reservas Canceladas:  " + listaReservasCanceladas.size());
                logger.info("Tamaño final de la lista de Reservas Verificadas: " + listaReservasVerificadas.size());
            }
        }

        int capacidad = listaReservasCanceladas.size() + listaReservasVerificadas.size();
        int ocupacionTotal = listaReservasVerificadas.size();
        long duracion = System.currentTimeMillis() - tiempoInicio; // Calcular la duración
        logger.info("Ocupación total: " + ocupacionTotal + " de " + capacidad + ", Duración del programa: " + duracion + " ms");
    }
}