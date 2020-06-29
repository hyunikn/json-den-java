package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTest {

    /*
    @BeforeClass
    public static void before() {
        String s = Util.readFile("obj.json");
        System.out.println(s);
    }
     */

    @Test
    public void basicEquality() {
        JsonObj obj1 = new JsonObj();
        JsonObj obj2 = new JsonObj();
        JsonArr arr1 = new JsonArr();
        JsonArr arr2 = new JsonArr();
        JsonBool t1 = new JsonBool(true);
        JsonBool t2 = new JsonBool(true);
        JsonBool f1 = new JsonBool(false);
        JsonBool f2 = new JsonBool(false);
        JsonNull null1 = new JsonNull();
        JsonNull null2 = new JsonNull();
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
        String obj = Util.readFile("obj.json");
        Json json = Json.parse(obj);
        assertTrue(json.isObj());
        assertEquals(Util.readFile("r8_obj.json"), json.stringify(8));
        assertEquals(Util.readFile("r41_obj.json"), json.stringify(4, 1));
        assertEquals(Util.readFile("r00_obj.json"), json.toString());
    }

    @Test(expected=ParseError.class)
    public void parseError0() throws ParseError {
        String text = Util.readFile("wrong.json");
        try {
            Json json = Json.parse(text);
        } catch (ParseError e) {
            assertEquals(
                    Util.readFile("r_wrong.msg"),
                    String.format("parse error %d: %s", e.errCase, e.getMessage()));
            throw e;
        }
    }

    @Test(expected=ParseError.class)
    public void parseError1() throws ParseError {
        String text = Util.readFile("obj.json");
        try {
            Json json = JsonArr.parse(text);
        } catch (ParseError e) {
            assertEquals(
                    Util.readFile("r_err1.msg"),
                    String.format("parse error %d: %s", e.errCase, e.getMessage()));
            throw e;
        }
    }

    @Test(expected=ParseError.class)
    public void parseError2() throws ParseError {
        String text = Util.readFile("dupkeys.json");
        try {
            Json json = Json.parse(text);
        } catch (ParseError e) {
            //System.out.println(String.format("parse error %d: %s", e.errCase, e.getMessage()));
            assertEquals(
                    Util.readFile("r_err2.msg"),
                    String.format("parse error %d: %s", e.errCase, e.getMessage()));
            throw e;
        }
    }

    @Test
    public void commentLines() throws ParseError {
        String text = Util.readFile("comment.json");
        Json json = Json.parse(text);
        //System.out.println(json.stringify(4));
        System.out.println(json.stringify(0));
        assertEquals(Util.readFile("r4_comment.json"), json.stringify(4));
        assertEquals(Util.readFile("r0_comment.json"), json.stringify(0));
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
