package com.livk.auth.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.livk.auth.server.common.service.Oauth2UserDetailsService;
import com.livk.auth.server.domain.User;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author livk
 * @date 2021/12/22
 */
public interface UserService extends IService<User>, Oauth2UserDetailsService {

}
