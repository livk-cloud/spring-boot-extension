package com.livk.proto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author livk
 */
public record User(Long id, String name, String email, Integer sex) implements Serializable {
    @Serial
    private static final long serialVersionUID = 468062760765055608L;
}
