package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

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
        assertEquals(Util.readFile("results/_8_obj.json"), json.stringify(8));
        assertEquals(Util.readFile("results/_41_obj.json"), json.stringify(4, 1));
        assertEquals(Util.readFile("results/_00_obj.json"), json.toString());
    }

    @Test(expected=ParseError.class)
    public void parseError0() throws ParseError {
        String text = Util.readFile("wrong.json");
        try {
            Json json = Json.parse(text);
        } catch (ParseError e) {
            assertEquals(
                    Util.readFile("results/_wrong.msg"),
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
                    Util.readFile("results/_err1.msg"),
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
                    Util.readFile("results/_err2.msg"),
                    String.format("parse error %d: %s", e.errCase, e.getMessage()));
            throw e;
        }
    }

    @Test
    public void commentLines() throws ParseError {
        String text = Util.readFile("comment.json");
        Json json = Json.parse(text);
        //System.out.println(json.stringify(4));
        //System.out.println(json.stringify(0));
        assertEquals(Util.readFile("results/_4_comment.json"), json.stringify(4));
        assertEquals(Util.readFile("results/_0_comment.json"), json.stringify(0));
    }

    @Test
    public void testClone() throws ParseError {
        Json o = Json.parse(Util.readFile("obj.json"));
        Json a = Json.parse(Util.readFile("arr.json"));
        Json bt = Json.parse(Util.readFile("bool-true.json"));
        Json bf = Json.parse(Util.readFile("bool-false.json"));
        Json nl = Json.parse(Util.readFile("null.json"));
        Json nmi = Json.parse(Util.readFile("num-int.json"));
        Json nmf = Json.parse(Util.readFile("num-float.json"));
        Json nme = Json.parse(Util.readFile("num-exp.json"));
        Json s = Json.parse(Util.readFile("str.json"));

        assertEquals(o, o.clone());
        assertEquals(a, a.clone());
        assertEquals(bt, bt.clone());
        assertEquals(bf, bf.clone());
        assertEquals(nl, nl.clone());
        assertEquals(nmi, nmi.clone());
        assertEquals(nmf, nmf.clone());
        assertEquals(nme, nme.clone());
        assertEquals(s, s.clone());

        assertEquals(o.hashCode(), o.clone().hashCode());
        assertEquals(a.hashCode(), a.clone().hashCode());
        assertEquals(bt.hashCode(), bt.clone().hashCode());
        assertEquals(bf.hashCode(), bf.clone().hashCode());
        assertEquals(nl.hashCode(), nl.clone().hashCode());
        assertEquals(nmi.hashCode(), nmi.clone().hashCode());
        assertEquals(nmf.hashCode(), nmf.clone().hashCode());
        assertEquals(nme.hashCode(), nme.clone().hashCode());
        assertEquals(s.hashCode(), s.clone().hashCode());

        assertEquals(o.stringify(0), ((Json) o.clone()).stringify(0));
        assertEquals(a.stringify(0), ((Json) a.clone()).stringify(0));
        assertEquals(bt.stringify(0), ((Json) bt.clone()).stringify(0));
        assertEquals(bf.stringify(0), ((Json) bf.clone()).stringify(0));
        assertEquals(nl.stringify(0), ((Json) nl.clone()).stringify(0));
        assertEquals(nmi.stringify(0), ((Json) nmi.clone()).stringify(0));
        assertEquals(nmf.stringify(0), ((Json) nmf.clone()).stringify(0));
        assertEquals(nme.stringify(0), ((Json) nme.clone()).stringify(0));
        assertEquals(s.stringify(0), ((Json) s.clone()).stringify(0));

        assertEquals(o.stringify(4), ((Json) o.clone()).stringify(4));
        assertEquals(a.stringify(4), ((Json) a.clone()).stringify(4));
        assertEquals(bt.stringify(4), ((Json) bt.clone()).stringify(4));
        assertEquals(bf.stringify(4), ((Json) bf.clone()).stringify(4));
        assertEquals(nl.stringify(4), ((Json) nl.clone()).stringify(4));
        assertEquals(nmi.stringify(4), ((Json) nmi.clone()).stringify(4));
        assertEquals(nmf.stringify(4), ((Json) nmf.clone()).stringify(4));
        assertEquals(nme.stringify(4), ((Json) nme.clone()).stringify(4));
        assertEquals(s.stringify(4), ((Json) s.clone()).stringify(4));

        assertEquals(o.stringify(4,1), ((Json) o.clone()).stringify(4,1));
        assertEquals(a.stringify(4,1), ((Json) a.clone()).stringify(4,1));
        assertEquals(bt.stringify(4,1), ((Json) bt.clone()).stringify(4,1));
        assertEquals(bf.stringify(4,1), ((Json) bf.clone()).stringify(4,1));
        assertEquals(nl.stringify(4,1), ((Json) nl.clone()).stringify(4,1));
        assertEquals(nmi.stringify(4,1), ((Json) nmi.clone()).stringify(4,1));
        assertEquals(nmf.stringify(4,1), ((Json) nmf.clone()).stringify(4,1));
        assertEquals(nme.stringify(4,1), ((Json) nme.clone()).stringify(4,1));
        assertEquals(s.stringify(4,1), ((Json) s.clone()).stringify(4,1));
    }

    @Test
    public void getx() throws ParseError {
        Json nested = Json.parse(Util.readFile("nested.json"));
        assertEquals(new JsonStr("merong"), nested.getx("0.a.0.b.c"));
    }

    @Test
    public void has() throws ParseError {
        Json nested = Json.parse(Util.readFile("nested.json"));
        assertTrue(nested.has("0.a.0.b.c"));
        assertFalse(nested.has("0.a.1.b.c"));
    }

    @Test
    public void longestReachablePrefix() throws ParseError {
        Json nested = Json.parse(Util.readFile("nested.json"));
        assertEquals("0.a.0", nested.longestReachablePrefix("0.a.0.d.e"));
    }
}
