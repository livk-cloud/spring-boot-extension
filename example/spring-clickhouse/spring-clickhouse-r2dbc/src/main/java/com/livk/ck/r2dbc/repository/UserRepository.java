package com.livk.ck.r2dbc.repository;

import com.livk.ck.r2dbc.entity.User;
import com.livk.commons.util.DateUtils;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;

/**
 * <p>
 * Repository
 * </p>
 *
 * @author livk
 */
@Repository
public class UserRepository {

    private final Mono<? extends Connection> mono;

    public UserRepository(ConnectionFactory connectionFactory) {
        this.mono = Mono.from(connectionFactory.create());
    }

    public Flux<User> findAll() {
        return mono.flatMapMany(connection ->
                        connection.createStatement("select id, app_id, version, reg_time from user").execute())
                .flatMap(result -> result.map(User::collect));
    }

    public Mono<Void> deleteById(Mono<Integer> id) {
        return id.flatMap(i ->
                mono.flatMapMany(connection ->
                        connection.createStatement("alter table user delete where id=:id")
                                .bind("id", i)
                                .execute()
                ).then()
        );
    }

    public Mono<Void> save(User user) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.YMD);
        String time = formatter.format(user.getRegTime());
        return mono.flatMapMany(connection ->
                connection.createStatement("insert into user values (:id,:appId,:version,:regTime)")
                        .bind("id", user.getId())
                        .bind("appId", user.getAppId())
                        .bind("version", user.getVersion())
                        .bind("regTime", time)
                        .execute()
        ).then();
    }
}
