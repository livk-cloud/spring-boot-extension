package com.livk.export.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author livk
 */
@Data
public class Authors {
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private Date birthdate;

    private Date added;
}
