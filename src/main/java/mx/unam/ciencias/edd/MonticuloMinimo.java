package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice<elementos;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if(indice>=elementos)
                throw new NoSuchElementException("No hay siguiente");
            return arbol[indice++];
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100);
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        arbol= nuevoArreglo(n);
        for(T e: iterable)
            agregaEn(elementos++, e);
       
        for(int i = (n/2)-1; i>=0; i--)
            acomodarAbajo(i);
    }

    private void agregaEn(int indice, T elemento){
        arbol[indice]=elemento;
        elemento.setIndice(indice);
    }

    private void acomodarAbajo(int v){
        int vIzq=2*v + 1;
        int vDer=2*v + 2;
        int minimo=v;

        if(vIzq<elementos&&arbol[vIzq].compareTo(arbol[minimo])<0)
            minimo=vIzq;
        
        if(vDer<elementos&& arbol[vDer].compareTo(arbol[minimo])<0)
            minimo=vDer;

        if(minimo!=v){
            intercambia(minimo, v);
            acomodarAbajo(minimo);
        }   
    }

    private void intercambia(int m, int v){
        T e = arbol[v];
        arbol[v]=arbol[m];
        arbol[m]=e;
        arbol[v].setIndice(v);
        arbol[m].setIndice(m);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if(elementos==arbol.length){
            T[] nuevo = nuevoArreglo(elementos*2);
            for(int i=0; i<elementos; i++)
                nuevo[i]=arbol[i];
            arbol = nuevo;    
        }
        agregaEn(elementos, elemento);
        elementos++;
        acomodaArriba(elementos-1);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(elementos==0)
            throw new IllegalStateException("El montículo se encuentra vacío.");
       
       T raiz = arbol[0];
       intercambia(0, (elementos-1));
       arbol[elementos-1].setIndice(-1);
       elementos--;
       acomodarAbajo(0);

       return raiz;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if(elemento.getIndice()<0||elemento.getIndice()>=elementos)
            return;

        int i = elemento.getIndice();
        intercambia(i, elementos-1);
        elementos--;
        reordena(arbol[i]);
        arbol[elementos].setIndice(-1);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        int i = elemento.getIndice();
        return (i<0 || i>=elementos)?false:arbol[i].equals(elemento);
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        arbol=nuevoArreglo(elementos);
        elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        int i = elemento.getIndice();
        acomodaArriba(i);
        acomodarAbajo(i);
    }

    private void acomodaArriba(int v){
        int vPadre = (v - 1)/2;

        if (v > 0 && arbol[v].compareTo(arbol[vPadre]) < 0) {
            intercambia(v, vPadre);
            acomodaArriba(vPadre);
        }
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
       if(i<0||i>=elementos)
            throw new NoSuchElementException("No existe tal elemento del árbol.");

        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String s ="";
        for(T e:arbol)
            s += e.toString() + ", ";
        
        return s;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if(monticulo.elementos!=elementos)
            return false;
        for(int i = 0; i<elementos; i++)
                if(!arbol[i].equals(monticulo.arbol[i]))
                    return false;

        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        
        Lista<Adaptador<T>> lista = new Lista<>();
        Lista<T> heapsort = new Lista<>();
        
        for(T e : coleccion)
            lista.agrega(new Adaptador<T>(e));
        
        MonticuloMinimo<Adaptador<T>>  monticulo = new MonticuloMinimo<>(lista);
        
        while(!monticulo.esVacia()){
            Adaptador<T> eliminado = monticulo.elimina();
            heapsort.agrega(eliminado.elemento);
        }

        return heapsort;
    }
}