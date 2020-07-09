package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;

public class ArrTest {

    @Test
    public void test() throws ParseError {
        JsonArr empty0 = JsonArr.instance();
        JsonArr empty1 = JsonArr.parse("[]");
        assertEquals(empty0, empty1);
        assertEquals(empty0.hashCode(), empty1.hashCode());
        assertEquals(empty0.clone(), empty1.clone());

        JsonArr arr0 = JsonArr.instance(Arrays.asList(
                    JsonArr.instance(Arrays.asList(JsonNum.instance(1), JsonNum.instance(1), JsonNum.instance(1))),
                    JsonArr.instance(Arrays.asList(JsonNum.instance(2), JsonNum.instance(2), JsonNum.instance(2))),
                    JsonArr.instance(Arrays.asList(JsonNum.instance(3), JsonNum.instance(3), JsonNum.instance(3)))
                ));
        JsonArr arr1 = JsonArr.parse("[ [ 1, 1, 1 ], [ 2, 2, 2 ], [ 3, 3, 3 ] ]");
        assertEquals(arr0, arr1);
        assertEquals(arr0.hashCode(), arr1.hashCode());
        assertEquals(arr0.clone(), arr1.clone());

        assertEquals(JsonArr.instance(Arrays.asList(JsonNum.instance(2), JsonNum.instance(2), JsonNum.instance(2))), arr0.get(1));
        assertEquals(JsonArr.parse("[ 2, 2, 2 ]"), arr0.remove(1));
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], [ 3, 3, 3 ] ]"), arr0);
        assertEquals(JsonArr.parse("[ 3, 3, 3 ]"), arr0.get(1));
        arr0.replace(1, JsonStr.instance("hello"));
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], \"hello\" ]"), arr0);
        arr0.insert(1, JsonNum.instance(3));
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], 3, \"hello\" ]"), arr0);
        arr0.append(JsonNull.instance());
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], 3, \"hello\", null ]"), arr0);
        arr0.append(JsonBool.instance(true));
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], 3, \"hello\", null, true ]"), arr0);
        arr0.append(JsonBool.instance(false));
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], 3, \"hello\", null, true, false ]"), arr0);

        assertEquals(6, arr0.size());
        arr0.clear();
        assertEquals(0, arr0.size());

        assertEquals(1, arr1.indexOf(Json.parse("[2, 2, 2]")));
        assertEquals(2, arr1.lastIndexOf(Json.parse("[3, 3, 3]")));
        assertEquals(-1, arr1.lastIndexOf(Json.parse("[null, null, null]")));

        assertTrue(arr1.isArr());
        JsonArr arr2 = arr1.asArr();
        assertEquals(arr2.getList(), JsonArr.parse("[ [ 1, 1, 1 ], [ 2, 2, 2 ], [ 3, 3, 3 ] ]").getList());
    }

    @Test
    public void methodChain() throws ParseError {
        JsonArr arr = JsonArr.instance();
        arr.append(JsonNum.instance(1000))
            .clear()
            .append(JsonNum.instance(0))
            .append(JsonNum.instance(1))
            .append(JsonNum.instance(2))
            .append(JsonNum.instance(3))
            .append(JsonNum.instance(4))
            .append(JsonNum.instance(5))
            .insert(-1, JsonNum.instance(4.5))
            .insert(-3, JsonNum.instance(3.5))
            .insert(-5, JsonNum.instance(2.5))
            .insert(-7, JsonNum.instance(1.5))
            .insert(-9, JsonNum.instance(0.5));
        //System.out.println(arr.toString());
        assertEquals(JsonArr.parse(Util.readFile("results/_arr_method_chain.json")), arr);

        arr.replace(1, JsonNum.instance(0.6))
            .replace(3, JsonNum.instance(1.6))
            .replace(5, JsonNum.instance(2.6))
            .replace(7, JsonNum.instance(3.6))
            .replace(9, JsonNum.instance(4.6));
        assertEquals(JsonArr.parse(Util.readFile("results/_arr_method_chain2.json")), arr);
    }
}
