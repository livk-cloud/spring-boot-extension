package com.livk.mvc.entity;

import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.CryptoDecrypt;
import com.livk.crypto.annotation.CryptoEncrypt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author livk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Info {
    @CryptoEncrypt(CryptoType.AES)
    @CryptoDecrypt
    private Long variableId;

    @CryptoEncrypt(CryptoType.AES)
    @CryptoDecrypt
    private Long paramId;
}
