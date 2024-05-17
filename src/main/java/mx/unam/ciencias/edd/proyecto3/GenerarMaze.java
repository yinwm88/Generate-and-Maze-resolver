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
        return (g && w )&& h;
    }


    public static void getValues(String[] entrada) {
        if(checkBanderas(entrada) == true){
            for (int i = 0; i < entrada.length; i++) {
                switch (entrada[i]) {
                    case "-s":
                        try {
                            if((!entrada[i + 1].equals("-w")) && (!entrada[i + 1].equals("-g")) && (!entrada[i + 1].equals("-h"))){
                                semilla = Integer.parseInt(entrada[i + 1]);
                                if (semilla <= 0) {
                                    System.out.println("El valor de la semilla debe ser un número positivo.");
                                }
                            }else{
                                System.out.println("Asegúrate de incluir un valor para la semilla.");

                            }
                            //System.out.println(semilla);
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
                            if((!entrada[i + 1].equals("-s")) && (!entrada[i + 1].equals("-g")) && (!entrada[i + 1].equals("-h"))){
                                columnas = Integer.parseInt(entrada[i + 1]);
                                if (columnas <= 0) {
                                    System.out.println("El valor del ancho debe ser un número positivo.");
                                }
                            }else{
                                System.out.println("Asegúrate de incluir un valor para las columnas.");

                            }
                            //System.out.println(columnas);
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
                            if((!entrada[i + 1].equals("-w")) && (!entrada[i + 1].equals("-g")) && (!entrada[i + 1].equals("-s"))){
                                renglones = Integer.parseInt(entrada[i + 1]);
                                if (renglones <= 0) {
                                    System.out.println("El valor de la altura debe ser un número positivo.");
                                } 
                            }else{
                                System.out.println("Asegúrate de incluir un valor para los renglones.");

                            }
                            //System.out.println(renglones);
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
            System.out.println(semilla);
        }else{
            System.out.println("\nAsegurate de incluir las siguientes banderas:\n '-g' obligatoria para generar un laberinto.\n '-h' obligatoria para indicar el numero de renglones del laberinto.\n '-w' obligatoria para indicar el numero de columnas del laberinto.\n '-s' OPCIONAL por si deseas agregar una semilla para generar el laberinto.\n");
        }

        
    }





}
