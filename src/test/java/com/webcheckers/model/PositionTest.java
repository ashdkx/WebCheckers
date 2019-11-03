package com.webcheckers.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    @Test
    public void checkPos() {
        Position pos = new Position(4, 5);
        assertEquals(4, pos.getRow(), "Row is" + pos.getRow());
        assertEquals(5, pos.getCell(), "Row is" + pos.getCell());
    }

}