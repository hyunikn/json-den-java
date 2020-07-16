package com.github.hyunikn.jsonden.example;

import com.github.hyunikn.jsonden.*;
import com.github.hyunikn.jsonden.exception.*;

public class Example {
    public static void main(String[] argv) {
    }


    private static final String RESOURCE_DIR = "src/main/resource/example";

    private static String readFile(String fileName) {
        File f = new File(RESOURCE_DIR + fileName);
        if (f.isFile()) {
            byte[] buf = new byte[(int) f.length() - 1];    // -1: vim auto-saves a newline character.
            try {
                new FileInputStream(f).read(buf);
            } catch (Throwable e) {
                throw new Error(e);
            }
            return new String(buf);
        } else {
            throw new Error(RESOURCE_DIR + fileName + " is not a file");
        }
    }

}
