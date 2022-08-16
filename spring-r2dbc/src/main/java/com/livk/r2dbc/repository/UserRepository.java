package com.livk.r2dbc.repository;

import com.livk.r2dbc.domain.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * <p>
 * Repository
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
public interface UserRepository extends R2dbcRepository<User, Long> {

}
