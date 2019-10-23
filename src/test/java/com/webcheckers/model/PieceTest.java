package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class PieceTest {

    @Test
    void getType() {
        Piece.type expectedPiece = Piece.type.SINGLE;

        Piece piece = new Piece(Piece.type.SINGLE, Piece.color.RED);

        Piece.type actualPiece = piece.getType();

        assertEquals(expectedPiece, actualPiece, "Default Piece Type should be: " + expectedPiece);
    }

    @Test
    void getColor() {
        Piece.color expectedCol = Piece.color.RED;

        Piece piece = new Piece(Piece.type.SINGLE, expectedCol);

        Piece.color actualCol = piece.getColor();

        assertEquals(expectedCol, actualCol, "Default color should be: " + expectedCol);
    }
}