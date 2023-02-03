package com.livk.ck.jdbc.service;

import com.livk.ck.jdbc.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author livk
 */
public interface UserService {

    List<User> list();

    boolean remove(@Param("regTime") String regTime);

    boolean save(User user);
}
