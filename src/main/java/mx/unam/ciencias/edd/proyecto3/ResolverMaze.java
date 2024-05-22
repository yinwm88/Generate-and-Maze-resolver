package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.BufferedInputStream;
import java.io.IOException;


public class ResolverMaze {

    private int columnas;
    private int renglones;
    private byte min = (byte)0x02;
    private byte max = (byte)0xFF;
    // Arreglo que contiene el laberinto en bytes
    private byte[] mazeByte;

    /** 
     * Metodo para leer archivo mze y revisar que la estructura 
     * del archivo sea valida e inicializamos el mazeByte 
    */
    public void leerMze(BufferedInputStream input){
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
            if(mazeByte.length != (columnas*renglones))
                System.err.println("Faltan elementos con base a las columnas y renglones proporcionados.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    /**
     * Metodo que resuleve el maze e imprime la solucion en svg
    */
    public void resolverMaze(){ 
        Maze maze = new Maze(mazeByte, columnas, renglones);
        //  Metodo que lo convierte a grafica ponderada
        //  Metodo que usa dijkstra para devolver la ruta mas corta
        //  Metodo que regresa el laberinto como SVG
        //  Metodo que regresa el svg de la ruta encontrada
    }

}
