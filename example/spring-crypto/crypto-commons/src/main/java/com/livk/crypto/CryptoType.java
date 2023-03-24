package com.livk.crypto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author livk
 */
@Getter
@RequiredArgsConstructor
public enum CryptoType {

    AES("AES"),

    PBE("PBE");

    private final String algorithm;

    public static CryptoType match(String str) {
        for (CryptoType type : values()) {
            if (str.startsWith(type.getAlgorithm())) {
                return type;
            }
        }
        throw new IllegalArgumentException("未匹配到CryptoType str:" + str);
    }

    public String wrapper(String str) {
        return checkWrapper(str) ? str : algorithm + "(" + str + ")";
    }

    public String unwrap(String str) {
        if (checkWrapper(str)) {
            str = str.replaceFirst(algorithm + "\\(", "");
            int length = str.length();
            return str.substring(0, length - 1);
        } else {
            return str;
        }
    }

    private boolean checkWrapper(String str) {
        return str.startsWith(algorithm + "(") && str.endsWith(")");
    }
}
