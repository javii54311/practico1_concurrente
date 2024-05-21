import java.util.ArrayList;
import java.util.Random;
public class ProcesoDeCheckin implements Runnable{

    private ArrayList<Reserva> reservasConfirmadas;
    private ArrayList<Reserva> reservasCanceladas;

    public ProcesoDeCheckin( ArrayList<Reserva> reservasConfirmadas, ArrayList<Reserva> reservasCanceladas) {
        this.reservasConfirmadas = reservasConfirmadas;
        this.reservasCanceladas = reservasCanceladas;
    }
    public void run() {
        SistemaDeReservas.hilosCheckin.incrementAndGet();
        //System.out.println("Proceso de checkin iniciado");
        while (hayAsientosParaCheckin()){
            intentarCheckin();
        }
        //System.out.println("No hay reservas confirmadas para hacer checkin, "+ Thread.currentThread().getName()+" finaliza");
        SistemaDeReservas.hilosCheckin.decrementAndGet();
    }
    public void intentarCheckin() {
        int randomIndex;
        Reserva reserva;

        // Sección crítica - Necesitamos un bloque sincronizado para acceder a la lista de reservas confirmadas y sacar una reserva de ella que exista
        // revisar esto. No se excluyen todos los asientos. Virtualmente no están haciendo nada este synchronized
        
        //sincroniza para que randomIndex siempre sea un índice válido
        synchronized(reservasConfirmadas){
            if(reservasConfirmadas.size()>0){
                randomIndex = new Random().nextInt(reservasConfirmadas.size());
                reserva = reservasConfirmadas.get(randomIndex);
                if (reserva.isCheck() == true)
                {   //si ya estaba chequeada, vuelve a intentar
                    try {
                        reservasConfirmadas.notifyAll();
                        reservasConfirmadas.wait(SistemaDeReservas.waitCheckin);
                    } catch (Exception e) {
                    }
                    return;
                }
            }
            else{
                try {
                    reservasConfirmadas.notifyAll();
                    reservasConfirmadas.wait(SistemaDeReservas.waitCheckin);
                } catch (Exception e) {
                }
                return;
            }
        }
        boolean seCancelo = false;
        boolean seChequeo = false;
        Random random = new Random();
        if(random.nextDouble() < 0.9){
            seChequeo = reserva.getAsiento().checkin();
        }
        else{
            seCancelo = reserva.getAsiento().failedCheckin();
        }
        if(seCancelo){
            synchronized(reservasConfirmadas){
                reservasConfirmadas.remove(reserva);
                try {
                    reservasConfirmadas.notifyAll();
                    reservasConfirmadas.wait(SistemaDeReservas.waitCheckin);
                } catch (Exception e) {
                    
                }
                }
            synchronized(reservasCanceladas){
            reservasCanceladas.add(reserva);
            try {
                reservasCanceladas.notifyAll();
                reservasCanceladas.wait(SistemaDeReservas.waitCheckin);
            } catch (Exception e) {
                
            }
            }

        }
        if(seCancelo||seChequeo){
            try {
                Thread.sleep(SistemaDeReservas.sleepCheckin);
            } catch (Exception e) {
                
            }
        }
    }
    
    public boolean hayAsientosParaCheckin() {
        if(reservasConfirmadas.size()==0 && SistemaDeReservas.hilosPagando.get()==0){
            return false;
        }
        return true;
    }
}
