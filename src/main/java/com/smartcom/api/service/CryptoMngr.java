package com.smartcom.api.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
public class CryptoMngr {

    public static String ALGORITHM = "AES";
    private static String AES_CBS_PADDING = "AES/CBC/PKCS5Padding";

    public static byte[] encrypt(final byte[] key, final byte[] IV, final byte[] message) throws Exception {
        return CryptoMngr.encryptDecrypt(Cipher.ENCRYPT_MODE, key, IV, message);
    }

    public static byte[] decrypt(final byte[] key, final byte[] IV, final byte[] message) throws Exception {
        return CryptoMngr.encryptDecrypt(Cipher.DECRYPT_MODE, key, IV, message);
    }

    private static byte[] encryptDecrypt(final int mode, final byte[] key, final byte[] IV, final byte[] message)
            throws Exception {
        final Cipher cipher = Cipher.getInstance(AES_CBS_PADDING);
        final SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
        final IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(mode, keySpec, ivSpec);
        return cipher.doFinal(message);
    }
}