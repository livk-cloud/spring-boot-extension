package com.livk.webflux.entity;

import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.AnnoDecrypt;
import com.livk.crypto.annotation.AnnoEncrypt;
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
    @AnnoEncrypt(CryptoType.AES)
    @AnnoDecrypt(CryptoType.AES)
    private Long variableId;

    @AnnoEncrypt(CryptoType.AES)
    @AnnoDecrypt(CryptoType.AES)
    private Long paramId;
}
