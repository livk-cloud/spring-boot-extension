package com.livk.sso.resoure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * <p>
 * Role
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@Data
public class Role implements GrantedAuthority {

    private Integer id;
    private String roleName;
    private String roleDescription;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }
}
