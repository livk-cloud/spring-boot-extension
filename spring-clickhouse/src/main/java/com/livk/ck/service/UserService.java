package com.livk.ck.service;

import com.livk.ck.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author livk
 * @date 2022/11/9
 */
public interface UserService {

    List<User> list();

    boolean remove(@Param("regTime") String regTime);

    boolean save(User user);
}
