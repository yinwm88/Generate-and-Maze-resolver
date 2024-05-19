package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedInputStream;

/**
 * Clase para genrar y resolver laberintos
 * @author Wong Mestas
 */

public class Proyecto3 {
    public static void main(String[] args){
        /**
         * 
        if (args.length == 0) {
            // leemos el archivo .mze para resolver el laberinto
            BufferedInputStream in = new BufferedInputStream(System.in);
            GenerarMaze laberinto = new GenerarMaze(in);
            
        }else{
            // sino , entonces generamos el laberinto e dentificamos banderas si es que hay
            // obtenemos las banderas de la entrada estandar.
            // pasamos los parametros corresponidentes a ResolverMaze




        }
         */
        if(GenerarMaze.getValues(args))GenerarMaze.generarMze();
    }

}
