package com.wingsoft.jsonden;

import com.wingsoft.jsonden.exception.ParseError;

import java.io.IOException;

public class Main {
    public static void main(String[] argv) throws ParseError {
        Json parsed = Json.parse(
                "{\"a\": 1 /** \"b\": 1.1,\n one two three\n hello mister monkey*/ \"c\": \"a\", \"d\": true, \"e\": false, \"f\": [1,2,3]}");

        if (parsed != null) {
            System.out.println(parsed.stringify(4));
            System.out.println(parsed.stringify(4, 1));
            System.out.println(parsed.stringify(0));
        }
    }
}
