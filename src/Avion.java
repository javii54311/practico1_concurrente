import java.util.Random;
import java.util.Scanner;

// Clase Avion
public class Avion {
    private final int filas;
    private final int columnas;
    private Asiento[][] asientos;
    private RegistroReservas registroReservas;

    public Avion(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.asientos = new Asiento[filas][columnas];
        this.registroReservas = new RegistroReservas();

        // Inicializar los asientos del avión
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                asientos[i][j] = new Asiento();
            }
        }
    }

    public void menu(int eleccion) {
        Scanner scanner = new Scanner(System.in);

        while (eleccion != 0) {
            System.out.println("\033[36m ---------- Menú de opciones ----------"+ "\u001B[0m");
            System.out.println("1. Mostrar disponibilidad de asientos");
            System.out.println("2. Imprimir disponibilidad de asientos");
            System.out.println("3. Reservar un asiento aleatorio");
            System.out.println("4. Mostrar reservas");
            System.out.println("5. Procesar reservas pendientes");
            System.out.println("6. Procesar reservas confirmadas");
            System.out.println("7. Verificar reservas confirmadas");
            System.out.println("0. Salir del programa");
            System.out.println("Ingrese su elección: ");
            System.out.println("\033[36m --------------------------------------" + "\u001B[0m");
            eleccion = scanner.nextInt();

            switch (eleccion) {
                case 1:
                    mostrarAsientos();
                    break;
                case 2:
                    imprimirAsientos();
                    break;
                case 3:
                    reservarAsientoAleatorio();
                    break;
                case 4:
                    registroReservas.mostrarReservas();
                    break;
                case 5:
                    procesarReservasPendientes();
                    break;
                case 6:
                    ProcesarReservaConfirmada();
                    break;
                case 7:
                    Verificar();
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }
        scanner.close();
    }

    public Asiento[][] getAsientos() {
        return asientos;
    }

    public void mostrarAsientos() {
        for(int i=0; i<filas; i++){
            for(int j=0; j<columnas; j++){
                System.out.println("Asiento fila: " + i + " columna: " + j + " estado: " + getAsientos()[i][j].getEstado());
            }
        }
    }

    public void imprimirAsientos() {
        System.out.println("\n");
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                switch (asientos[i][j].getEstado()) {
                    case LIBRE:
                        System.out.print("\033[32m [L] " + "\u001B[0m");  // L para libre
                        break;
                    case OCUPADO:
                        System.out.print("\033[31m [O] " + "\u001B[0m");  // O para ocupado
                        break;
                    case DESCARTADO:
                        System.out.print("\033[33m [D] " + "\u001B[0m");  // D para descartado
                        break;
                }
            }
            System.out.println();
        }
        System.out.println("\n");
    }
    // Método para intentar reservar un asiento aleatorio
    public void reservarAsientoAleatorio() {

        Random random = new Random();
        Reserva reserva = new Reserva();

        int filaAleatoria, columnaAleatoria;
        boolean asientoEncontrado = false;

        // Intentar reservar un asiento aleatorio hasta que se encuentre uno libre
        do {
            filaAleatoria = random.nextInt(filas);
            columnaAleatoria = random.nextInt(columnas);
            Asiento asiento = asientos[filaAleatoria][columnaAleatoria];
            if (asiento.getEstado() == EstadoAsiento.LIBRE) {
                asiento.setEstado(EstadoAsiento.OCUPADO);
                reserva.setEstado(EstadoReserva.PENDIENTE_DE_PAGO);
                reserva.setFila(filaAleatoria);
                reserva.setColumna(columnaAleatoria);
                reserva.setAsiento(asiento);
                registroReservas.agregarReservaPendiente(reserva);
                asientoEncontrado = true;
                System.out.printf("Asiento reservado. Fila: %d, Columna: %d\n", filaAleatoria, columnaAleatoria);
            }
        } while (!asientoEncontrado);



    }

    public void procesarReservasPendientes() {
        Random random = new Random();

        // Obtener una reserva aleatoria de la lista de reservas pendientes
        if (!registroReservas.getReservasPendientes().isEmpty()) {
            int indiceAleatorio = random.nextInt(registroReservas.getReservasPendientes().size());
            Reserva reserva = registroReservas.getReservasPendientes().get(indiceAleatorio);

            // Intentar pagar la reserva
            if (random.nextDouble() < 0.9) { // 90% de probabilidad de éxito
                System.out.println("Reserva " + reserva.getFila() + " " + reserva.getColumna() + " pagada");
                // Eliminar la reserva de la lista de pendientes
                registroReservas.eliminarReservaPendiente(reserva);
                // Agregar la reserva a la lista de confirmadas
                registroReservas.agregarReservaConfirmada(reserva);
                reserva.setEstado(EstadoReserva.CONFIRMADA);
            }
            else {
                // 10% de probabilidad de fracaso
                System.out.println("Reserva " + reserva.getFila() + " " + reserva.getColumna() +" no pagada");
                // Colocar el asiento en estado DESCARTADO
                reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                // Eliminar la reserva de la lista de pendientes
                registroReservas.eliminarReservaPendiente(reserva);
                // Agregar la reserva a la lista de canceladas
                registroReservas.agregarReservaCancelada(reserva);
                reserva.setEstado(EstadoReserva.CANCELADA);
            }
        }
        else{
            System.out.println("No hay reservas pendientes");
        }
    }
    public void ProcesarReservaConfirmada() {
        Random random = new Random();

        // Verificar si hay reservas confirmadas
        if (!registroReservas.getReservasConfirmadas().isEmpty()) {
            // Seleccionar una reserva aleatoria de la lista de confirmadas
            int indiceAleatorio = random.nextInt(registroReservas.getReservasConfirmadas().size());
            Reserva reserva = registroReservas.getReservasConfirmadas().get(indiceAleatorio);

            if(reserva.getCheck()){
                System.out.println("La reserva " + reserva.getFila()+ " " + reserva.getColumna()+  " estaba checkeada");
            }
            else{
                // Intentar cancelar la reserva con un 10% de probabilidad
                if (random.nextDouble() < 0.1) {
                    // Marcar el asiento asociado como DESCARTADO
                    System.out.println("Reserva " + reserva.getFila() + " "+ reserva.getColumna() + " cancelada");
                    reserva.getAsiento().setEstado(EstadoAsiento.DESCARTADO);
                    // Eliminar la reserva de la lista de confirmadas
                    registroReservas.eliminarReservaConfirmada(reserva);
                    // Agregar la reserva a la lista de canceladas
                    registroReservas.agregarReservaCancelada(reserva);
                }
                else{
                    System.out.println("Reserva "+reserva.getFila() + " " + reserva.getColumna()+" no cancelada");
                    System.out.println("Se la marca como chequeada");
                    reserva.setCheck(true);
                }
            }

        }
        else{
            System.out.println("No hay reservas confirmadas");
        }
    }

    public void Verificar(){
        // Verificar si hay reservas confirmadas
        if(!registroReservas.getReservasConfirmadas().isEmpty()){
            // Seleccionar una reserva aleatoria de la lista de confirmadas
            Random random = new Random();
            int indiceAleatorio = random.nextInt(registroReservas.getReservasConfirmadas().size());
            Reserva reserva = registroReservas.getReservasConfirmadas().get(indiceAleatorio);

            // Verificar si esta checkeada
            if(reserva.getCheck()){
                System.out.println("La reserva " + reserva.getFila()+ " " + reserva.getColumna()+  " estaba checkeada");
                System.out.println("Se la marca como verificada.");
                registroReservas.eliminarReservaConfirmada(reserva);
                registroReservas.agregarReservaVerificada(reserva);
                reserva.setEstado(EstadoReserva.VERIFICADA);
            }
            else{
                // La reserva no estaba checkeada
                System.out.println("La reserva " + reserva.getFila()+ " " + reserva.getColumna()+  " no estaba checkeada");
            }
        }
        else{
            System.out.println("No hay reservas confirmadas");
        }
    }
}