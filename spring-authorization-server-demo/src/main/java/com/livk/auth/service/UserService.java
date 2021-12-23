package com.livk.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.livk.auth.domain.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author livk
 * @date 2021/12/22
 */
public interface UserService extends IService<Users>, UserDetailsService {
}
