package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;

public class ArrTest {

    @Test
    public void test() throws ParseError {
        JsonArr empty0 = new JsonArr();
        JsonArr empty1 = JsonArr.parse("[]");
        assertEquals(empty0, empty1);
        assertEquals(empty0.hashCode(), empty1.hashCode());
        assertEquals(empty0.clone(), empty1.clone());

        JsonArr arr0 = new JsonArr(Arrays.asList(
                    new JsonArr(Arrays.asList(new JsonNum(1), new JsonNum(1), new JsonNum(1))),
                    new JsonArr(Arrays.asList(new JsonNum(2), new JsonNum(2), new JsonNum(2))),
                    new JsonArr(Arrays.asList(new JsonNum(3), new JsonNum(3), new JsonNum(3)))
                ));
        JsonArr arr1 = JsonArr.parse("[ [ 1, 1, 1 ], [ 2, 2, 2 ], [ 3, 3, 3 ] ]");
        assertEquals(arr0, arr1);
        assertEquals(arr0.hashCode(), arr1.hashCode());
        assertEquals(arr0.clone(), arr1.clone());

        assertEquals(new JsonArr(Arrays.asList(new JsonNum(2), new JsonNum(2), new JsonNum(2))), arr0.get(1));
        assertEquals(JsonArr.parse("[ 2, 2, 2 ]"), arr0.delete(1));
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], [ 3, 3, 3 ] ]"), arr0);
        assertEquals(JsonArr.parse("[ 3, 3, 3 ]"), arr0.replace(1, new JsonStr("hello")));
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], \"hello\" ]"), arr0);
        arr0.insert(1, new JsonNum(3));
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], 3, \"hello\" ]"), arr0);
        arr0.append(new JsonNull());
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], 3, \"hello\", null ]"), arr0);
        arr0.append(new JsonBool(true));
        assertEquals(JsonArr.parse("[ [ 1, 1, 1 ], 3, \"hello\", null, true ]"), arr0);
        arr0.append(new JsonBool(false));
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
}
