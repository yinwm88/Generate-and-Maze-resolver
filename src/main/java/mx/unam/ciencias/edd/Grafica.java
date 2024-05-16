package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override
        public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override
        public T next() {
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La distancia del vértice. */
        private double distancia;
        /* El índice del vértice. */
        private int indice;
        /* La lista de vecinos del vértice. */
        private Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            color = Color.NINGUNO;
            vecinos = new Lista<>();
        }

        /* Regresa el elemento del vértice. */
        @Override
        public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override
        public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override
        public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override
        public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            return compare(distancia, vertice.distancia);
        }

        private int compare(double a, double b){
            if(a<b){
                return -1;
            }else if(a>b){
                return 1;
            }else{
                return 0;
            }
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos();
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica<T>.Vertice v, Grafica<T>.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(contiene(elemento))
            throw new IllegalArgumentException("El elemento ya esta contenido en la gráfica.");
        if(elemento==null)
            throw new IllegalArgumentException("No se puede agregar null a la gráfica.");
        Vertice v = new Vertice(elemento);
        vertices.agrega(v);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        conecta(a, b, 1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {

        if(a.equals(b))
            throw new IllegalArgumentException("Se está conectando el vertice consigo mismo.");
        
        if(peso<=0)
            throw new IllegalArgumentException("El peso asignado no es poitivo.");
        
        Vertice va = (Vertice)vertice(a);
        Vertice vb = (Vertice)vertice(b);
        
        if(sonVecinos(a, b))
            throw new IllegalArgumentException("No hay nada que conectar, ya estan conectados.");

        va.vecinos.agrega(new Vecino(vb, peso));
        vb.vecinos.agrega(new Vecino(va, peso));
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice vA = (Vertice)vertice(a);
        Vertice vB = (Vertice)vertice(b);
        
        if(!sonVecinos(a, b))
            throw new IllegalArgumentException("No están conectados.");

       for(Vecino vecino : vA.vecinos)
            if(vecino.vecino.elemento.equals(vB.elemento))
                vA.vecinos.elimina(vecino);

        for(Vecino vecino : vB.vecinos)
            if(vecino.vecino.elemento.equals(vB.elemento))
                vB.vecinos.elimina(vecino);

        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(Vertice v: vertices)
            if(v.elemento.equals(elemento))
                return true;

        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice vertice = (Vertice)vertice(elemento);

        for(Vecino ve : vertice.vecinos)
            desconecta(ve.vecino.elemento, vertice.elemento);
        
        vertices.elimina(vertice);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
     Vertice vA = (Vertice)vertice(a);
     Vertice vB = (Vertice)vertice(b);

     for(Vecino v : vA.vecinos)
        if(v.vecino.equals(vB))
            return true;

     return false;   
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        Vertice vA = (Vertice)vertice(a);
        Vertice vB = (Vertice)vertice(b);
        
        for(Vecino vecino : vA.vecinos)
            if(vecino.vecino.equals(vB))
                return vecino.peso;
        
        throw new IllegalArgumentException("No están conectados.");
        
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
       Vertice va = (Vertice)vertice(a);
       Vertice vb = (Vertice)vertice(b);

        if(peso<=0)
            throw new IllegalArgumentException("El peso es menor o igual a 0.");

        if(!sonVecinos(a, b))
            throw new IllegalArgumentException("Los vértices no son vecinos.");             
        
        for (Vecino vecino : va.vecinos)
            if (vecino.vecino.equals(vb)) {
                vecino.peso = peso;
                break;
            }
            
        for (Vecino vecino : vb.vecinos)
            if (vecino.vecino.equals(va)) {
                vecino.peso = peso;
            }    
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {

        for(Vertice v : vertices)
            if(v.elemento.equals(elemento))
                return v;
        
        throw new NoSuchElementException("El elemento no se encuentra en la gráfica.");
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {

        if(vertice == null || (vertice.getClass() != Vertice.class && vertice.getClass() != Vecino.class))
            throw new IllegalArgumentException("Vértice Inválido.");
        
        if(vertice.getClass() == Vertice.class){
            Vertice v = (Vertice)vertice;
            v.color = color;
        }

        if(vertice.getClass() == Vecino.class){
            Vecino v = (Vecino)vertice;
            v.vecino.color = color; 
        }

    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        recorridoBFS(vertices.getPrimero());
        
        for(Vertice v : vertices)
            if(v.color == Color.ROJO)
                return false;
        
        return true;    
    }

    private void recorridoBFS (Vertice v){
        paraCadaVertice((u)->setColor(u, Color.ROJO));
        setColor(v, Color.NEGRO);
        Cola<Vertice> q = new Cola<>();
        q.mete(v);
        while(!q.esVacia()){
            v = q.saca();
            for(Vecino u : v.vecinos){
                if(u.vecino.getColor() == Color.ROJO){
                    setColor(u.vecino, Color.NEGRO);
                    q.mete(u.vecino);
                }
            }
        }

    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice v : vertices)
            accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * 
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *                 recorrido.
     * @param accion   la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        paraCadaVertice((u) -> setColor(u, Color.ROJO));
        Vertice v = (Vertice) vertice(elemento);
        Cola<Vertice> q = new Cola<>();
        v.color=Color.NEGRO;
        q.mete(v);
        while (!q.esVacia()) {
            v = q.saca();
            accion.actua(v);
            for (Vecino m : v.vecinos) {
                if (m.vecino.color == Color.ROJO) {
                    setColor(m.vecino, Color.NEGRO);
                    q.mete(m.vecino);
                }
            }
        }
        paraCadaVertice((u) -> setColor(u, Color.NINGUNO));
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * 
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *                 recorrido.
     * @param accion   la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = (Vertice) vertice(elemento);
        Pila<Vertice> s = new Pila<>();
        paraCadaVertice((u) -> setColor(u, Color.ROJO));
        v.color=Color.NEGRO;
        s.mete(v);
        while (!s.esVacia()) {
            v = s.saca();
            accion.actua(v);
            for (Vecino m : v.vecinos) {
                if (m.vecino.color == Color.ROJO) {
                    setColor(m.vecino, Color.NEGRO);
                    s.mete(m.vecino);
                }
            }
        }
        paraCadaVertice((u) -> setColor(u, Color.NINGUNO));
    }

    /**
     * Nos dice si la gráfica es vacía.
     * 
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override
    public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override
    public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * 
     * @return una representación en cadena de la gráfica.
     */
    @Override
    public String toString(){
        String s = "{";
        for (Vertice v : vertices) 
           s+=String.format("%s, ", v.elemento.toString());
    
        s += "}, {";

        Lista<T> marcados = new Lista<>();
        for (Vertice vertice : vertices) {
            for (Vecino vecino : vertice.vecinos)
                if (!marcados.contiene(vecino.vecino.elemento))
                    s += String.format("(%s, %s), ",vertice.elemento.toString(), vecino.vecino.elemento.toString());

            marcados.agrega(vertice.elemento);
        
        }
        return s + "}";
    }

  

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * 
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override
    public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")Grafica<T> grafica = (Grafica<T>) objeto;
        
        if (aristas != grafica.aristas ||  vertices.getLongitud() != grafica.vertices.getLongitud())
            return false;

        for(Vertice v : vertices)
            if(!grafica.contiene(v.elemento))
                return false;
    
        for(Vertice v : vertices){
           Vertice u = (Vertice)grafica.vertice(v.elemento);
        
           if(v.vecinos.getLongitud()!=u.vecinos.getLongitud())
                return false;

            for(Vecino x : v.vecinos){
                Boolean cont = false;
                for(Vecino y : u.vecinos){
                    if(x.vecino.elemento.equals(y.vecino.elemento)){
                        cont = true;
                        break;
                    }
                }
                if(!cont)
                    return false;
            }
        }

        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * 
     * @return un iterador para iterar la gráfica.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        Vertice s = (Vertice)vertice(origen);
        Vertice t = (Vertice)vertice(destino);

        if(s==t){
            Lista<VerticeGrafica<T>> tM = new Lista<>();
            tM.agrega(s);
            return tM;
        }

        for(Vertice v : vertices)
            v.distancia = Double.MAX_VALUE;
        
        s.distancia = 0;

        Cola<Vertice> q = new Cola<>();
        q.mete(s);
        
        while(!q.esVacia()){
            Vertice u = q.saca();
            for(Vecino vecino : u.vecinos){
                   if(vecino.vecino.distancia == Double.MAX_VALUE){
                    vecino.vecino.distancia = u.distancia + 1;
                    q.mete(vecino.vecino);
                   } 
            }
        }

        return reconstruyeTrayectoriaMinima((aux, vecino) -> vecino.vecino.distancia == aux.distancia-1, t);
    }

    /**
     * Metodo que reconstruye la trayectoria(tanto la mínima como la de peso minimo) 
     * dado un vertice destino a el vertice origen.
     * @param buscador elige el vecino por el cual continuar.
     * @param t vertice destino del cual se construye la treyector'ia.
     * @return La trayectoría reconstruida.
     */
    private  Lista<VerticeGrafica<T>> reconstruyeTrayectoriaMinima(BuscadorCamino<T> buscador, Vertice t){
        if(t.distancia == Double.MAX_VALUE)
            return new Lista<VerticeGrafica<T>>();
        
        Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
        Vertice a = t;
        trayectoria.agrega(a);
        
        while(a.distancia!=0){
            for(Vecino vecino : a.vecinos)
                if(buscador.seSiguen(a, vecino)){
                    trayectoria.agrega(vecino.vecino);
                    a = vecino.vecino;
                }        
        }

        return trayectoria.reversa();
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
       Vertice s = (Vertice)vertice(origen);
       Vertice t = (Vertice)vertice(destino);
        
       for(Vertice v : vertices)
            v.distancia = Double.MAX_VALUE;
       
        s.distancia = 0;
        
        MonticuloDijkstra <Vertice> monticulo = new MonticuloMinimo<>(vertices);

        while(!monticulo.esVacia()){
            Vertice u = monticulo.elimina();
            for(Vecino vecino : u.vecinos){
                if(vecino.vecino.distancia   > u.distancia  + vecino.peso){
                    vecino.vecino.distancia = u.distancia + vecino.peso; 
                    monticulo.reordena(vecino.vecino);
                }
            }
        }

        return reconstruyeTrayectoriaMinima((aux, vecino) -> (vecino.vecino.distancia + vecino.peso) == aux.distancia, t);
    }
}