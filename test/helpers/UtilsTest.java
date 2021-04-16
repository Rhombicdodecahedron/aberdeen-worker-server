package helpers;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cette classe renferme le la classe Utilstest de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 19.01.2021
 */
public class UtilsTest {

    /**
     * Constructeur de la classe UtilsTest.
     */
    public UtilsTest() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // Explications :
    //
    // Ce test permet de voir si un tableau de byte peut être correctement 
    // compressé et décompressé. Cela permet alors de voir si après la 
    // compression et la compression la valeur de sortie est la même que celle 
    // de départ. Ce teste fonctionne alors avec les méthodes "compressByteArray" 
    // et "decompressByteArray" main dans la main. Nous pouvons globalement dire 
    // que ce test est un test fonctionnel car nous testons le fonctionnement 
    // entre deux méthodes.
    //
    // N° CAS   IN                                            OUT
    // -------------------------------------------------------------------------
    // 1        null                                          null
    // 2	new byte[]{}                                  []
    // 3	new byte[]{1,2,3,4,5,6}                       [1,2,3,4,5,6]    
    //
    ////////////////////////////////////////////////////////////////////////////
    @Test
    public void testCompressByteArray() {
        byte[] byteTestCompress, byteTestDecompress, valeurATester;

        // 1        null                                              null
        valeurATester = null;
        byteTestCompress = Utils.compressByteArray(valeurATester);
        byteTestDecompress = Utils.decompressByteArray(byteTestCompress);
        assertArrayEquals(valeurATester, byteTestDecompress);

        // 2	new byte[]{}					  []
        valeurATester = new byte[]{};
        byteTestCompress = Utils.compressByteArray(valeurATester);
        byteTestDecompress = Utils.decompressByteArray(byteTestCompress);
        assertArrayEquals(valeurATester, byteTestDecompress);

        // 3	new byte[]{1,2,3,4,5,6}				  [1,2,3,4,5,6] 
        valeurATester = new byte[]{1, 2, 3, 4, 5, 6};
        byteTestCompress = Utils.compressByteArray(valeurATester);
        byteTestDecompress = Utils.decompressByteArray(byteTestCompress);
        assertArrayEquals(valeurATester, byteTestDecompress);

    }
}
