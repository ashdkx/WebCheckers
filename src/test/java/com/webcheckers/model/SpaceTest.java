package com.webcheckers.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Nicholas Curl
 */
@Tag("Model-tier")
public class SpaceTest {

    private int cellIDx = 4;

    @Test
    public void ctor_valid(){
        new Space(4,null, true);
    }
    @Test
    public void ctor_invalid(){
        new Space(4,null, true);
    }

    @Test
    public void isValid(){
        final Space CuT1 = new Space(4,null, true);
        assertTrue(CuT1.isValid(),"Invalid Space");
        CuT1.setPiece(Piece.whiteSingle);
        assertFalse(CuT1.isValid(),"Space should not be valid");
        final Space CuT2 = new Space(4, null,false);
        assertFalse(CuT2.isValid(),"Space should not be valid");
        CuT2.setPiece(Piece.whiteSingle);
        assertFalse(CuT2.isValid(),"Space should not be valid");
    }

}
