package com.wingsoft.jsonden;

import java.io.IOException;

public class Main {
    public static void main(String[] argv) throws IOException {
        Json.parse("{\"a\": 1, \"b\": 1.1, \"c\": \"a\", \"d\": true, \"e\": false, \"f\": [1,2,3]}");
    }
}
