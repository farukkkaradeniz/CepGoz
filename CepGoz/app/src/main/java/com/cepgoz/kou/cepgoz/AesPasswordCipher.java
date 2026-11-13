package com.cepgoz.kou.cepgoz;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES/CBC encryption for credential values. Pure Java so it is unit-testable
 * without the Android framework.
 */
public final class AesPasswordCipher {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;
    private static final int KEY_LENGTH = 16;

    private AesPasswordCipher() {
    }

    public static byte[] encrypt(byte[] keyBytes, byte[] plaintext) throws GeneralSecurityException {
        byte[] key = normalizeKey(keyBytes);
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        byte[] ciphertext = cipher.doFinal(plaintext);
        byte[] out = new byte[IV_LENGTH + ciphertext.length];
        System.arraycopy(iv, 0, out, 0, IV_LENGTH);
        System.arraycopy(ciphertext, 0, out, IV_LENGTH, ciphertext.length);
        return out;
    }

    public static byte[] decrypt(byte[] keyBytes, byte[] ivAndCiphertext) throws GeneralSecurityException {
        if (ivAndCiphertext == null || ivAndCiphertext.length <= IV_LENGTH) {
            throw new GeneralSecurityException("ciphertext too short");
        }
        byte[] key = normalizeKey(keyBytes);
        byte[] iv = Arrays.copyOfRange(ivAndCiphertext, 0, IV_LENGTH);
        byte[] ciphertext = Arrays.copyOfRange(ivAndCiphertext, IV_LENGTH, ivAndCiphertext.length);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        return cipher.doFinal(ciphertext);
    }

    public static byte[] generateKey() {
        byte[] key = new byte[KEY_LENGTH];
        new SecureRandom().nextBytes(key);
        return key;
    }

    private static byte[] normalizeKey(byte[] keyBytes) {
        if (keyBytes == null || keyBytes.length == 0) {
            throw new IllegalArgumentException("key required");
        }
        if (keyBytes.length == KEY_LENGTH) {
            return keyBytes;
        }
        byte[] normalized = new byte[KEY_LENGTH];
        System.arraycopy(keyBytes, 0, normalized, 0, Math.min(keyBytes.length, KEY_LENGTH));
        return normalized;
    }
}
