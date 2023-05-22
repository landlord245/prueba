import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
/**
 * Mapa
 */
public class Mapa {
    private final String CELDA;
    private final String BANDERA;
    private final String BANDERA_ERRONEA;
    private final String BANDERA_CORRECTA;
    private final String VACIO;
    private final String MINAS;
    private final int PROXIMIDAD_MINA[];
    private final String[][] posicionesMinas;
    private boolean ejecucionJuego;
    private int contadorAcceso;
    private int tamañoCelda;
    private String[][] mapa;

    // El constructor genera una tabla segun celdas indicadas.
    public Mapa(int celdas) {
        this.CELDA   = " ䷀ ";
        this.BANDERA = " ⫸ ";
        this.BANDERA_ERRONEA = " ⨷ ";
        this.BANDERA_CORRECTA = " ☑ ";
        this.VACIO   = " ⿲";
        this.MINAS   = " ⦿ ";
        this.contadorAcceso  = 0;
        this.tamañoCelda     = celdas;
        this.ejecucionJuego = true;
        this.posicionesMinas = new String[this.tamañoCelda][this.tamañoCelda];
        this.PROXIMIDAD_MINA = new int[6];
        for (int i = 0; i < PROXIMIDAD_MINA.length; i++) {
            this.PROXIMIDAD_MINA[i] = (i+1);
        }
        this.mapa = new String[celdas][celdas];
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa.length; j++) {
                mapa[i][j] = CELDA;
            }
        }
    }

    // Esta funcion muestra la tabla
    public void MostrarMapa() {
        String celdas = "";
        for (int i = 0; i < mapa.length; i++) {
            celdas += mapa[0][i];
        }

        System.out.print("X:  ");
        for (int i = 0; i < mapa.length; i++) {
            System.out.print(i+"  ");
        }
        System.out.println();
        for (int i = 0; i < mapa.length; i++) {
            System.out.print(i+": ");
            for (int j = 0; j < mapa.length; j++) {
                System.out.print(mapa[i][j]);
            }
            System.out.println();
        }
        System.out.println("Y ⬆");
    }

    public String getCELDA() {
        return CELDA;
    }

    public String getBANDERA() {
        return BANDERA;
    }

    public String getMINAS() {
        return MINAS;
    }

    public String getVACIO() {
        return VACIO;
    }

    public boolean isEjecucionJuego() {
        return ejecucionJuego;
    }

    public void setEjecucionJuego(boolean ejecucionJuego) {
        this.ejecucionJuego = ejecucionJuego;
    }

    public void GenerarMinas(int[] PrimeraRevelacionBloque) {
        Random random = new Random();
        int cantidadMinas = ((this.tamañoCelda*2)*75)/100;
        int x, y, contadorMinas;
        contadorMinas = 0;
        do {
            for (int i = 0; i < this.mapa.length; i++) {
                do {
                    x = random.nextInt(this.mapa.length);
                    y = random.nextInt(this.mapa.length);
                } while (x == PrimeraRevelacionBloque[0] && y == PrimeraRevelacionBloque[1]);
                this.posicionesMinas[x][y] = this.MINAS;
                // System.out.println("("+x+","+y+")");
            }
        } while (contadorMinas == (cantidadMinas+1));
    }

    public void PosicionDeLasMinas() {
        for (int i = 0; i < posicionesMinas.length; i++) {
            for (int j = 0; j < posicionesMinas.length; j++) {
                try {
                    if (posicionesMinas[i][j].equalsIgnoreCase(this.MINAS)) {
                        System.out.print(" ("+i+","+j+"),");
                    }
                } catch (NullPointerException e) {}
            }
        }
        System.out.println("\n");
    }

    public void AddBandera(int X, int Y) {
        boolean validarCelda = ValidarCelda(X, Y);
        if (validarCelda) {
            this.mapa[X][Y] = this.BANDERA;
        }
    }

    public void RevealBLock(int X, int Y) {
        boolean validarCelda = ValidarCelda(X, Y);
        if (validarCelda) {
            System.out.println("\nPosicion revelada: (" + X + "," + Y + ")\n");

            if (ExcepcionesArrayPosicionMinas(X, Y).equalsIgnoreCase(this.MINAS)) {
                GameOver();
            } else {
                this.mapa[Y][X] = this.VACIO;
                contadorAcceso++;
            }

            if (contadorAcceso == 1) {
                int[] primeraRevelacionBloque = { X, Y };
                GenerarMinas(primeraRevelacionBloque);
            }
        }
    }

    public void GameOver() {
        System.out.println(
            "\t⨳ GAME OVER ⨳"
            + "\n•Bloques Revelados: "+this.contadorAcceso
        );
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa.length; j++) {
                if (ExcepcionesArrayPosicionMinas(i, j).equalsIgnoreCase(this.MINAS)) { // Si hay una mina.
                    if (this.mapa[i][j].equals(this.BANDERA)) {// Comprueba si hay una bandera encima.
                        this.mapa[i][j] = this.BANDERA_CORRECTA;
                    }else {// si no la hay.
                        this.mapa[i][j] = this.MINAS;
                    }
                }else if(this.mapa[i][j].equals(BANDERA)){// si no hay una mina pero si una bandera.
                    this.mapa[i][j] = this.BANDERA_ERRONEA;

                }else{// si ninguna de atras es correcta.
                    this.mapa[i][j] = this.CELDA;
                }
            }
        }
        System.out.println("•Mapa:");
        MostrarMapa();
        ejecucionJuego = false;
    }

    // En esta funcion se comprobara que es lo que desea realizar el usuario
    // Tenemos que verficar que el numero de insertado de las posiciones se validen, esto se puede hacer aqui o fuerta.
    public void RealizarAccion(char BanderaB_RevelarR) {
        if (BanderaB_RevelarR == 'B') {
            System.out.println("Inserte el valor de la X:");
            int X = ExcepcionesInt();
            System.out.println("Inserte el valor de la Y:");
            int Y = ExcepcionesInt();
            AddBandera(X, Y);
        }else if (BanderaB_RevelarR == 'R') {
            System.out.println("Inserte el valor de la X:");
            int X = ExcepcionesInt();
            System.out.println("Inserte el valor de la Y:");
            int Y = ExcepcionesInt();
            RevealBLock(X, Y);
        }else {
            System.out.println("Opcion Añadida incorrecta: \nB) Poner una Bandera.\nR) Revelar un bloque.");
        }
    }

    // Aqui se comprobara que el aceso a las cordenadas (X,Y) es valido o no.
    public boolean ValidarCelda(int X, int Y) {
        if (X < this.mapa.length && Y < this.mapa.length) {
            return true;
        }else {
            System.out.println("La posicion expresada"+"("+X+","+Y+")"+" no existe en la tabla.");
            return false;
        }

    }

    // Excepciones para INTEGER
    public int ExcepcionesInt() {
        Scanner sc = new Scanner(System.in);
        int input = 0;
        boolean finBucle = true;
        do {
            System.out.print("> ");
            try {
                finBucle = true;
                sc.next();
                input = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Tipo de dato insertado incorrecto, solo se aceptan numero enteros:\n" + e);
                finBucle = false;
            }
        } while (!finBucle);
        return input;
    }

    // Excepcion para las Arrays vacias, principalmente las que devuelven null.
    public String ExcepcionesArrayPosicionMinas(int X, int Y) {
        String input;
        try {
            input = this.posicionesMinas[X][Y];
        } catch (NullPointerException e) {
            input = "/";
        }
        if (input == null) {
            input = "/";
        }
        return input;
    }
}