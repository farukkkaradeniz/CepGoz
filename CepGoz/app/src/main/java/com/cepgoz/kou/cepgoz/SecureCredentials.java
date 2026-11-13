package com.cepgoz.kou.cepgoz;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

/**
 * Encrypts remember-me passwords before SharedPreferences storage.
 * AES key material is kept in app-private internal storage, not in the prefs XML.
 */
public final class SecureCredentials {

    private static final String KEY_FILE = "cepgoz_aes.key";
    private static final String ENC_PREFIX = "enc1:";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private SecureCredentials() {
    }

    public static String encryptPassword(Context context, String password) {
        if (password == null || password.length() == 0) {
            return "";
        }
        try {
            byte[] key = getOrCreateFileKey(context);
            byte[] encrypted = AesPasswordCipher.encrypt(key, password.getBytes(UTF8));
            return ENC_PREFIX + Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to encrypt password", e);
        }
    }

    public static String decryptPassword(Context context, String stored) {
        if (stored == null || stored.length() == 0) {
            return "";
        }
        if (!stored.startsWith(ENC_PREFIX)) {
            // Legacy plaintext must not be used for auto-login.
            return "";
        }
        try {
            byte[] key = getOrCreateFileKey(context);
            byte[] decoded = Base64.decode(stored.substring(ENC_PREFIX.length()), Base64.NO_WRAP);
            byte[] plain = AesPasswordCipher.decrypt(key, decoded);
            return new String(plain, UTF8);
        } catch (Exception e) {
            return "";
        }
    }

    public static void putEncryptedPassword(Context context, SharedPreferences.Editor editor,
                                            String prefKey, String password) {
        editor.putString(prefKey, encryptPassword(context, password));
    }

    public static String getDecryptedPassword(Context context, SharedPreferences prefs, String prefKey) {
        return decryptPassword(context, prefs.getString(prefKey, ""));
    }

    /** Removes legacy plaintext password entries so they are not left on disk. */
    public static void clearLegacyPlaintextPassword(SharedPreferences prefs, String prefKey) {
        String stored = prefs.getString(prefKey, "");
        if (stored != null && stored.length() > 0 && !stored.startsWith(ENC_PREFIX)) {
            prefs.edit().remove(prefKey).commit();
        }
    }

    private static synchronized byte[] getOrCreateFileKey(Context context) {
        File keyFile = new File(context.getFilesDir(), KEY_FILE);
        if (keyFile.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(keyFile);
                byte[] key = new byte[(int) keyFile.length()];
                int read = in.read(key);
                if (read == key.length && key.length > 0) {
                    return key;
                }
            } catch (Exception ignored) {
                // regenerate below
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        byte[] key = AesPasswordCipher.generateKey();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(keyFile);
            out.write(key);
            out.flush();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to persist encryption key", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ignored) {
                }
            }
        }
        return key;
    }
}
