import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        int filas = 10;
        int columnas = 6;
        Avion av = new Avion(filas, columnas);
        
        av.menu(1);

    }
}