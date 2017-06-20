package es.albarregas.util;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/**
 * Clase con algunas utilidades que nos pueden ser necesarias
 * @author Ricardo
 */
public class Utilidades {

    /**
     * Encriptamos la cadena que se nos pase en MD5, que no se puede desencriptar por seguridad
     * @param cadena
     * @return
     * @throws Exception 
     */
    
    //En fase de pruebas. Probandose en métodos login, registro... 
    public static String encriptarMD5(String cadena) throws Exception {
        MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        md.update(cadena.getBytes());
        byte[] digest = md.digest();
        byte[] encoded = Base64.encodeBase64(digest);
        return new String(encoded);
    }

}
