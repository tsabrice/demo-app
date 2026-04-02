package ca.uqam.patchpilot.demo.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;

/**
 * Encryption utility service.
 *
 * VULNERABILITY 5 — Weak Cryptographic Algorithm (SonarQube rule: java:S4426)
 *
 * DES (Data Encryption Standard) uses a 56-bit key that can be brute-forced
 * with commodity hardware in hours. It has been deprecated since the late 1990s
 * and is classified as broken by NIST.
 *
 * Secure fix: replace DES with AES-256-GCM:
 *   KeyGenerator.getInstance("AES") with a key size of 256 bits,
 *   and Cipher.getInstance("AES/GCM/NoPadding").
 */
@Service
public class CryptoService {

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_BITS = 128;

    public byte[] encrypt(byte[] data) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_BITS, iv));
        return cipher.doFinal(data);
    }
}
