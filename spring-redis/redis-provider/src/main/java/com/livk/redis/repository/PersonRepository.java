package com.livk.redis.repository;

import com.livk.redis.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * PersonRepository
 * </p>
 *
 * @author livk
 * @date 2022/12/30
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, String> {
}
