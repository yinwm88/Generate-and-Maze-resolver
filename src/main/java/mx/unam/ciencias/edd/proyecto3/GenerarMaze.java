package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.util.Random;

public class GenerarMaze {

    // Banderas
    private static boolean g = false;
    private static boolean w = false;
    private static boolean h = false;
    private static boolean s = false;

    // Valores del laberinto
    private static int columnas;
    private static int renglones;
    private static int semilla;

    // Verifica las banderas
    private static boolean checkBanderas(String[] entrada) {
        for (String b : entrada) {
            if (b.equals("-g")) g = true;
            if (b.equals("-w")) w = true;
            if (b.equals("-h")) h = true;
            if (b.equals("-s")) s = true;
        }
        return g && w && h;
    }

    // Obtiene los valores de los argumentos
    public static void getValues(String[] entrada) {
        if (checkBanderas(entrada)) {
            for (int i = 0; i < entrada.length; i++) {
                switch (entrada[i]) {
                    case "-s":
                        if (i + 1 < entrada.length && !entrada[i + 1].startsWith("-")) {
                            try {
                                semilla = Integer.parseInt(entrada[i + 1]);
                                if (semilla <= 0) {
                                    System.out.println("El valor de la semilla debe ser un número positivo.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("El valor de la semilla debe ser un número.");
                            }
                            i++;
                        } else {
                            System.out.println("Asegúrate de incluir un valor para la semilla.");
                        }
                        break;
                    case "-w":
                        if (i + 1 < entrada.length && !entrada[i + 1].startsWith("-")) {
                            try {
                                columnas = Integer.parseInt(entrada[i + 1]);
                                if (columnas <= 0) {
                                    System.out.println("El valor de las columnas debe ser un número positivo.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("El valor de las columnas debe ser un número.");
                            }
                            i++;
                        } else {
                            System.out.println("Asegúrate de incluir un valor para las columnas.");
                        }
                        break;
                    case "-h":
                        if (i + 1 < entrada.length && !entrada[i + 1].startsWith("-")) {
                            try {
                                renglones = Integer.parseInt(entrada[i + 1]);
                                if (renglones <= 0) {
                                    System.out.println("El valor de los renglonesdebe ser un número positivo.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("El valor de los renglones debe ser un número.");
                            }
                            i++;
                        } else {
                            System.out.println("Asegúrate de incluir un valor para los renglones.");
                        }
                        break;
                }
            }
        } else {
            printUsage();
        }
    }

    /**
     * Imprime el uso correcto del programa
    */
    private static void printUsage() {
        System.out.println("\nAsegúrate de incluir las siguientes banderas:");
        System.out.println(" '-g' obligatoria para generar un laberinto.");
        System.out.println(" '-w' obligatoria para indicar el número de columnas del laberinto.");
        System.out.println(" '-h' obligatoria para indicar el número de renglones del laberinto.");
        System.out.println(" '-s' opcional si deseas agregar una semilla para generar el laberinto.\n");
    }

    // Genera el laberinto
    public static void generarMaze() {
        Random random = s ? new Random(semilla) : new Random();
        //Maze maze = new Maze(columnas, renglones, random);
        // Aquí podrías tener el código para generar y mostrar el laberinto
    }

}
