package com.library.kernel.framework.auth;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Hash de contraseñas con PBKDF2 (incluido en el JDK, sin dependencias externas).
 *
 * Formato almacenado: pbkdf2$iteraciones$saltBase64$hashBase64
 */
public final class PasswordHasher {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 210_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher(){}

    public static String hash(String password){
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] hash = derive(password, salt, ITERATIONS);

        Base64.Encoder encoder = Base64.getEncoder();
        return PREFIX + "$" + ITERATIONS + "$" + encoder.encodeToString(salt) + "$" + encoder.encodeToString(hash);
    }

    public static boolean verify(String password, String storedHash){
        if(password == null || storedHash == null){
            return false;
        }

        String[] parts = storedHash.split("\\$");
        if(parts.length != 4 || !parts[0].equals(PREFIX)){
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[1]);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] salt = decoder.decode(parts[2]);
            byte[] expected = decoder.decode(parts[3]);

            // Comparación en tiempo constante para no filtrar información por tiempos
            return MessageDigest.isEqual(expected, derive(password, salt, iterations));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derive(String password, byte[] salt, int iterations){
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_BITS);
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 no disponible en este JDK", e);
        } finally {
            spec.clearPassword();
        }
    }
}
