package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.util.Random;


public class ResolverMaze {

    private boolean hayColumnas=false;
    private boolean hayRenglones=false;
    private int columnas;
    private int renglones;
    private byte min = 0x02;
    private byte max = 0xFF;
    private Grafica graficaMaze;

    //  leer archivo  y revisar que la estructura del archivo sea valida.
    public void crearGrafica(BufferedInputStream input){

        int i=0;
        while((b=input.read()) !=-1){
            if(i>5){
                graficaMaze.agrega(b);
            }else if(i>3 && i<6){
                if(i==4 && !hayRenglones) renglones = b;
                if(i==5 && !hayColumnas)  columnas = b;
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
                        
                    case default: System.err.println("Checa que los primeros caracteres sean la palbra MAZE en hexadecimal");
                } 
            }
            i++;
        }
        
        if(graficaMaze.getElementos() != (columnas*renglones)){
            System.err.println("Faltan elementos con base a las columnas y renglones proporcionados.");
        }else{
            // Obtener una grafica ponderada con los vertices, es decir
            // Usa  Metodo que usa dijkstra para devolver la ruta mas corta

        }
    }




    //  Metodo que convierte la grafica en arreglo  de enteros
    //  Imprimir el laberinto -> podemos usar el metodo de Maze para dibujar el laberintos en svg
    //  Imprimir camino -> podemos usar un metood de la clase Maze que dibujar la solucion  en svg
    


}
