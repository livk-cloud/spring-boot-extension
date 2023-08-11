package com.livk.rest.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @author livk
 */
@Data
@Entity(name = "`user`")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Column(length = 20)
    private Integer age;
}
