package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>
 * Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.
 * </p>
 *
 * <p>
 * Un árbol instancia de esta clase siempre cumple que:
 * </p>
 * <ul>
 * <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 * descendientes por la izquierda.</li>
 * <li>Cualquier elemento en el árbol es menor o igual que todos sus
 * descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
        extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            pila = new Pila<>();
            Vertice v = raiz;
            while (v != null) {
                pila.mete(v);
                v = v.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override
        public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override
        public T next() {
            Vertice v = pila.saca();
            T e = v.elemento;
            if (v.hayDerecho()) {
                pila.mete(v.derecho);
                v = v.derecho.izquierdo;
                while (v != null) {
                    pila.mete(v);
                    v = v.izquierdo;
                }
            }
            return e;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() {
        super();
    }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * 
     * @param coleccion la colección a partir de la cual creamos el árbol
     *                  binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * 
     * @param elemento el elemento a agregar.
     */
    @Override
    public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException();
        Vertice n = nuevoVertice(elemento);
        elementos++;
        ultimoAgregado = n;
        if (raiz == null) {
            raiz = n;
        } else {
            aux(raiz, n);
        }
    }

    private void aux(Vertice v, Vertice n) {
        if (n.elemento.compareTo(v.elemento) <= 0) {
            if (!v.hayIzquierdo()) {
                v.izquierdo = n;
                n.padre = v;
            } else {
                aux(v.izquierdo, n);
            }
        } else {
            if (!v.hayDerecho()) {
                v.derecho = n;
                n.padre = v;
            } else {
                aux(v.derecho, n);
            }
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * 
     * @param elemento el elemento a eliminar.
     */
    @Override
    public void elimina(T elemento) {
        if (elemento == null)
            return;
        Vertice e = vertice(busca(elemento));
        if (e == null)
            return;
        elementos--;
        if (tieneLosDosHijo(e)) {
            e = intercambiaEliminable(e);
        }
        eliminaVertice(e);
    }

    private boolean tieneLosDosHijo(Vertice v) {
        if (v.hayDerecho() && v.hayIzquierdo())
            return true;
        return false;
    }

    private Vertice maximoEnSubArbol(Vertice v) {
        if (v.derecho == null)
            return v;
        return maximoEnSubArbol(v.derecho);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * 
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice max = maximoEnSubArbol(vertice.izquierdo);
        T e = vertice.elemento;
        vertice.elemento = max.elemento;
        max.elemento = e;
        return max;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * 
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        Vertice u = returnUnHijo(vertice);
        Vertice p = vertice.padre;

        if (!vertice.hayPadre()) {
            raiz = u;
        } else {
            if (vertice.equals(vertice.padre.izquierdo)) {
                p.izquierdo = u;
            } else {
                p.derecho = u;
            }
        }
        if (u != null)
            u.padre = p;
    }

    private Vertice returnUnHijo(Vertice v) {
        if (v.hayIzquierdo())
            return v.izquierdo;
        return v.derecho;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * 
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override
    public VerticeArbolBinario<T> busca(T elemento) {
        if (elemento == null)
            return null;
        return busca(elemento, raiz);
    }

    private Vertice busca(T e, Vertice v) {
        if (v == null)
            return null;
        if (e.compareTo(v.elemento) == 0) {
            return v;
        } else if (e.compareTo(v.elemento) < 0) {
            return busca(e, v.izquierdo);
        } else {
            return busca(e, v.derecho);
        }

    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * 
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * 
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        if (vertice == null || !vertice.hayIzquierdo())
            return;
        Vertice v = vertice(vertice);
        Vertice i = v.izquierdo;
        i.padre = v.padre;
        if (v != raiz) {
            if (v.padre.izquierdo == v)
                i.padre.izquierdo = i;
            if (v.padre.derecho == v)
                i.padre.derecho = i;
        }
        if (v == raiz)
            raiz = i;
        v.izquierdo = i.derecho;
        if (i.hayDerecho())
            i.derecho.padre = v;
        v.padre = i;
        i.derecho = v;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * 
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        if (vertice == null || !vertice.hayDerecho())
            return;
        Vertice v = vertice(vertice);
        Vertice d = v.derecho;
        d.padre = v.padre;
        if (v != raiz) {
            if (v.padre.izquierdo == v)
                d.padre.izquierdo = d;
            if (v.padre.derecho == v)
                d.padre.derecho = d;
        }
        if (v == raiz)
            raiz = d;
        v.derecho = d.izquierdo;
        if (d.hayIzquierdo())
            d.izquierdo.padre = v;
        v.padre = d;
        d.izquierdo = v;
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * 
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPreOrder(accion, raiz);
    }

    private void dfsPreOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
        if (v == null)
            return;
        accion.actua(v);
        dfsPreOrder(accion, v.izquierdo);
        dfsPreOrder(accion, v.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * 
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        dfsInOrder(accion, raiz);
    }

    private void dfsInOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
        if (v == null)
            return;
        dfsInOrder(accion, v.izquierdo);
        accion.actua(v);
        dfsInOrder(accion, v.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * 
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(accion, raiz);
    }

    private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
        if (v == null)
            return;
        dfsPostOrder(accion, v.izquierdo);
        dfsPostOrder(accion, v.derecho);
        accion.actua(v);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * 
     * @return un iterador para iterar el árbol.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterador();
    }
}