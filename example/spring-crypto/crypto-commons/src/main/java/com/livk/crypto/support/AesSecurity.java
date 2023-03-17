package com.livk.crypto.support;

import com.livk.crypto.CryptoType;
import com.livk.crypto.parse.CryptoFormatter;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author livk
 */
public class AesSecurity implements CryptoFormatter<Long> {
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final String SALT = "livk-salt-id";

    private static final SecretKeySpec SPEC;

    static {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(CryptoType.AES.getAlgorithm());
            kg.init(256, new SecureRandom(SALT.getBytes()));
            SecretKey secretKey = kg.generateKey();
            SPEC = new SecretKeySpec(secretKey.getEncoded(), CryptoType.AES.getAlgorithm());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long parse(String text, Locale locale) throws ParseException {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SPEC);
            byte[] result = cipher.doFinal(Base64.decodeBase64(text));
            String str = new String(result, StandardCharsets.UTF_8);
            return Long.parseLong(str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String print(Long value, Locale locale) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, SPEC);
            byte[] byteContent = value.toString().getBytes(StandardCharsets.UTF_8);
            byte[] result = cipher.doFinal(byteContent);
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CryptoType type() {
        return CryptoType.AES;
    }
}
