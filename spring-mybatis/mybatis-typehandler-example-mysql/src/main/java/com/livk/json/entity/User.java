package com.livk.json.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 */
@Data
public class User {

    private Long id;

    private String username;

    private String password;

    private JsonNode des;

}
