package io.github.hyunikn.jsonden;

import io.github.hyunikn.jsonden.exception.ParseError;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumTest {

    @Test
    public void test() throws ParseError {
        JsonNum i = JsonNum.instance(3);
        JsonNum f = JsonNum.instance(3.33);
        JsonNum e = JsonNum.instance(3.33e10);
        assertEquals(i, JsonNum.parse("3"));
        assertEquals(f, JsonNum.parse("3.33"));
        assertEquals(e, JsonNum.parse("3.33e10"));
        assertEquals(i.hashCode(), JsonNum.parse("3").hashCode());
        assertEquals(f.hashCode(), JsonNum.parse("3.33").hashCode());
        assertEquals(e.hashCode(), JsonNum.parse("3.33e10").hashCode());
        assertEquals(i.clone(), JsonNum.parse("3").clone());
        assertEquals(f.clone(), JsonNum.parse("3.33").clone());
        assertEquals(e.clone(), JsonNum.parse("3.33e10").clone());
        assertEquals(i, i.clone());
        assertEquals(f, f.clone());
        assertEquals(e, e.clone());
        assertTrue(i.isNum());
        assertTrue(f.isNum());
        assertTrue(e.isNum());
        assertEquals(i, i.asNum());
        assertEquals(f, f.asNum());
        assertEquals(e, e.asNum());
        assertTrue(i.getInt() == 3);
        assertTrue(f.getFloat() == 3.33f);
        assertTrue(e.getDouble() == 3.33e10);
    }
}
