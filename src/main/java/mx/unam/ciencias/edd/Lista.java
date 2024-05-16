package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * 
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
          this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
          anterior = null;
          siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return  siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
          if(siguiente == null){
            throw new NoSuchElementException ("NoSuchElementException");
          }
          anterior = this.siguiente;
          siguiente = (this.siguiente).siguiente;
          return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
          if(anterior == null){
            throw new NoSuchElementException ("NoSuchElementException");
          }
          siguiente = this.anterior;
          anterior = (this.anterior).anterior;
          return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
          anterior = null;
          siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
          siguiente = null;
          anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
          return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
      agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
      if(elemento == null){
        throw new IllegalArgumentException("IllegalArgumentException");
      }
      Nodo n = new Nodo(elemento);
      if(esVacia()){
        cabeza = rabo = n;
      }else{
      n.anterior = rabo;
      rabo.siguiente = n;
      rabo = n;
      }
      longitud ++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
      if(elemento == null){
        throw new IllegalArgumentException("IllegalArgumentException");
      }
      Nodo n = new Nodo(elemento);
      if(esVacia()){
        cabeza = rabo = n;
      }else{
        cabeza.anterior = n;
        n.siguiente = cabeza;
        cabeza = n;
      }
      longitud ++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
      if(elemento == null){
        throw new IllegalArgumentException("IllegalArgumentException");
      }
      if(i <= 0){
        agregaInicio(elemento);
      }else if(i >= longitud){
        agregaFinal(elemento);
      }else{
        Nodo n = cabeza;
        Nodo m = new Nodo(elemento);
        while(i != 0 ){
          n = n.siguiente;
          i--;
        }
        n.anterior.siguiente = m;
        m.anterior = n.anterior;
        n.anterior = m;
        m.siguiente = n;
        longitud++;
    }
  }


    private Nodo getElemento(T elemento){
      Nodo n = cabeza;
      while( n != null){
        if(elemento.equals(n.elemento))
          return n;
        n = n.siguiente;
      }
      return null;
    }


    private void eliminaNodo(Nodo n){
      longitud --;
      if(esVacia()) return;
      if(cabeza.equals(rabo)){
        cabeza = rabo = null;
      }else if(n.equals(cabeza)){
        n.siguiente.anterior = null;
        cabeza = n.siguiente;
      }else if(n.equals(rabo)){
        n.anterior.siguiente = null;
        rabo = n.anterior;
      }else{
        n.anterior.siguiente = n.siguiente;
        n.siguiente.anterior = n.anterior;
      }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
      Nodo n = getElemento(elemento);
      if(n == null) return;
      eliminaNodo(n);
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
      if(esVacia()){
        throw new NoSuchElementException("NoSuchElementException");
      }else{
        T t = cabeza.elemento;
        eliminaNodo(cabeza);
        return t;
      }
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
      if(esVacia()){
        throw new NoSuchElementException("NoSuchElementException");
      }
      T t = rabo.elemento;
      eliminaNodo(rabo);
      return t;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
      Nodo n = cabeza;
      while(n != null){
        if(elemento.equals(n.elemento))
          return true;
        n = n.siguiente;
      }
      return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
      Nodo n = cabeza;
      Lista<T> r = new Lista<>();
      while(n != null){
        r.agregaInicio(n.elemento);
        n = n.siguiente;
      }
      return r;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
      Nodo n = cabeza;
      Lista<T> c = new Lista<>();
      while( n != null){
        c.agregaFinal(n.elemento);
        n = n.siguiente;
      }
      return  c;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
      cabeza = null;
      rabo = null;
      longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
      if(esVacia()){
        throw new NoSuchElementException("NoSuchElementException");
      }
      return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
      if(esVacia()){
        throw new NoSuchElementException("NoSuchElementException");
      }
      return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
      if(i < 0 || i >= longitud){
        throw new ExcepcionIndiceInvalido("ExcepcionIndiceInvalido");
      }
      Nodo n = cabeza;
      while(i != 0){
        n = n.siguiente;
        i--;
      }
      return n.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
      Nodo n = cabeza;
      int i = 0;

      if(elemento == null) return -1;
      while(n != null){
        if(elemento.equals(n.elemento))
          return i;
        i++;
        n = n.siguiente;
      }
      return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
      if(esVacia()){
        return "[]";
      }else{
        String s = "[";
        Nodo n = cabeza;

        while(n != null){
          if(n.equals(rabo)){
            s += n.elemento + "]";
          }else{
            s += n.elemento + ", ";
          }
          n = n.siguiente;
        }
        return s;
      }
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if(lista.longitud != longitud) return false;
        Nodo n = lista.cabeza;
        Nodo m = cabeza;

        while(n != null){
          while(!n.elemento.equals(m.elemento))
            return false;
          n = n.siguiente;
          m = m.siguiente;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
      if(this.longitud == 0 || this.longitud==1)
        return this.copia();

      Lista<T> lista1 = new Lista<>();
      Lista<T> lista2 = new Lista<>();
      int c = 0;
      Nodo n = cabeza;

      while(c < this.longitud/2){
        lista1.agrega(n.elemento);
        n = n.siguiente;
        c++;
      }
      while(c < this.longitud){
        lista2.agrega(n.elemento);
        n = n.siguiente;
        c++;
      }
      lista1 =  lista1.mergeSort(comparador);
      lista2 = lista2.mergeSort(comparador);
      return merge(comparador, lista1, lista2);
    }

    private Lista<T> merge(Comparator<T> com, Lista<T> l1, Lista<T> l2){
      Lista<T> lc = new Lista<>();
      Iterador i = (Iterador)l1.iterator(), j = (Iterador)l2.iterator();

      while(i.hasNext() && j.hasNext())
          if(com.compare(i.siguiente.elemento, j.siguiente.elemento)<=0){
            lc.agrega(i.next());
          }else{
            lc.agrega(j.next());
          }
      while(j.hasNext())
        lc.agrega(j.next());
      while(i.hasNext())
        lc.agrega(i.next());
      return lc;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
    if(elemento==null)return false;
    if(comparador.compare(elemento, cabeza.elemento)<0)return false;
    if(comparador.compare(elemento, rabo.elemento)>0)return false;
    Nodo n = cabeza;
    while(n!=null){
      if(comparador.compare(elemento, n.elemento)==0)
        return true;
      n = n.siguiente;
    }
    return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}