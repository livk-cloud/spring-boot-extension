package com.livk.webflux.entity;

import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.AnnoEncrypt;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author livk
 */
@Data
@AllArgsConstructor
public class Info {
    @AnnoEncrypt(CryptoType.AES)
    private Long paramId;

    @AnnoEncrypt(CryptoType.AES)
    private Long idStr;
}
