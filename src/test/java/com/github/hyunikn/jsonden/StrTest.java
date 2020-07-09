package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import org.junit.Test;
import static org.junit.Assert.*;

public class StrTest {

    @Test
    public void construct() throws ParseError {
        JsonStr s = JsonStr.instance("hello");
        assertEquals(s, JsonStr.parse("\"hello\""));
        assertEquals(s.hashCode(), JsonStr.parse("\"hello\"").hashCode());
        assertEquals(s.clone(), JsonStr.parse("\"hello\"").clone());
        assertTrue(s.isStr());
        assertEquals(s, s.asStr());
        assertEquals(s.getString(), "hello");
    }
}
