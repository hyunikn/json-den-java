package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import org.junit.Test;
import static org.junit.Assert.*;

public class BoolTest {

    @Test
    public void test() throws ParseError {
        JsonBool bt = new JsonBool(true);
        JsonBool bf = new JsonBool(false);
        assertEquals(bt, JsonBool.parse("true"));
        assertEquals(bf, JsonBool.parse("false"));
        assertEquals(bt.hashCode(), JsonBool.parse("true").hashCode());
        assertEquals(bf.hashCode(), JsonBool.parse("false").hashCode());
        assertEquals(bt, bt.clone());
        assertEquals(bf, bf.clone());
        assertTrue(bt.isBool() && bf.isBool()); 
        assertEquals(bt, bt.asBool());
        assertTrue(bt.getBoolean());
        assertTrue(!bf.getBoolean());
    }
}
