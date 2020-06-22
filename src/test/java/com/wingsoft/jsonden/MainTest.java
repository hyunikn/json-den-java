package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    /*
    @BeforeClass
    public static void before() {
        String s = Util.readJSON("obj.json");
        System.out.println(s);
    }
     */

    @Test
    public void basicEquality() {
        JsonObj obj1 = new JsonObj();
        JsonObj obj2 = new JsonObj();
        JsonArr arr1 = new JsonArr();
        JsonArr arr2 = new JsonArr();
        JsonBool t1 = JsonBool.lookup(true);
        JsonBool t2 = JsonBool.lookup(true);
        JsonBool f1 = JsonBool.lookup(false);
        JsonBool f2 = JsonBool.lookup(false);
        JsonNull null1 = JsonNull.lookup();
        JsonNull null2 = JsonNull.lookup();
        JsonNum num1 = new JsonNum(1.1);
        JsonNum num2 = new JsonNum(1.1);
        JsonStr s1 = new JsonStr("abc");
        JsonStr s2 = new JsonStr("abc");

        assertEquals(obj1, obj2);
        assertEquals(arr1, arr2);
        assertEquals(t1, t2);
        assertEquals(f1, f2);
        assertEquals(null1, null2);
        assertEquals(num1, num2);
        assertEquals(s1, s2);

        assertEquals(obj1.hashCode(), obj2.hashCode());
        assertEquals(arr1, arr2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertEquals(f1.hashCode(), f2.hashCode());
        assertEquals(null1.hashCode(), null2.hashCode());
        assertEquals(num1.hashCode(), num2.hashCode());
        assertEquals(s1.hashCode(), s2.hashCode());

        obj2.set("a", null1);
        arr2.append(null1);

        assertFalse(obj1.equals(obj2));
        assertFalse(arr1.equals(arr2));
        assertFalse(t1.equals(f1));
        assertFalse(num1.equals(new JsonNum(1.2)));
        assertFalse(s1.equals(new JsonStr("cde")));
    }

    @Test
    public void parseAndStringify() throws ParseError {
        String text = Util.readJSON("obj.json");
        Json json = Json.parse(text);
        assertTrue(json.isObj());
        System.out.println(json.stringify(8));
        System.out.println(json.stringify(4, 1));
        System.out.println(json.toString());
    }

    @Test(expected=ParseError.class)
    public void parseError0() throws ParseError {
        String text = Util.readJSON("wrong.json");
        try {
            Json json = Json.parse(text);
        } catch (ParseError e) {
            System.out.println(String.format("parse error %d: %s", e.errCase, e.getMessage()));
            throw e;
        }
    }

    @Test
    public void parseError1() {
    }

    @Test
    public void parseError2() {
    }

    @Test
    public void commentLines() {
    }

    @Test
    public void testClone() {
    }

    @Test
    public void has() {
    }

    @Test
    public void longestReachablePrefix() {
    }
}
