package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedInputStream;

/**
 * Clase para genrar y resolver laberintos
 * @author Wong Mestas
 */

public class Proyecto3 {
    public static void main(String[] args)throws InvalidMazeException{
        if (args.length == 0) {
            BufferedInputStream in = new BufferedInputStream(System.in);
            ResolverMaze.leerMze(in);
            ResolverMaze.resolverMaze();
        }else{
            if(GenerarMaze.getValues(args))GenerarMaze.generarMze();
        }
    }

}
