import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Mapa mapa = new Mapa(10);
        while (mapa.isEjecucionJuego()) {
            mapa.MostrarMapa();
            System.out.println("\t~ CONTROLES ~\n B) Poner una Bandera.\n R) Revelar un bloque.");
            char letraIn = ExcepcionesChar();
            mapa.RealizarAccion(Character.toUpperCase(letraIn));
            // System.out.println("-");
            // mapa.PosicionDeLasMinas();
        }
    }

    public static char ExcepcionesChar() {
        Scanner sc = new Scanner(System.in);
        boolean finBucle;
        char input = 'd';
        do {
            System.out.print("> ");
            try {
                finBucle = true;
                input = sc.next().charAt(0);
            } catch (Exception e) {
                System.out.println("Tipo de input invalido, solo se haceptan letras:\n"+e);
                finBucle = false;
            }
        } while (!finBucle);
        return input;
    }
}