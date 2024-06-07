package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.util.Random;

/** 
 * Clase que representa un laberinto
*/
public class Maze {

    /**
     * Clase interna para la representación de cada cuarto
    */
    private class Room {
        private int wallAndScore; 

        private Room(int wallsAndScore) {
            this.wallAndScore = wallsAndScore;
        }

        private int getAll(){
            return wallAndScore;
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

    Grafica<Room> graficaMaze = new Grafica<>();

    /**
    * Constructor para generar  un laberinto valido
    */
    public Maze(int columnas, int renglones, Random random) {            
        this.columnas = columnas;
        this.renglones = renglones;
        this.random = random;
        this.maze = new Room[columnas][renglones];
    }

    /**
    * Constructor para resolver  un laberinto dado
    */
    public Maze(byte[] mazeByte, int columnas, int renglones) {
        this.columnas = columnas;
        this.renglones = renglones;
        this.maze = new Room[columnas][renglones];
        int i = 0;
        for (int y = 0; y < renglones; y++) 
            for (int x = 0; x < columnas; x++) 
                maze[x][y] = new Room(mazeByte[i++]);    
    }

    /**
    * Metodo que inicializa un laberinto con puntajes aleatorios y un recorrido valido
    */
    public void iniciarMaze() {
        allWallrandomScore();
        setEntradaySalida();
        demolerParedEntradaSalida();
        boolean[][] visitados = new boolean[columnas][renglones];
        conectaEntradaySalida(s1, f1, visitados);
    }

    /**
    * Metodo que agrega un cuarto al laberinto con su puntaje y paredes corresondientes
    */
    private void agregarRoom(int x, int y, int score, int walls){
        maze[x][y] = new Room(score << 4 | walls);
    }

    /**
    * Método que inicia el laberinto con todas las paredes y un score aleatorio
    */
    private void allWallrandomScore() {
        for (int x = 0; x < columnas; x++) {
            for (int y = 0; y < renglones; y++) {
                int score = random.nextInt(16);
                agregarRoom(x, y, score, 0x0F); 
            }   
        }
    }

    /**
    * Metodo que modifica la entrada y salida del laberinto por crearse
    */
    private void setEntradaySalida() {
        s1 = random.nextInt(columnas);
        f1 = (s1 == columnas - 1 || s1 == 0) ? random.nextInt(renglones) : (random.nextBoolean() ? 0 : renglones - 1);
        do {
            s2 = random.nextInt(columnas);
            f2 = (s2 == columnas - 1 || s2 == 0) ? random.nextInt(renglones) : (random.nextBoolean() ? 0 : renglones - 1);
        } while (s1 == s2 && f1 == f2);
    }

    /**
    * Metodo que crea puertas en la salida y entrada
    */
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

    /**
    * Metodo que conecta la entrada con  la salida
    */
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

    /**
    * Metodo que le da aleatoriedad a la seleccion de una dirreccion.
    */
    private void barajar(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int aux = arr[j];
            arr[j] = arr[i];
            arr[i] = aux;
        }
    }

    /**
    * Metodo que abre paredes entre dos cuartos
    */
    private void demolerPared(int x, int y, int direccion) {
        int mask = ~direccion;
        int actual = maze[x][y].getWall();
        maze[x][y].setWall(actual & mask);
    }

    /**
    * Metodo para obtener la direccion opuesta de la direccion dada
    */
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
                throw new IllegalArgumentException("Direccion no contmeplada.");
        }
    }

    /**
    * Metodo que genera un arreglo de bytes, util para imprimir el laberinto 
    */
    public byte[] getMazeByte(){
        byte[] mze = new byte[columnas * renglones];
        int i = 0;
        for (int y = 0; y < renglones; y++) 
            for (int x = 0; x < columnas; x++) 
                mze[i++] = (byte) maze[x][y].wallAndScore;
            
        return mze;
    }


    // Metodo que convierte maze a una grafica ponderada
    private void mazeTografica(){
        graficaMaze = new Grafica<>();

        //llenamos la grafica de vertices(cuartos)
        for (int x = 0; x < columnas; x++) {
            for (int y = 0; y < renglones; y++) {
                graficaMaze.agrega(maze[x][y]);
            }
        }

         //invalidamos los indices de la entrada y salida
        s1 = -1; f1 = -1; s2 = -1; f2 = -1;

        // Conectar los cuartos adyacentes que no tienen pared entre ellos
        for(int x=0; x<columnas; x++){
            for(int y=0;y<renglones;y++){

                //CHECAMOS LAS POSIBLES INCOSISTENCIAS
                Room cuarto = maze[x][y];
                int currentScore = cuarto.getScore();
                int walls = cuarto.getWall();

                // Verificar este
                if (x + 1 < columnas && (walls & ESTE) == 0 && !graficaMaze.sonVecinos(cuarto , maze[x + 1][y])) {
                    graficaMaze.conecta(cuarto , maze[x + 1][y], currentScore + maze[x + 1][y].getScore());
                }

                // Verificar norte
                if (y - 1 >= 0 && (walls & NORTE) == 0 && !graficaMaze.sonVecinos(maze[x][y - 1],cuarto)) {
                    graficaMaze.conecta(cuarto , maze[x][y - 1], currentScore + maze[x][y - 1].getScore());
                }

                // Verificar oeste
                if (x - 1 >= 0 && (walls & OESTE) == 0 && !graficaMaze.sonVecinos(cuarto , maze[x - 1][y])) {
                    graficaMaze.conecta(cuarto , maze[x - 1][y], currentScore + maze[x - 1][y].getScore());
                }

                // Verificar sur
                if (y + 1 < renglones && (walls & SUR) == 0 && !graficaMaze.sonVecinos(cuarto , maze[x][y + 1])) {
                    graficaMaze.conecta(cuarto , maze[x][y + 1], currentScore + maze[x][y + 1].getScore());
                }


               
                // ENCONTRAR COORDENADAS DEL INICIO Y DEL DESTINO
                if(y==0 && (cuarto.getWall()&NORTE)==0){
                    if (s1 == -1) {
                        s1 = x; f1 = y;
                    } else if (s2 == -1) {
                        s2 = x; f2 = y;
                    } else {
                        System.err.println("Laberinto inválido: más de una entrada o salida encontrada.");
                    }
                }else if(x==0 && (cuarto.getWall()&OESTE)==0){
                    if (s1 == -1) {
                        s1 = x; f1 = y;
                    } else if (s2 == -1) {
                        s2 = x; f2 = y;
                    } else {
                        System.err.println("Laberinto inválido: más de una entrada o salida encontrada.");
                    }
                }else if(y==renglones-1 && (cuarto.getWall()&SUR)==0){
                    if (s1 == -1) {
                        s1 = x; f1 = y;
                    } else if (s2 == -1) {
                        s2 = x; f2 = y;
                    } else {
                        System.err.println("Laberinto inválido: más de una entrada o salida encontrada.");
                    }
                }else if(x==columnas-1 && (cuarto.getWall()&ESTE)==0){
                    if (s1 == -1) {
                        s1 = x; f1 = y;
                    } else if (s2 == -1) {
                        s2 = x; f2 = y;
                    } else {
                        System.err.println("Laberinto inválido: más de una entrada o salida encontrada.");
                    }
                }  
            }
        }

        // Verificar si se encontró  una entrada y una salida
        if (s1 == -1 || s2 == -1) {
            System.err.println("Laberinto inválido: no se encontraron una entrada o una salida.");
        }
    }

    // Metodo que usa dijkstra para devolver la ruta de peso minimo
    private Lista<VerticeGrafica<Room>> mazeDijkstra() throws InvalidMazeException {
        mazeTografica();
        if(s1<0 && s2<0 && f1<0 && f2<0){
            Lista<VerticeGrafica<Room>> lista = new Lista<>();
            return lista;
        }
        Room inicio = maze[s1][f1], fin=maze[s2][f2];
        return graficaMaze.dijkstra(inicio, fin);     

    }

    /**
    * Metodo que genera el SVG del laberinto
    */
    private String graficarMaze() {
        StringBuilder svg = new StringBuilder();
        svg.append("<svg viewBox=\"0 0 " + (columnas * 10) + " " + (renglones * 10) + "\">");
        for (int x = 0; x < columnas; x++) {
            for (int y = 0; y < renglones; y++) {
                byte wall = maze[x][y].getWall();
                if ((wall & NORTE) != 0) svg.append("<line x1=\"" + x * 10 + "\" y1=\"" + y * 10 + "\" x2=\"" + (x + 1) * 10 + "\" y2=\"" + y * 10 + "\" stroke=\"black\"/>");
                if ((wall & ESTE) != 0) svg.append("<line x1=\"" + (x + 1) * 10 + "\" y1=\"" + y * 10 + "\" x2=\"" + (x + 1) * 10 + "\" y2=\"" + (y + 1) * 10 + "\" stroke=\"black\"/>");
                if ((wall & SUR) != 0) svg.append("<line x1=\"" + x * 10 + "\" y1=\"" + (y + 1) * 10 + "\" x2=\"" + (x + 1) * 10 + "\" y2=\"" + (y + 1) * 10 + "\" stroke=\"black\"/>");
                if ((wall & OESTE) != 0) svg.append("<line x1=\"" + x * 10 + "\" y1=\"" + y * 10 + "\" x2=\"" + x * 10 + "\" y2=\"" + (y + 1) * 10 + "\" stroke=\"black\"/>");
            }
        }
        svg.append("<circle cx=\"" + (s1 * 10 + 5) + "\" cy=\"" + (f1 * 10 + 5) + "\" r=\"3\" fill=\"green\"/>");
        svg.append("<circle cx=\"" + (s2 * 10 + 5) + "\" cy=\"" + (f2 * 10 + 5) + "\" r=\"3\" fill=\"red\"/>");
        svg.append("</svg>");
        return svg.toString();
    }

    /**
    * Metodo que genera el SVG del camino encontrado por Dijkstra
    */
    public String graficarSolucion()  throws InvalidMazeException{
        Lista<VerticeGrafica<Room>> path = mazeDijkstra();
        if(!path.esVacia()){
            
            StringBuilder svg = new StringBuilder(graficarMaze());

            StringBuilder polylinePoints = new StringBuilder();

            for (VerticeGrafica<Room> vertice : path) {
                Room room = vertice.get();
                int x = -1, y = -1;

                outer: for (int i = 0; i < columnas; i++) {
                    for (int j = 0; j < renglones; j++) {
                        if (maze[i][j] == room) {
                            x = i;
                            y = j;
                            break outer;
                        }
                    }
                }

                if (x != -1 && y != -1) {
                    polylinePoints.append(x * 10 + 5).append(",").append(y * 10 + 5).append(" ");
                } else {
                    System.err.println("Error: Room not found in maze array.");
                }
            }

            svg.insert(svg.indexOf("</svg>"), "<polyline points=\"" + polylinePoints.toString() + "\" stroke=\"black\" fill=\"none\"/>");
            return svg.toString();
        }
        return "No hay solucion";
    }

    


}
