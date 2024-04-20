import java.util.ArrayList;
import java.util.List;

// Clase RegistroReservas para mantener un registro de las reservas pendientes
class RegistroReservas {
    
    private List<Reserva> reservasPendientes;
    private List<Reserva> reservasConfirmadas;
    private List<Reserva> reservasCanceladas;
    private List<Reserva> reservasVerificadas;

    public RegistroReservas() {

        this.reservasPendientes = new ArrayList<>();
        this.reservasConfirmadas = new ArrayList<>();
        this.reservasCanceladas = new ArrayList<>();
        this.reservasVerificadas = new ArrayList<>();
    }

    public void agregarReservaPendiente(Reserva reserva) { //syncronized
        synchronized (reservasPendientes) {
            System.out.printf("Reserva pendiente hecha por el hilo %s\n", Thread.currentThread().getName());
            this.reservasPendientes.add(reserva);
        }
    }

    public void agregarReservaConfirmada(Reserva reserva) { //syncronized
        synchronized (reservasConfirmadas) {
            System.out.printf("Reserva confirmada hecha por el hilo %s\n", Thread.currentThread().getName());
            this.reservasConfirmadas.add(reserva);
        }
    }

    public void agregarReservaCancelada(Reserva reserva) { //syncronized
        synchronized (reservasCanceladas) {
            System.out.printf("Reserva cancelada hecha por el hilo %s\n", Thread.currentThread().getName());
            this.reservasCanceladas.add(reserva);
        }
    }

    public void agregarReservaVerificada(Reserva reserva) { //syncronized
        synchronized (reservasVerificadas) {
            System.out.printf("Reserva verificada hecha por el hilo %s\n", Thread.currentThread().getName());
            this.reservasVerificadas.add(reserva);
        }
    }


    //Getters (No sé si deberian sincronizarse)
    public List<Reserva> getReservasPendientes() { //syncronized
        synchronized (reservasPendientes) {
            return this.reservasPendientes;
        }
    }

    public List<Reserva> getReservasConfirmadas() { //syncronized
        synchronized (reservasConfirmadas) {
            return this.reservasConfirmadas;
        }
    }

    public List<Reserva> getReservasCanceladas() { //syncronized
        synchronized (reservasCanceladas) {
            return this.reservasCanceladas;
        }
    }

    public List<Reserva> getReservasVerificadas() { //syncronized
        synchronized (reservasVerificadas) {
            return this.reservasVerificadas;
        }
    }

    public void eliminarReservaPendiente(Reserva reserva) {
        synchronized (reservasPendientes) {
            if(this.reservasPendientes != null && this.reservasPendientes.contains(reserva)){
                this.reservasPendientes.remove(reserva);
            }
            else{
                System.out.println("La reserva no existe");
            }
        }
    }

    public void eliminarReservaConfirmada(Reserva reserva) {
        if(this.reservasConfirmadas != null && this.reservasConfirmadas.contains(reserva)){
            this.reservasConfirmadas.remove(reserva);
        }
        else{
            System.out.println("La reserva no existe");
        }

    }

    public void eliminarReservaCancelada(Reserva reserva) {
        if(this.reservasCanceladas != null && this.reservasCanceladas.contains(reserva)){
            this.reservasCanceladas.remove(reserva);
        }
        else{
            System.out.println("La reserva no existe");
        }

    }

    public void eliminarReservaVerificada(Reserva reserva) {
        if(this.reservasVerificadas != null && this.reservasVerificadas.contains(reserva)){
            this.reservasVerificadas.remove(reserva);
        }
        else{
            System.out.println("La reserva no existe");
        }

    }

    void mostrarReservas() {
        mostrarReservasPendientes();
        mostrarReservasConfirmadas();
        mostrarReservasCanceladas();
        mostrarReservasVerificadas();
    }
    
    void mostrarReservasPendientes() {
        if(reservasPendientes.isEmpty()){
            System.out.println("No hay reservas pendientes\n");
        }
        else{
            System.out.println("\033[33m -------------------------" + "\u001B[0m");
            System.out.println("\033[33m    RESERVAS PENDIENTES\n" + "\u001B[0m");
            System.out.println("      " + "Fila" + " " + "Columna");
            for (Reserva reserva : reservasPendientes) {
                System.out.println("        " +reserva.getFila() + "     " + reserva.getColumna());
            }
            System.out.println("\033[33m -------------------------" + "\u001B[0m");
            System.out.println("\n");
        }
    }

    void mostrarReservasConfirmadas() {
        if(reservasConfirmadas.size() == 0){
            System.out.println("No hay reservas confirmadas\n");
        }
        else{
            System.out.println("\033[32m -------------------------" + "\u001B[0m");
            System.out.println("\033[32m   RESERVAS CONFIRMADAS\n" + "\u001B[0m");
            System.out.println("      " + "Fila" + " " + "Columna");
            for (Reserva reserva : reservasConfirmadas) {
                System.out.println("        "+reserva.getFila() + "     " + reserva.getColumna());
            }
            System.out.println("\033[32m -------------------------" + "\u001B[0m");
            System.out.println("\n");
        }
    }

    void mostrarReservasCanceladas() {
        if(reservasCanceladas.size() == 0){
            System.out.println("No hay reservas canceladas\n");
        }
        else{
            System.out.println("\033[31m -------------------------" + "\u001B[0m");
            System.out.println("\033[31m    RESERVAS CANCELADAS\n" + "\u001B[0m");
            System.out.println("      " + "Fila" + " " + "Columna");
            for (Reserva reserva : reservasCanceladas) {
                System.out.println("        " +reserva.getFila() + "     " + reserva.getColumna());
            }
            System.out.println("\033[31m -------------------------" + "\u001B[0m");
            System.out.println("\n");
        }
    }

    void mostrarReservasVerificadas() {
        if(reservasVerificadas.size() == 0){
            System.out.println("No hay reservas verificadas\n");
        }
        else{
            System.out.println("\033[34m -------------------------" + "\u001B[0m");
            System.out.println("\033[34m   RESERVAS VERIFICADAS\n" + "\u001B[0m");
            System.out.println("      " + "Fila" + " " + "Columna");
            for (Reserva reserva : reservasVerificadas) {
                System.out.println("        " +reserva.getFila() + "     " + reserva.getColumna());
            }
            System.out.println("\033[34m -------------------------" + "\u001B[0m");
            System.out.println("\n");
        }
    }

}