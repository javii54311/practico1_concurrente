import java.util.ArrayList;
import java.util.Random;

public class ProcesoDeVerificacion implements Runnable{
    ArrayList<Reserva> reservasConfirmadas;
    ArrayList<Reserva> reservasVerificadas;
    public ProcesoDeVerificacion(ArrayList<Reserva> reservasConfirmadas, ArrayList<Reserva> reservasVerificadas) {
        this.reservasConfirmadas = reservasConfirmadas;
        this.reservasVerificadas = reservasVerificadas;
    }
    public void run() {
        SistemaDeReservas.hilosVerificando.incrementAndGet();
        System.out.println("Proceso de verificación iniciado");
        while (hayAsientosParaVerificar()) {
            intentarVerificar();
        }
        System.out.println("No hay reservas confirmadas para verificar, "+ Thread.currentThread().getName()+" finaliza");
        SistemaDeReservas.hilosVerificando.decrementAndGet();
    }
    public void intentarVerificar() {
        int randomIndex;
        Reserva reserva;
        //sincroniza para que randomIndex siempre sea un índice válido
        synchronized(reservasConfirmadas){
            if(reservasConfirmadas.size()>0){
                randomIndex = new Random().nextInt(reservasConfirmadas.size());
                reserva = reservasConfirmadas.get(randomIndex);
                try {
                    reservasConfirmadas.notifyAll();
                    reservasConfirmadas.wait(SistemaDeReservas.waitVerificacion);
                } catch (Exception e) {
                    
                }
            }
            else{
                try {
                    reservasConfirmadas.notifyAll();
                    reservasConfirmadas.wait(SistemaDeReservas.waitVerificacion);
                } catch (Exception e) {
                    
                }
                return;
            }

        }

        if(reserva.isCheck() == false)
        {   //si aun no está chequeada no se puede verificar
            return;
        }

        boolean seVerifico = reserva.getAsiento().verificar();
        
        if(seVerifico){
            synchronized(reservasConfirmadas){
                reservasConfirmadas.remove(reserva);
                try {
                    reservasConfirmadas.notifyAll();
                    reservasConfirmadas.wait(SistemaDeReservas.waitVerificacion);
                } catch (Exception e) {
                    
                }
            }
            synchronized(reservasVerificadas){
                reservasVerificadas.add(reserva);
                try {
                    reservasVerificadas.notifyAll();
                    reservasVerificadas.wait(SistemaDeReservas.waitVerificacion);
                } catch (Exception e) {
                    
                }
            }
            try {
                Thread.sleep(SistemaDeReservas.sleepVerificacion);
            } catch (Exception e) {
                
            }
        }
        else{
            //System.out.println("No se ha podido verificar la reserva: " + reserva.toString());
        }
    }
    public boolean hayAsientosParaVerificar() {
        if(SistemaDeReservas.hilosCheckin.get() == 0 && reservasConfirmadas.size() == 0){
            return false;
        }
        return true;
    }
}
