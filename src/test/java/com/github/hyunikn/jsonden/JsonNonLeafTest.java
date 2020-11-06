package io.github.hyunikn.jsonden;

import io.github.hyunikn.jsonden.exception.ParseError;
import io.github.hyunikn.jsonden.exception.UnreachablePath;

import java.util.Map;
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

    @Test
    public void setxBigArrIdx0() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#0.is.#0.your.#0.love.#11", 0);
        //System.out.println(j.stringify(4));
        assertEquals(Util.readFile("results/_big_arr_idx0.json"), j.stringify(4));
    }
    @Test
    public void setxBigArrIdx1() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#1.is.#1.your.#0.love", 0);
        //System.out.println(j.stringify(4));
        assertEquals(Util.readFile("results/_big_arr_idx1.json"), j.stringify(4));
    }
    @Test
    public void setxBigArrIdx2() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#2.is.your.love", 0);
        //System.out.println(j.stringify(4));
        assertEquals(Util.readFile("results/_big_arr_idx2.json"), j.stringify(4));
    }



    @Test(expected=UnreachablePath.class)
    public void setxErr0() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.#0.is.#0.your.#0.love.love", 0);
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
    public void setxErr5() throws ParseError, UnreachablePath {
        JsonObj j = JsonObj.parse(Util.readFile("x_methods.json"));
        j.setx("how.#0.deep.is.your.love", 0);
    }

    @Test
    public void flatten() throws ParseError {
        JsonObj j = JsonObj.parse(Util.readFile("flatten.json"));
        //System.out.println(Jsons.prettyPrintFlattened(j.flatten()));
        assertEquals(Util.readFile("results/_flatten.out"), Jsons.prettyPrintFlattened(j.flatten()));
    }

    @Test
    public void compareObjects() throws ParseError, UnreachablePath {
        JsonObj j1, j2;
        j1 = JsonObj.parse(Util.readFile("compare11.json"));
        j2 = JsonObj.parse(Util.readFile("compare22.json"));

        Map<String, List<Json>> d0 = j1.diffAtCommonPaths(j2);
        //System.out.println(Jsons.prettyPrintDiff(d0));
        assertEquals(Util.readFile("results/_diffAt0.out"), Jsons.prettyPrintDiff(d0));
        Map<String, List<Json>> d1 = j1.diffAtCommonPaths(j1);
        //System.out.println(Jsons.prettyPrintDiff(d1));
        assertEquals("", Jsons.prettyPrintDiff(d1));

        Map<String, List<Json>> d2 = j1.diff(j2);
        //System.out.println(Jsons.prettyPrintDiff(d2));
        assertEquals(Util.readFile("results/_diff00.out"), Jsons.prettyPrintDiff(d2));
        Map<String, List<Json>> d3 = j1.diff(j1);
        assertEquals("", Jsons.prettyPrintDiff(d3));

        JsonObj c0 = (JsonObj) j1.clone();
        c0.intersect(j2);
        //System.out.println(c0.stringify(4));
        assertEquals(Util.readFile("results/_intersect0.out"), c0.stringify(4));

        JsonObj c1 = (JsonObj) j1.clone();
        c1.intersect(j1);
        //System.out.println(c1.stringify(4));
        assertEquals(c1, j1);

        JsonObj s0 = (JsonObj) j1.clone();
        s0.subtract(j2);
        //System.out.println(s0.stringify(4));
        assertEquals(Util.readFile("results/_subtract0.out"), s0.stringify(4));

        JsonObj s1 = (JsonObj) j1.clone();
        s1.subtract(j1);
        //System.out.println(s1.stringify(4));
        assertEquals(JsonObj.instance(), s1);

        JsonObj s2 = (JsonObj) j2.clone();
        s2.subtract(j1);
        //System.out.println(s2.stringify(4));
        assertEquals(Util.readFile("results/_subtract2.out"), s2.stringify(4));

        JsonObj m0 = (JsonObj) j1.clone();
        m0.merge(j2);
        //System.out.println(m0.stringify(4));
        assertEquals(Util.readFile("results/_merge0.out"), m0.stringify(4));

        JsonObj m1 = (JsonObj) j2.clone();
        m1.merge(j1);
        //System.out.println(m1.stringify(4));
        assertEquals(Util.readFile("results/_merge1.out"), m1.stringify(4));
    }
}
