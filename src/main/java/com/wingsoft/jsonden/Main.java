package com.wingsoft.jsonden;

import java.io.IOException;

public class Main {
    public static void main(String[] argv) throws IOException {
        Json parsed = Json.parse(
                "{\"a\": 1, \"b\": 1.1, \"c\": \"a\", \"d\": true, \"e\": false, \"f\": [1,2,3]}");
        System.out.println(parsed.stringify(2, 0));
        System.out.println(parsed.stringify(0, 0));
    }
}
