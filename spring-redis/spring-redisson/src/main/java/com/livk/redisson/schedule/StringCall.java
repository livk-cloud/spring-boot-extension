package com.livk.redisson.schedule;

import java.io.Serializable;

/**
 * <p>
 * StringCall
 * </p>
 *
 * @author livk
 */
public class StringCall implements Serializable, Runnable {

    @Override
    public void run() {
        System.out.println("66666666666");
    }
}
