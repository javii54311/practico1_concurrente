import java.util.List;
import java.util.Random;
public class ProcesoDeVerificacion implements Runnable {
    int sleep_verification;
    List<Reserva> reservasConfirmadas;
    List<Reserva> reservasVerificadas;
   public ProcesoDeVerificacion(List<Reserva>reservasConfirmadas, List<Reserva> reservasVerificadas, int sleep_verification){
         this.reservasConfirmadas=reservasConfirmadas;
         this.reservasVerificadas=reservasVerificadas;
         this.sleep_verification=sleep_verification;
   }
   public void run(){

   }
   
}
