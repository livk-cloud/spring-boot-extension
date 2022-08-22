package com.livk.auth.server.common.service;

import com.livk.auth.server.common.core.principal.Oauth2User;
import org.springframework.core.Ordered;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface Oauth2UserDetailsService extends UserDetailsService, Ordered {

    /**
     * 是否支持此客户端校验
     *
     * @param clientId 目标客户端
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     *
     * @return 排序值
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 通过用户实体查询
     *
     * @param Oauth2User user
     * @return
     */
    default UserDetails loadUserByUser(Oauth2User Oauth2User) {
        return this.loadUserByUsername(Oauth2User.getUsername());
    }

}
