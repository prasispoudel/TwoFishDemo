package com.example.TwoFishDemo.util;

import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

public class TwoFishUtil {
    private static final String ALGO = "Twofish";
    static {
        Security.addProvider(new BouncyCastlePQCProvider());
    }

    public static String encrypt (String data, String secret) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte [] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData, String secret_key) throws Exception{
        Cipher cipher =Cipher.getInstance(ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(secret_key.getBytes(), ALGO);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte [] decoded = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
    }

}
