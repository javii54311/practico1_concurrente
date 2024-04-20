// Clase Sistema
public class Sistema {
    protected static final int FILAS = 3;
    protected static final int COLUMNAS = 3;
    protected static Asiento[][] asientos;
    protected static RegistroReservas registroReservas;

    public Sistema() {
        asientos = new Asiento[Sistema.FILAS][Sistema.COLUMNAS];
        registroReservas = new RegistroReservas();

        // Inicializar los asientos del avión
        for (int i = 0; i < Sistema.FILAS; i++) {
            for (int j = 0; j < Sistema.COLUMNAS; j++) {
                asientos[i][j] = new Asiento();
            }
        }

        // Creacion de hilos
        ProcesoReserva pr1 = new ProcesoReserva(asientos, registroReservas);
        ProcesoReserva pr2 = new ProcesoReserva(asientos, registroReservas);
        ProcesoReserva pr3 = new ProcesoReserva(asientos, registroReservas);       
        ProcesoPago pp1 = new ProcesoPago(registroReservas);
        ProcesoPago pp2 = new ProcesoPago(registroReservas);
        // ProcesoCanceVali pcv1 = new ProcesoCanceVali(registroReservas);
        // ProcesoCanceVali pcv2 = new ProcesoCanceVali(registroReservas);
        // ProcesoCanceVali pcv3 = new ProcesoCanceVali(registroReservas);
        // ProcesoVeri pv1 = new ProcesoVeri(registroReservas);
        // ProcesoVeri pv2 = new ProcesoVeri(registroReservas);
        Thread t1 = new Thread(pr1, "Procesador de Reserva 1");
        Thread t2 = new Thread(pr2, "Procesador de Reserva 2");
        Thread t3 = new Thread(pr3, "Procesador de Reserva 3");
        Thread t4 = new Thread(pp1, "Procesador de Pago 1");
        Thread t5 = new Thread(pp2, "Procesador de Pago 1");
        // Thread t6 = new Thread(pcv1, "Procesador de Cancelacion/Validacion 1");
        // Thread t7 = new Thread(pcv2, "Procesador de Cancelacion/Validacion 2");
        // Thread t8 = new Thread(pcv3, "Procesador de Cancelacion/Validacion 3");
        // Thread t9 = new Thread(pv1, "Procesador de Verificacion 1");
        // Thread t10 = new Thread(pv2, "Procesador de Verificacion 1");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        // t6.start();
        // t7.start();
        // t8.start();
        // t9.start();
        // t10.start();
    }

    public Asiento[][] getAsientos() {
        return asientos;
    }

    public void mostrarAsientos() {
        for (int i = 0; i < Sistema.FILAS; i++) {
            for (int j = 0; j < Sistema.COLUMNAS; j++) {
                System.out.println(
                        "Asiento fila: " + i + " columna: " + j + " estado: " + getAsientos()[i][j].getEstado());
            }
        }
    }

    public void imprimirAsientos() {
        System.out.println("\n");
        for (int i = 0; i < Sistema.FILAS; i++) {
            for (int j = 0; j < Sistema.COLUMNAS; j++) {
                switch (asientos[i][j].getEstado()) {
                    case LIBRE:
                        System.out.print("\033[32m [L] " + "\u001B[0m"); // L para libre
                        break;
                    case OCUPADO:
                        System.out.print("\033[31m [O] " + "\u001B[0m"); // O para ocupado
                        break;
                    case DESCARTADO:
                        System.out.print("\033[33m [D] " + "\u001B[0m"); // D para descartado
                        break;
                }
            }
            System.out.println();
        }
        System.out.println("\n");
    }

}