package edu.rice.comp610.util;

import org.junit.jupiter.api.Test;

import java.rmi.server.UID;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    IUtil util = Util.getInstance();

    @Test
    void getInstance() {
        assertNotNull(util);
    }

    @Test
    void randomInt() {
        assertEquals(10, util.randomInt(10, 11));
    }

    @Test
    void getJsonParser() {
        assertNotNull(util.getJsonParser());
    }

    @Test
    void getRandomColor() {
        assertNotNull(util.getRandomColor());
    }
}