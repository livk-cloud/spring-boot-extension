package com.livk.mvc.controller;

import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.AnnoDecrypt;
import com.livk.mvc.entity.Info;
import org.springframework.web.bind.annotation.*;

/**
 * @author livk
 */
@RestController
@RequestMapping("info")
public class InfoController {

    @GetMapping("{id}")
    public Info info(@PathVariable("id") @AnnoDecrypt(CryptoType.AES) Long variableId,
                     @RequestParam("id") @AnnoDecrypt(CryptoType.AES) Long paramId) {
        return new Info(variableId, paramId);
    }
}
