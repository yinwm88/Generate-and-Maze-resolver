package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.PrintStream;
import java.util.Random;

/**
 * Clase para generar un archivo que contiene un laberinto valido.
 */
public class GenerarMaze {

    /**Bandera permite generar un laberinto*/
    private static boolean g = false;
    /**Bandera permite saber las columnas*/
    private static boolean w = false;
    /**Bandera permite saber los renglones*/
    private static boolean h = false;
    /**Bandera opcional permite bindar una semilla*/
    private static boolean s = false;

    /**Numero de columnas*/
    private static int columnas;
    /**Numero de renglones*/
    private static int renglones;
    /**Valor de la semilla*/
    private static int semilla;

    /**Metood para verificar la existencia de banderas*/
    private static boolean checkBanderas(String[] entrada) {
        for (String b : entrada) {
            if (b.equals("-g")) g = true;
            if (b.equals("-w")) w = true;
            if (b.equals("-h")) h = true;
            if (b.equals("-s")) s = true;
        }
        return g && w && h;
    }

    /**Imprime el uso correcto del programa*/
    private static void printUsage() {
        System.err.println("\nAsegúrate de incluir las siguientes banderas:");
        System.err.println(" '-g' obligatoria para generar un laberinto.");
        System.err.println(" '-w' obligatoria para indicar el número de columnas del laberinto.");
        System.err.println(" '-h' obligatoria para indicar el número de renglones del laberinto.");
        System.err.println(" '-s' opcional si deseas agregar una semilla para generar el laberinto.\n");
    }

    /**Metodo para obtener los valores del laberinto*/
    public static boolean getValues(String[] entrada) {
        boolean esValido=true;
        if (checkBanderas(entrada)) {
            for (int i = 0; i < entrada.length; i++) {
                switch (entrada[i]) {
                    case "-s":
                        if (i + 1 < entrada.length && !entrada[i + 1].startsWith("-")) {
                            try {
                                semilla = Integer.parseInt(entrada[i + 1]);
                                if (semilla <= 0) {
                                    esValido &=false;
                                    System.err.println("El valor de la semilla debe ser un número positivo.");
                                }
                            } catch (NumberFormatException e) {
                                esValido &=false;
                                System.err.println("El valor de la semilla debe ser un número.");
                            }
                            i++;
                        } else {
                            esValido &=false;
                            System.err.println("Asegúrate de incluir un valor para la semilla.");
                        }
                        break;
                    case "-w":
                        if (i + 1 < entrada.length && !entrada[i + 1].startsWith("-")) {
                            try {
                                columnas = Integer.parseInt(entrada[i + 1]);
                                if (columnas < 2 || columnas > 255) {
                                    esValido &=false;
                                    System.err.println("El valor de las columnas debe variar entre 2 y 255.");
                                }
                            } catch (NumberFormatException e) {
                                esValido &=false;
                                System.err.println("El valor de las columnas debe ser un número.");
                            }
                            i++;
                        } else {
                            esValido &=false;
                            System.err.println("Asegúrate de incluir un valor para las columnas.");
                        }
                        break;
                    case "-h":
                        if (i + 1 < entrada.length && !entrada[i + 1].startsWith("-")) {
                            try {
                                renglones = Integer.parseInt(entrada[i + 1]);
                                if (renglones < 2 || renglones > 255) {
                                    esValido &=false;
                                    System.err.println("El valor de los renglones debe variar entre 2 y 255.");
                                }
                            } catch (NumberFormatException e) {
                                esValido &=false;
                                System.err.println("El valor de los renglones debe ser un número.");
                            }
                            i++;
                        } else {
                            esValido &=false;
                            System.err.println("Asegúrate de incluir un valor para los renglones.");
                        }
                        break;
                }
            }
        } else {
            esValido&=false;
            printUsage();
        }
        return esValido;
    }

    /**Metodo para generar el archivo mze que contiene un laberinto valido*/
    public static void generarMze() {
        Random random = s ? new Random(semilla) : new Random();
        Maze maze = new Maze(columnas, renglones, random);
        byte[] mze = new byte[6 + columnas * renglones];
        byte[] mazeByte = maze.getMazeByte();
        int i = 0;
        //agregamos los bytes indispensables M A Z E
        mze[i] = (byte)(0x4d);
        mze[++i] = (byte)(0x41);
        mze[++i] = (byte)(0x5a);
        mze[++i] = (byte)(0x45);

        //agregamos las columnas y renglones en byte 
        mze[++i] = (byte)(columnas & 0xFF);
        mze[++i] = (byte)(renglones & 0xFF);
        
        for(byte h : mazeByte){
            mze[++i] = h;
        }

        try{
            PrintStream out=new PrintStream(System.out);
            for (byte b : mze) {
                out.write(b);
            }
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


        //imprimimos contenido para el archivo mze (deberan ser columnas x renglones bytes en total)
        
    }
}
