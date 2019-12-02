package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RowTest {
    private Row row;
    private Piece piece;

    @BeforeEach
    public void setup() {
        piece = Piece.redSingle;
        row = new Row(4, piece, true);
    }

    @Test
    void iterator() {
    }

    @Test
    void getIndex() {
        assertEquals(4, row.getIndex());
    }

    @Test
    void getSpace() {
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                assertEquals(piece, row.getSpace(i).getPiece());
            } else {
                assertEquals(null, row.getSpace(i).getPiece());
            }
        }
    }
}