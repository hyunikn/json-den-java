package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class ObjTest {

    @Test
    public void test() throws ParseError {
        JsonObj empty0 = new JsonObj();
        JsonObj empty1 = JsonObj.parse("{}");
        assertEquals(empty0, empty1);
        assertEquals(empty0.hashCode(), empty1.hashCode());
        assertEquals(empty0.clone(), empty1.clone());

        LinkedHashMap<String, Json> map = new LinkedHashMap<>();
        JsonArr arr = new JsonArr(Arrays.asList(new JsonNum(1), new JsonNum(1), new JsonNum(1)));
        map.put("a", arr);
        map.put("o", empty0);
        map.put("bt", new JsonBool(true));
        map.put("bf", new JsonBool(false));
        map.put("null", new JsonNull());
        map.put("num-int", new JsonNum(3));
        map.put("num-float", new JsonNum(3.33));
        map.put("num-exp", new JsonNum(3.33e10));
        map.put("str", new JsonStr("hello"));
        JsonObj obj0 = new JsonObj(map);
        JsonObj obj1 = JsonObj.parse(
                "{\"a\": [1, 1, 1], \"o\": {}, \"bt\": true, \"bf\": false, \"null\": null, " +
                "\"num-int\": 3, \"num-float\": 3.33, \"num-exp\": 3.33e10, \"str\": \"hello\"}");
        assertEquals(obj0, obj1);
        assertEquals(obj0.hashCode(), obj1.hashCode());
        assertEquals(obj0.clone(), obj1.clone());

        assertEquals(JsonArr.parse("[ 1, 1, 1 ]"), obj0.remove("a"));
        assertEquals(null, obj0.remove("a"));
        assertEquals(empty1, obj0.get("o"));
        assertEquals(empty1, obj0.get("o"));
        obj0.set("a", arr);
        assertEquals(obj0, obj1);
        assertEquals(map, obj1.getMap());
        assertEquals(obj0.keySet(),
                new HashSet(Arrays.asList("a", "o", "bt", "bf", "null", "num-int", "num-float", "num-exp", "str")));
        assertTrue(obj0.isObj() && obj1.isObj());
        JsonObj obj2 = obj0.asObj();
        assertTrue(obj2.isObj());
        obj0.clear();
        assertEquals(empty0, obj0);
    }
}
