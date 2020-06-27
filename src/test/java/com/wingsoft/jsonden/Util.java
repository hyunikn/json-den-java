package com.wingsoft.jsonden;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Util {

    private static final String RESOURCE_DIR = "src/test/resource/";

    public static String readFile(String fileName) {
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
