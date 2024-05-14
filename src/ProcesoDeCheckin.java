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
        System.out.println("Proceso de checkin iniciado");
        while (hayAsientosParaCheckin()){
            intentarCheckin();
        }
        System.out.println("No hay reservas confirmadas para hacer checkin, "+ Thread.currentThread().getName()+" finaliza");
    }
    public void intentarCheckin() {
        int randomIndex;
        Reserva reserva;

        // Sección crítica - Necesitamos un bloque sincronizado para acceder a la lista de reservas confirmadas y sacar una reserva de ella que exista
        // revisar esto. No se excluyen todos los asientos. Virtualmente no están haciendo nada este synchronized
        
        synchronized(reservasConfirmadas){
            if(reservasConfirmadas.size()>0){
                randomIndex = new Random().nextInt(reservasConfirmadas.size());
                reserva = reservasConfirmadas.get(randomIndex);
                if (reserva.isCheck() == true)
                {   //si ya estaba chequeada, vuelve a intentar
                    return;
                }
            }
            else{
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
                    reservasConfirmadas.wait(1);
                } catch (Exception e) {
                    
                }
                }
            synchronized(reservasCanceladas){
            reservasCanceladas.add(reserva);
            try {
                reservasCanceladas.notifyAll();
                reservasCanceladas.wait(1);
            } catch (Exception e) {
                
            }
            }

        }
        if(seCancelo||seChequeo){
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                
            }
        }
    }
    public boolean hayAsientosParaCheckin() {
        boolean flag = false;
        



        return reservasConfirmadas.size() > 0;
    }
}
