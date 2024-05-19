package mx.unam.ciencias.edd.proyecto3;

import java.util.Random;

/** 
 * Clase que representa un laberinto
 */
public class Maze {

    /**Clase interna para la representación de cada cuarto*/
    private class Room {
        private int wallAndScore; 

        private Room(int wallsAndScore) {
            this.wallAndScore = wallsAndScore;
        }

        private int getScore() {
            return (wallAndScore & 0xF0) >> 4;
        }

        private byte getWall() {
            return (byte) (wallAndScore & 0x0F);
        }

        private void setWall(int wall) {
            wallAndScore = (wallAndScore & 0xF0) | (wall & 0x0F);
        }
    }

    private int renglones, columnas;
    private Room[][] maze;
    private Random random; 

    private int s1, s2, f1, f2; // me refiero al índice
    private boolean hayRuta = false;
    private static final int ESTE = 1, NORTE = 2, OESTE = 4, SUR = 8;

    /**Constructor de un laberinto valido*/
    public Maze(int columnas, int renglones, Random random) {            
        this.columnas = columnas;
        this.renglones = renglones;
        this.random = random;
        this.maze = new Room[columnas][renglones];
        iniciarMaze();  
    }

    /**Metodo que inicializa un laberinto con puntajes aleatorios y un recorrido valido*/
    private void iniciarMaze() {
        allWallrandomScore();
        setEntradaySalida();
        demolerParedEntradaSalida();
        boolean[][] visitados = new boolean[columnas][renglones];
        conectaEntradaySalida(s1, f1, visitados);
    }

    /**Método que inicia el laberinto con todas las paredes y un score aleatorio*/
    private void allWallrandomScore() {
        for (int x = 0; x < columnas; x++) {
            for (int y = 0; y < renglones; y++) {
                int score = random.nextInt(16);
                maze[x][y] = new Room(score << 4 | 0x0F); // por default es mas facil poner todas las paredes, para despues derrumbarlas
            }
        }
    }

    private void setEntradaySalida() {
        s1 = random.nextInt(columnas);
        f1 = (s1 == columnas - 1 || s1 == 0) ? random.nextInt(renglones) : (random.nextBoolean() ? 0 : renglones - 1);
        do {
            s2 = random.nextInt(columnas);
            f2 = (s2 == columnas - 1 || s2 == 0) ? random.nextInt(renglones) : (random.nextBoolean() ? 0 : renglones - 1);
        } while (s1 == s2 && f1 == f2);
    }

    private void demolerParedEntradaSalida() {
        // Derribar pared en la entrada
        if (s1 == 0) {
            demolerPared(s1, f1, OESTE); 
        } else if (s1 == columnas - 1) {
            demolerPared(s1, f1, ESTE); 
        } else if (f1 == 0) {
            demolerPared(s1, f1, NORTE);
        } else if (f1 == renglones - 1) {
            demolerPared(s1, f1, SUR);
        }

        // Derribar pared en la salida
        if (s2 == 0) {
            demolerPared(s2, f2, OESTE);
        } else if (s2 == columnas - 1) {
            demolerPared(s2, f2, ESTE); 
        } else if (f2 == 0) {
            demolerPared(s2, f2, NORTE); 
        } else if (f2 == renglones - 1) {
            demolerPared(s2, f2, SUR); 
        }
    }

    private void conectaEntradaySalida(int x, int y, boolean[][] visitados) {
        if (x == s2 && y == f2) {
            hayRuta = true;
            return;
        }

        visitados[x][y] = true;
        int[] direcciones = {ESTE, NORTE, OESTE, SUR};
        barajar(direcciones);

        for (int direccion : direcciones) {
            int nx = x, ny = y;
            if (direccion == ESTE) nx++;
            else if (direccion == NORTE) ny--;
            else if (direccion == OESTE) nx--;
            else if (direccion == SUR) ny++;

            if (nx >= 0 && nx < columnas && ny >= 0 && ny < renglones && !visitados[nx][ny]) {
                demolerPared(x, y, direccion);
                demolerPared(nx, ny, getDireccionOpuesta(direccion));
                conectaEntradaySalida(nx, ny, visitados);
                if (hayRuta) return;
            }
        }
    }

    private void barajar(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int aux = arr[j];
            arr[j] = arr[i];
            arr[i] = aux;
        }
    }

    private void demolerPared(int x, int y, int direccion) {
        int mask = ~direccion;
        int actual = maze[x][y].getWall();
        maze[x][y].setWall(actual & mask);
    }

    private int getDireccionOpuesta(int direccion) {
        switch (direccion) {
            case ESTE:
                return OESTE;
            case NORTE:
                return SUR;
            case OESTE:
                return ESTE;
            case SUR:
                return NORTE;
            default:
                throw new IllegalArgumentException("¿Que direccion es esa?.");
        }
    }

    public byte[] getMazeByte(){
        byte[] mze = new byte[columnas * renglones];
        int i = 0;
        for (int y = 0; y < renglones; y++) {
            for (int x = 0; x < columnas; x++) {
                mze[i++] = (byte) maze[x][y].wallAndScore;
            }
        }
        return mze;
    }
}
