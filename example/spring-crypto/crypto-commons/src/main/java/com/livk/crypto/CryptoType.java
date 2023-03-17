package com.livk.crypto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author livk
 */
@Getter
@RequiredArgsConstructor
public enum CryptoType {

    AES("AES");

    private final String algorithm;
}
