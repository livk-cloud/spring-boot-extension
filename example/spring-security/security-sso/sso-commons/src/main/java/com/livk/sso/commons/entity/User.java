package com.livk.sso.commons.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private Integer status;

    private List<Role> roles;

    @JsonCreator
    public static User create(@JsonProperty("id") Long id,
                              @JsonProperty("username") String username,
                              @JsonProperty("password") String password,
                              @JsonProperty("status") Integer status,
                              @JsonProperty("roles") List<Role> roles) {
        return new User().setId(id)
                .setUsername(username)
                .setPassword(password)
                .setStatus(status)
                .setRoles(roles);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_ADMIN");
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
