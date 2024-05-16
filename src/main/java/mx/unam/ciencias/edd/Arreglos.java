package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        int a=0, b=arreglo.length-1;
        quickSort(arreglo, comparador, a, b);
    }

    private static <T> void quickSort(T[] arr, Comparator<T> comparador, int a, int b){
      if(b<=a)return;
      int i = a+1, j = b;
      while(i<j)
        if(comparador.compare(arr[i],arr[a])>0 && comparador.compare(arr[j], arr[a])<=0 ){
          intercambia(arr, i, j);
          i++;
          j--;
        }else if(comparador.compare(arr[i],arr[a])<=0 ){
          i++;
        }else{
          j--;
        }      
      if(comparador.compare(arr[i],arr[a])>0 )
          i--;
      intercambia(arr, a, i);
      quickSort(arr, comparador, a, i-1);
      quickSort(arr, comparador, i+1, b);
    }

    private static <T>  void intercambia(T[]arr, int a, int b){
        T e = arr[a];
        arr[a]=arr[b];
        arr[b]=e;
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
      for(int i=0; i<arreglo.length; i++){
        int m = i;
        for(int j=i+1; j<arreglo.length; j++){
          if(comparador.compare(arreglo[j], arreglo[m])<0)
            m = j;
        }
        intercambia(arreglo, i, m);
      }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
       int a = 0, b = arreglo.length-1;
       return busquedaBinaria(arreglo, elemento, comparador, a, b);
    }

    private static <T> int busquedaBinaria(T[] arr, T e, Comparator<T> com, int a, int b){
        if(b<a)return -1;
        int m = a+((b-a)/2);
        if(com.compare(arr[m], e)==0){
            return m;
        }else if(com.compare(e, arr[m])<0){
          return busquedaBinaria(arr, e, com, a, m-1);
        }else{
          return busquedaBinaria(arr, e, com, m+1, b);
        }
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}