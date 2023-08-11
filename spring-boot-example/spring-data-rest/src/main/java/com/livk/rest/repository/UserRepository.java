package com.livk.rest.repository;

import com.livk.rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author livk
 */
@RepositoryRestResource(collectionResourceRel = "users", path = "user")
public interface UserRepository extends JpaRepository<User, Long> {

	@RestResource(rel = "auth", path = "auth")
	User findByUsernameAndPassword(@Param("name") String username, @Param("pwd") String password);

}
