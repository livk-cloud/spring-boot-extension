package com.livk.r2dbc.repository;

import com.livk.r2dbc.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Repository
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

}
