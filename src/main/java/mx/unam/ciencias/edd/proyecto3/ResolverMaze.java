package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Random;


public class ResolverMaze {

    private int columnas;
    private int renglones;
    private byte min = (byte)0x02;
    private byte max = (byte)0xFF;
    // Arreglo que contiene el laberinto en bytes
    private byte[] mazeByte;

    //  leer archivo  y revisar que la estructura del archivo sea valida y ademas llenamos el mazeByte.
    public void crearMaze(BufferedInputStream input){
        try{
            int i= 0;
            int j = 0;
            int b;
            while((b = input.read()) !=-1){
                if(i>5){
                    mazeByte[j]=(byte)b;
                    j++;
                }else if(i>3 && i<6){
                    if(i==4) renglones = b;
                    if(i==5)  columnas = b;
                }else{
                    switch(b){
                        case 0x4d:
                            if(i!=0)
                                System.err.println("El primer byte debe ser 0x4d");
                            
                        case 0x41:
                            if(i!=1)
                                System.err.println("El segundo byte debe ser 0x41");
                            
                        case 0x5a:
                            if(i!=2)
                                System.err.println("El tercer byte debe ser 0x5a");
                            
                        case 0x45:
                            if(i!=3)
                                System.err.println("El cuarto caracter debe ser 0x45");
                        default:
                            System.err.println("Checa que los primeros caracteres sean la palabra MAZE en hexadecimal");    
                    } 
                }
                i++;
            }
        
            if(mazeByte.length != (columnas*renglones)){
                System.err.println("Faltan elementos con base a las columnas y renglones proporcionados.");
            }else{
                // Obtener un mazeRoom  
                Maze maze = new Maze(mazeByte, columnas, renglones);
                // luego usamos un metodo que lo convierte a grafica ponderada
                // Usa  Metodo que usa dijkstra para devolver la ruta mas corta

            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }




    //  Imprimir el laberinto -> podemos usar el metodo de Maze para dibujar el laberintos en svg
    //  Imprimir camino -> podemos usar un metood de la clase Maze que dibujar la solucion  en svg dada un arreglo que contiene unicamente las casillas por las que hay que pasar
    


}
