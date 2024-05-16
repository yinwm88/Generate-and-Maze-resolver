package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * 
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        int r = 0, i = 0, l = llave.length;
        while (l >= 4) {
            r ^= littleendian(llave[i], llave[i + 1], llave[i + 2], llave[i + 3]);
            i += 4;
            l -= 4;
        }
        int t = 0;
        switch (l) {
            case 3:
                t |= posicion(llave, 3);
                break;
            case 2:
                t |= posicion(llave, 2);
                break;
            case 1:
                t |= posicion(llave, 1);
                break;
        }

        return r ^ t;
    }

    private static int littleendian(byte a, byte b, byte c, byte d) {
        return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | ((d & 0xFF));
    }

    private static int posicion(byte[] b, int n) {
        int l = b.length;
        if (n == 3)
            return ((b[l - 3] & 0xFF) << 24) | ((b[l - 2] & 0xFF) << 16) | ((b[l - 1] & 0xFF) << 8);
        if (n == 2)
            return ((b[l - 2] & 0xFF) << 24) | ((b[l - 1] & 0xFF) << 16);

        return ((b[l - 1] & 0xFF) << 24);
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * 
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;

        int l = llave.length;
        int i = 0;
        boolean e = true;
        while (e) {
            a += bigendian(entero(llave, i+3), entero(llave, i+2),entero(llave, i+1), entero(llave, i));
            i += 4;

            b += bigendian(entero(llave, i+3), entero(llave, i+2),entero(llave, i+1), entero(llave, i));
            i += 4;

            if (l - i >= 4)
                c += bigendian(entero(llave, i+3), entero(llave, i+2),entero(llave, i+1), entero(llave, i));
            else {
                e = false;
                c += llave.length;
                c += bigendian(entero(llave, i+2), entero(llave, i+1),entero(llave, i), (byte)0);
            }

            i+=4;

            a -= b + c;
            a ^= (c >>> 13);
            b -= c + a;
            b ^= (a << 8);
            c -= a + b;
            c ^= (b >>> 13);

            a -= b + c;
            a ^= (c >>> 12);
            b -= c + a;
            b ^= (a << 16);
            c -= a + b;
            c ^= (b >>> 5);

            a -= b + c;
            a ^= (c >>> 3);
            b -= c + a;
            b ^= (a << 10);
            c -= a + b;
            c ^= (b >>> 15);
        }
        return c;
    }   

    private static int bigendian(byte a, byte b, byte c, byte d) {
        return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | ((d & 0xFF));
    }

    private static byte entero(byte[] llave, int i) {
        if (i < llave.length)
            return (byte)(0xFF & llave[i]);
        
        return (byte)0;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * 
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int h = 5381;
        int n = llave.length;

        for (int i = 0; i < n; i++)
            h += (h << 5) + (0xFF & llave[i]);

        return h;
    }
}