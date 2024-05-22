package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedInputStream;

/**
 * Clase para genrar y resolver laberintos
 * @author Wong Mestas
 */

public class Proyecto3 {
    public static void main(String[] args){
        /**  
        if (args.length == 0) {
            ResolverMaze.leerMze();
            ResolverMaze.ResolverMaze();
        }else{
            if(GenerarMaze.getValues(args))GenerarMaze.generarMze();
        }
        */
        BufferedInputStream in = new BufferedInputStream(System.in);
        ResolverMaze.leerMze(in);
        ResolverMaze.resolverMaze();
    }

}
