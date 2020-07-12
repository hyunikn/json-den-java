package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;
import com.github.hyunikn.jsonden.exception.UnreachablePath;

import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNonLeafTest {

    @Test
    public void getx() throws ParseError {
        JsonArr nested = JsonArr.parse(Util.readFile("nested.json"));
        assertEquals(JsonStr.instance("merong"), nested.getx("#0.a.#0.b.c"));
    }

    @Test
    public void has() throws ParseError {
        JsonArr nested = JsonArr.parse(Util.readFile("nested.json"));
        assertTrue(nested.has("#0.a.#0.b.c"));
        assertFalse(nested.has("#0.a.#1.b.c"));
    }

    @Test
    public void longestReachablePrefix() throws ParseError {
        JsonArr nested = JsonArr.parse(Util.readFile("nested.json"));
        assertEquals("#0.a.#0", nested.longestReachablePrefix("#0.a.#0.d.e"));
    }

    @Test
    public void xMethods() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        JsonObj r = JsonObj.parse(Util.readFile("results/_x_methods.json"));
        assertEquals(j.getx("how.#0.deep.#0.is.#0.your.#0.love.#1").asBool().getBoolean(), false);
        assertEquals(j.getx("how.#0.deep.#0.is.#0.your.#0.love.#2"), JsonNull.instance());
        assertEquals(j.getx("how.#0.deep.#0.is.#0.your.#0.love.#4").asNum().getDouble(), 1.1, 0.0);

        j.setx("how.#0.deep.#0.is.#1.your", "love");
        j.setx("how.#0.deep.#0.is.#2.your", "love");
        j.setx("how.#0.deep.#0.is.#0.your.#0.love.#1", JsonObj.instance());   // replace
        j.setx("how.#0.deep.#0.is.#0.your.#0.love.#1.how.deep", JsonObj.instance());
        //System.out.println(j.toString());
        assertEquals(r, j);
    }

    @Test(expected=UnreachablePath.class)
    public void setxErr0() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#0.is.#0.your.#0.love.love", 0);
    }

    @Test(expected=UnreachablePath.class)
    public void setxErr1() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#0.is.#0.your.#0.love.#8", 0);
    }

    @Test(expected=UnreachablePath.class)
    public void setxErr2() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#0.is.#0.your.#0.love.#0.love", 0);
    }

    @Test(expected=UnreachablePath.class)
    public void setxErr3() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#0.is.#0.your.#0.love.#0.love.love", 0);
    }

    @Test(expected=UnreachablePath.class)
    public void setxErr4() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#1.is.#1.your.#0.love", 0);
    }

    @Test(expected=UnreachablePath.class)
    public void setxErr5() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.is.your.love", 0);
    }

    @Test(expected=UnreachablePath.class)
    public void setxErr6() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#2.is.your.love", 0);
    }

    @Test
    public void flatten() throws ParseError {
        JsonObj j = JsonObj.parse(Util.readFile("flatten.json"));
        //System.out.println(Jsons.prettyPrintFlattened(j.flatten()));
        assertEquals(Util.readFile("results/_flatten.out"), Jsons.prettyPrintFlattened(j.flatten()));
        //System.out.println(Jsons.prettyPrintFlattened(j.flatten2()));
        assertEquals(Util.readFile("results/_flatten2.out"), Jsons.prettyPrintFlattened(j.flatten2()));
    }

    @Test
    public void compareObjects() throws ParseError {
        JsonObj j1, j2;
        j1 = JsonObj.parse(Util.readFile("compare1.json"));
        j2 = JsonObj.parse(Util.readFile("compare2.json"));

        LinkedHashMap<String, List<Json>> d0 = j1.diffAtCommonPaths(j2);
        //System.out.println(Jsons.prettyPrintDiff(d0));
        assertEquals(Util.readFile("results/_diffAt0.out"), Jsons.prettyPrintDiff(d0));
        LinkedHashMap<String, List<Json>> d1 = j1.diffAtCommonPaths(j1);
        //System.out.println(Jsons.prettyPrintDiff(d1));
        assertEquals("", Jsons.prettyPrintDiff(d1));

        LinkedHashMap<String, Json> c0 = j1.intersect(j2);
        //System.out.println(Jsons.prettyPrintFlattened(c0));
        assertEquals(Util.readFile("results/_intersect0.out"), Jsons.prettyPrintFlattened(c0));
        LinkedHashMap<String, Json> c1 = j1.intersect(j1);
        //System.out.println(Jsons.prettyPrintFlattened(c1));
        assertEquals(Util.readFile("results/_intersect1.out"), Jsons.prettyPrintFlattened(c1));

        LinkedHashMap<String, Json> s0 = j1.subtract(j2);
        //System.out.println(Jsons.prettyPrintFlattened(s0));
        assertEquals(Util.readFile("results/_subtract0.out"), Jsons.prettyPrintFlattened(s0));
        LinkedHashMap<String, Json> s1 = j1.subtract(j1);
        //System.out.println(Jsons.prettyPrintFlattened(s1));
        assertEquals("", Jsons.prettyPrintFlattened(s1));
        LinkedHashMap<String, Json> s2 = j2.subtract(j1);
        //System.out.println(Jsons.prettyPrintFlattened(s2));
        assertEquals(Util.readFile("results/_subtract2.out"), Jsons.prettyPrintFlattened(s2));

        c0 = j1.intersectLeaves(j2);
        //System.out.println(Jsons.prettyPrintFlattened(c0));
        assertEquals(Util.readFile("results/_intersectLeaves0.out"), Jsons.prettyPrintFlattened(c0));
        c1 = j2.intersectLeaves(j2);
        //System.out.println(Jsons.prettyPrintFlattened(c1));
        assertEquals(Util.readFile("results/_intersectLeaves1.out"), Jsons.prettyPrintFlattened(c1));

        s0 = j1.subtractLeaves(j2);
        //System.out.println(Jsons.prettyPrintFlattened(s0));
        assertEquals(Util.readFile("results/_subtractLeaves0.out"), Jsons.prettyPrintFlattened(s0));
        s1 = j1.subtractLeaves(j1);
        //System.out.println(Jsons.prettyPrintFlattened(s1));
        assertEquals("", Jsons.prettyPrintFlattened(s1));
        s2 = j2.subtractLeaves(j1);
        //System.out.println(Jsons.prettyPrintFlattened(s2));
        assertEquals(Util.readFile("results/_subtractLeaves2.out"), Jsons.prettyPrintFlattened(s2));
    }
}
