// 和 com.orderhub.domain.User 在一个包下，所以不用 import
package com.orderhub.domain;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserTest {


    /**
     * 这里看到对于 rename 方法，我们对于不同的场景写了两个测试方法，这是对的！
     * 而不是在一个 test 方法里测试所有的场景
     * 我们这样写会更好定位到底哪里出现了问题！
     */
    @Test
    void rename_withValidName_updatesName() {
        User alice = new User("001", "Alice@gmail.com", "Alice");
        alice.rename("Bob");
        assertEquals("Bob", alice.getName());
    }

    @Test
    void rename_withNull_throwsException() {
        User alice = new User("001", "Alice@gmail.com", "Alice");
        assertThrows(IllegalArgumentException.class, () -> alice.rename(null));
        assertThrows(IllegalArgumentException.class, () -> alice.rename(" "));
    }




}
