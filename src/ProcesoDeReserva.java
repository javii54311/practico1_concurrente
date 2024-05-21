import java.util.ArrayList;
import java.util.Random;

public class ProcesoDeReserva implements Runnable{
    private Asiento asientos[][];
    private ArrayList<Reserva> reservasPendientes;

    public ProcesoDeReserva(Asiento asientos[][], ArrayList<Reserva> reservasPendientes) {
        this.asientos = asientos;
        this.reservasPendientes = reservasPendientes;
        
    }
    public void run() {
        SistemaDeReservas.hilosReservando.incrementAndGet();
        while (hayAsientosLibres()) {
            intentarReservar();
        }
        //System.out.println("No hay asientos libres, "+ Thread.currentThread().getName()+" finaliza");
        SistemaDeReservas.hilosReservando.decrementAndGet();
    }
    public void intentarReservar() {
        Random random = new Random();
        int filaAleatoria = random.nextInt(SistemaDeReservas.FILAS);
        int columnaAleatoria = random.nextInt(SistemaDeReservas.COLUMNAS);
        boolean seHaReservado =asientos[filaAleatoria][columnaAleatoria].reservar();
        if(seHaReservado){
            synchronized (reservasPendientes){
                reservasPendientes.add(asientos[filaAleatoria][columnaAleatoria].getReserva());
                try {
                    reservasPendientes.notifyAll();
                    reservasPendientes.wait(SistemaDeReservas.waitReserva);
                } catch (Exception e) {
                    
                }
            }
            try {
                Thread.sleep(SistemaDeReservas.sleepReserva);
            } catch (Exception e) {
                
            }

        }
        else{
            //System.out.println("No se ha podido realizar la reserva en el asiento: " + filaAleatoria + " " + columnaAleatoria);
        }
        
    }
    public boolean hayAsientosLibres() {
        for (int i = 0; i < SistemaDeReservas.FILAS; i++) {
            for (int j = 0; j < SistemaDeReservas.COLUMNAS; j++) {
                if(asientos[i][j].getEstado() == EstadoAsiento.LIBRE){
                    return true;
                }
            }
        }
        return false;
    }
}
