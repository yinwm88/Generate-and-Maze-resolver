package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;


/** 
 * Clase que representa un laberinto
 */
public class Maze{

    /**Clase interna para la representación de cada cuarto*/
    private class Room {
        private byte wallsAndScore; // 8 bits: 4 bits mas significativos para el puntaje, 4 bits menos significativos para las paredes

        private Room(byte wallsAndScore) {
            this.wallsAndScore = wallsAndScore;
        }

        private int getScore() {
            return (wallsAndScore & 0xF0) >> 4;
        }

        private byte getWalls() {
            return wallsAndScore & 0x0F;
        }

        private void setWalls(int walls) {
            wallsAndScore = (wallsAndScore & 0xF0) | (walls & 0x0F);
        }

        /**
         * 
        private void setScore(int score) {
            wallsAndScore = (wallsAndScore & 0x0F) | ((score & 0x0F) << 4);
        }
         */
    }

    private int renglones, columnas;
    private Room[][] maze;
    private Room incio, fin;

    /** Puertas*/
    private static final byte ESTE = 0001, NORTE = 0010, OESTE = 0100, SUR = 1000;

    /**constructor de un laberinto valido*/
    public Maze(int columnas, int renglones, Random random) {
        this.columnas = columnas;
        this.renglones = renglones;
        this.maze = new Room[columnas][renglones];
        maze.iniciarMaze(random);
    }


    /**Metodo que inicializa un laberinto con puntajes aleatorios y un recorrido valido*/
    private void iniciarMaze(Random random){
        //la idea es inciar el laberinto de manera que todos los cuartos tengan todas las paredes y su score sea aleatorio
        for (int x = 0; x < columnas; x++) {
            for (int y = 0; y < renglones; y++) {
                int score = random.nextInt(16); 
                maze[x][y] = new Room(score << 4 | 0x0F); 
            }
        }




    } 




}
