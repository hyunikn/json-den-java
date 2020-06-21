package com.wingsoft.jsonden;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class MainTest {

    @Test
    public void basicEquality() {
        assertEquals(new JsonObj(), new JsonObj());
        assertEquals(new JsonArr(), new JsonArr());
        assertEquals(JsonBool.lookup(true), JsonBool.lookup(true));
        assertEquals(JsonBool.lookup(false), JsonBool.lookup(false));
        assertEquals(JsonNull.lookup(), JsonNull.lookup());
        assertEquals(new JsonNum(1.1), new JsonNum(1.1));
        assertEquals(new JsonStr("a"), new JsonStr("a"));

        assertEquals(new JsonObj().hashCode(), new JsonObj().hashCode(), 0);
        assertEquals(new JsonArr().hashCode(), new JsonArr().hashCode(), 0);
        assertEquals(JsonBool.lookup(true).hashCode(), JsonBool.lookup(true).hashCode(), 0);
        assertEquals(JsonBool.lookup(false).hashCode(), JsonBool.lookup(false).hashCode(), 0);
        assertEquals(JsonNull.lookup().hashCode(), JsonNull.lookup().hashCode(), 0);
        assertEquals(new JsonNum(1.1).hashCode(), new JsonNum(1.1).hashCode(), 0);
        assertEquals(new JsonStr("a").hashCode(), new JsonStr("a").hashCode(), 0);
    }
}
