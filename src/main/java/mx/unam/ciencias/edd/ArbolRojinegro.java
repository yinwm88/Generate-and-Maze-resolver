package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
           super(elemento);
           color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            String s="";
            if(color==Color.ROJO){
                s+="R{";
            }else{
                s+="N{";
            }
            return s + elemento + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;

                return (color==vertice.color &&  super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro n = (VerticeRojinegro)ultimoAgregado;
        n.color=Color.ROJO;
        rebalancear(n);
    }

    private void rebalancear(VerticeRojinegro v){
             if(esNegro(v))return;
            VerticeRojinegro padre = (VerticeRojinegro)v.padre;
            
            // v es la raiz
            if(padre==null){
                v.color = Color.NEGRO;
                return;
            }

            // v tiene padre y  es negro
            if(esNegro(padre))return;
            // el padre de v es rojo, entonces
            VerticeRojinegro tio = tio(v);
            VerticeRojinegro abu = (VerticeRojinegro)padre.padre; 
            //si el tio es rojo
            if(!esNegro(tio)){
                tio.color=Color.NEGRO;
                padre.color=Color.NEGRO;
                abu.color=Color.ROJO;
                rebalancear(abu);
                return;
            }
            if(estanCruzados(padre,v)){
            //enderezamos
                if(esIzquierdo(padre))
                    super.giraIzquierda(padre);
                else
                    super.giraDerecha(padre);
            //acualizacion
            VerticeRojinegro r = padre;
            padre=v;
            v=r;
            } 
            //padre y vertice son rojos y no estan cruzados, continua el siguiente  caso:
            padre.color=Color.NEGRO;
            abu.color=Color.ROJO;
            if(esIzquierdo(v))
                super.giraDerecha(abu);
            else
                super.giraIzquierda(abu);

    }

    private boolean estanCruzados(VerticeRojinegro a, VerticeRojinegro b){
        return (!esIzquierdo(a)&&esIzquierdo(b))||(esIzquierdo(a)&&!esIzquierdo(b));
    }

    private boolean esNegro(VerticeRojinegro v){
        return v==null || v.color==Color.NEGRO;
    }

    private VerticeRojinegro padre(VerticeRojinegro v){
        return (VerticeRojinegro)v.padre;
    }

    private VerticeRojinegro abu (VerticeRojinegro v){
        return padre((VerticeRojinegro)v.padre);
    }

    private boolean esIzquierdo(VerticeRojinegro v){
        if(v.padre==null||v.padre.derecho==v)
            return false;      
        return true;    
    }

    private VerticeRojinegro tio (VerticeRojinegro v){
        if(esIzquierdo(padre(v)))          
            return (VerticeRojinegro)abu(v).derecho;
        return (VerticeRojinegro)abu(v).izquierdo;
    }

    private Vertice hijo(Vertice v){
        return v.derecho!=null?v.derecho:v.izquierdo;
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro v = (VerticeRojinegro)busca(elemento);
	    if(v == null)
            return;
	    if (v.derecho!=null && v.izquierdo!=null)
            v = (VerticeRojinegro)intercambiaEliminable(v);
	    VerticeRojinegro h = (VerticeRojinegro)hijo(v);
        elementos--;
        if(h==null){
            h = (VerticeRojinegro)nuevoVertice(null);
            h.padre=v;
            v.izquierdo=h;
            h.color=Color.NEGRO;
        }
        eliminaVertice(v);

	    // Si el hijo es ROJO, entonces el vertice eliminado es NEGRO, el hijo ahora es NEGRO.
	    if (!esNegro(h)) {
		    h.color = Color.NEGRO;
		    return;
	    }
	    // Si el eliminado es ROJO, el hijo sera NEGRO y no hacemos nada 
	    // Ambos vertices son NEGROS, entonces rebalanceamos sobre el hijo.
	    if (esNegro(v) && esNegro(h))
		    rebalancea(h);

	    // Si el hijo resulta ser fantasma, lo eliminamos.
	    if (h.elemento==null){
            if(h.padre==null)
                raiz=null;
            else if(esIzquierdo(h))
                h.padre.izquierdo=null;
            else
                h.padre.derecho=null;
        }

    }

    private void rebalancea(VerticeRojinegro v){
        // Caso 1, v no tiene padre.
        VerticeRojinegro padre = padre(v);
        if (padre==null)return;
        VerticeRojinegro hermano;
        hermano = hermano(v);
        // Caso 2, hermano es ROJO y por ende padre es NEGRO.
        if (!esNegro(hermano)){
            padre.color = Color.ROJO;
            hermano.color = Color.NEGRO;
            if (esIzquierdo(v) )
                super.giraIzquierda(padre);
            else 
                super.giraDerecha(padre);
        }
        
        //actualizamos el hermano de v
        hermano = hermano(v);
        VerticeRojinegro hi = (VerticeRojinegro)hermano.izquierdo;
        VerticeRojinegro hd = (VerticeRojinegro)hermano.derecho;

        // Caso 3
        if(esNegro(hi) && esNegro(hd) && esNegro(hermano) && esNegro(padre)){
            hermano.color = Color.ROJO;
            rebalancea(padre);
            return;
        }

        // Caso 4
        if (esNegro(hi) && esNegro(hd) && esNegro(hermano) && !esNegro(padre)){
            hermano.color = Color.ROJO;
            padre.color = Color.NEGRO;
            return;
        }

        // Caso 5a, hijoIzqHermano es ROJO y v es hijo izquierdo.
        if(esIzquierdo(v) && !esNegro(hi) && esNegro(hd)){				   
            hermano.color = Color.ROJO;
            hi.color = Color.NEGRO;
            super.giraDerecha(hermano);
        }

        // Caso 5b, hijoDerHermano es ROJO y v es hijo derecho.
        if (!esIzquierdo(v) && !esNegro(hd) && esNegro(hi)) {
            hermano.color = Color.ROJO;
            hd.color = Color.NEGRO;
            super.giraIzquierda(hermano);
        }

         // Actualizamos al hermano y sus hijos.
        hermano = hermano(v);
        hermano.color = padre.color;
        padre.color = Color.NEGRO;
        hi = (VerticeRojinegro)hermano.izquierdo;
        hd = (VerticeRojinegro)hermano.derecho;
       
        // Caso 6.
        if(esIzquierdo(v)) {
            hd.color = Color.NEGRO;
            super.giraIzquierda(padre);
        }else{
            hi.color = Color.NEGRO;
            super.giraDerecha(padre);
        }
            
    }

    private VerticeRojinegro hermano(VerticeRojinegro v){
        if(esIzquierdo(v))
            return (VerticeRojinegro)v.padre.derecho;
        return (VerticeRojinegro)v.padre.izquierdo;
    }
    
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}