package com.livk.redisson.order.entity;

import com.livk.commons.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * Employer
 * </p>
 *
 * @author livk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employer {
    private String name;
    private int age;
    private String wife;
    private Double salary;
    private String putTime;

    public void setPutTime() {
        this.putTime = DateUtils.format(LocalDateTime.now(), DateUtils.HMS);
    }
}
