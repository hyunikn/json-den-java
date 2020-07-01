package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import org.junit.Test;
import static org.junit.Assert.*;

public class NullTest {

    @Test
    public void test() throws ParseError {
        JsonNull nul = new JsonNull();
        assertEquals(nul, JsonNull.parse("null"));
        assertEquals(nul.hashCode(), JsonNull.parse("null").hashCode());
        assertEquals(nul.clone(), JsonNull.parse("null").clone());
        assertEquals(nul, nul.clone());
        assertTrue(nul.isNull());
        assertEquals(nul, nul.asNull());
    }
}
