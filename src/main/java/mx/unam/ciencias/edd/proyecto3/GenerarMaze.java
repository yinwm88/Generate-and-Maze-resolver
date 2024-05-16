package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.PrintStream;
import java.util.Random;


public class GenerarMaze {

    /* Bandera obligatoria para permitir generar un laberinto*/
    private  static boolean g = false;
    /* Bandera obligatoria para asignar la cant de columnas*/
    private static boolean w = false;
    /* Bandera obligatoria para asignar la cant de renglones*/
    private static boolean h = false;
    /* Bandera opcional para saber si se desea brindar una cierta semilla*/
    private static boolean s = false;

    /* Las columnas del laberinto*/
    private static int columnas;
    /* Los renglones del laberinto*/
    private static int semilla;
    /* Bandera obligatoria para asignar la cant de columnas*/
    private static  int renglones;


    private static  boolean checkBanderas(String[] entrada) {
        for (String b : entrada) {
            if (b.equals("-g")) g = true;
            if (b.equals("-w")) w = true;
            if (b.equals("-h")) h = true;
            if (b.equals("-s")) s = true;
        }
        return g && w && h;
    }


    public static void getValues(String[] entrada) {
        if(checkBanderas(entrada) == true){
            for (int i = 0; i < entrada.length; i++) {
                switch (entrada[i]) {
                    case "-s":
                        try {
                            semilla = Integer.parseInt(entrada[i + 1]);
                            if (semilla <= 0) {
                                throw new IllegalArgumentException("El valor de la semilla debe ser un número positivo.");
                            }
                            System.out.println(semilla);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Asegúrate de incluir un valor para la semilla.");
                        } catch (NumberFormatException e) {
                            System.out.println("El valor de la semilla debe ser un número.");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "-w":
                        try {
                            columnas = Integer.parseInt(entrada[i + 1]);
                            if (columnas <= 0) {
                                throw new IllegalArgumentException("El valor del ancho debe ser un número positivo.");
                            }
                            System.out.println(columnas);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Asegúrate de incluir un valor para el ancho.");
                        } catch (NumberFormatException e) {
                            System.out.println("El valor del ancho debe ser un número.");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "-h":
                        try {
                            renglones = Integer.parseInt(entrada[i + 1]);
                            if (renglones <= 0) {
                                throw new IllegalArgumentException("El valor de la altura debe ser un número positivo.");
                            }
                            System.out.println(renglones);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Asegúrate de incluir un valor para la altura.");
                        } catch (NumberFormatException e) {
                            System.out.println("El valor de la altura debe ser un número.");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                }
            }
            if(!s){
                Random rand = new Random();
                semilla = rand.nextInt(100);
            }
        }else{
            if(!g)throw new IllegalArgumentException("Asegurate de incluir la bandera '-g' obligatoria para generar un laberinto.");
            if(!w)throw new IllegalArgumentException("Asegurate de incluir la bandera '-w' obligatoria para indicar el numero de columnas.");
            if(!h)throw new IllegalArgumentException("Asegurate de incluir la bandera '-h' obligatoria para indicar el numero de renglones.");
        }
        
    }





}
