package com.livk.function;

import org.junit.jupiter.api.Test;

/**
 * <p>
 * BranchHandleTest
 * </p>
 *
 * @author livk
 * @date 2022/10/17
 */
class BranchHandleTest {

    @Test
    void trueOrFalseHandle() {
        BranchHandle.isTrueOrFalse(o -> true).trueOrFalseHandle(() -> System.out.println(true), () -> System.out.println(false));
        BranchHandle.isTrueOrFalse(o -> false).trueOrFalseHandle(() -> System.out.println(true), () -> System.out.println(false));
    }
}
