package com.livk.sso.commons.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * <p>
 * Role
 * </p>
 *
 * @author livk
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role implements GrantedAuthority {

    private Integer id;

    private String roleName;

    private String roleDescription;

    @JsonCreator
    public static Role create(@JsonProperty("authority") String authority) {
        Role role = new Role();
        role.setRoleName(authority);
        return role;
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }

}
