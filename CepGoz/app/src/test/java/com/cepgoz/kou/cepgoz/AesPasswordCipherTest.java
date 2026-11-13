package com.cepgoz.kou.cepgoz;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AesPasswordCipherTest {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Test
    public void encryptDecrypt_roundTripsPassword() throws Exception {
        byte[] key = AesPasswordCipher.generateKey();
        byte[] plain = "secret-password-123".getBytes(UTF8);

        byte[] encrypted = AesPasswordCipher.encrypt(key, plain);
        byte[] decrypted = AesPasswordCipher.decrypt(key, encrypted);

        assertArrayEquals(plain, decrypted);
        assertFalse(Arrays.equals(plain, encrypted));
        assertTrue(encrypted.length > plain.length);
    }

    @Test
    public void encrypt_usesRandomIv() throws Exception {
        byte[] key = AesPasswordCipher.generateKey();
        byte[] plain = "same-password".getBytes(UTF8);

        byte[] first = AesPasswordCipher.encrypt(key, plain);
        byte[] second = AesPasswordCipher.encrypt(key, plain);

        assertFalse(Arrays.equals(first, second));
        assertEquals(
                new String(AesPasswordCipher.decrypt(key, first), UTF8),
                new String(AesPasswordCipher.decrypt(key, second), UTF8));
    }

    @Test(expected = Exception.class)
    public void decrypt_rejectsWrongKey() throws Exception {
        byte[] key = AesPasswordCipher.generateKey();
        byte[] otherKey = AesPasswordCipher.generateKey();
        byte[] encrypted = AesPasswordCipher.encrypt(key, "pw".getBytes(UTF8));
        AesPasswordCipher.decrypt(otherKey, encrypted);
    }
}
