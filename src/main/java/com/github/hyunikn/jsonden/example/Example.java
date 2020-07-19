package com.github.hyunikn.jsonden.example;

import com.github.hyunikn.jsonden.*;
import com.github.hyunikn.jsonden.exception.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;

public class Example {

    public static void main(String[] argv) throws ParseError, UnreachablePath {

		String leftText = readFile("left.json");
		String rightText = readFile("right.json");

        // parse
        JsonObj left = JsonObj.parse(leftText);
        JsonObj right = JsonObj.parse(rightText);

        // stringify
        System.out.println("# stringify with indent size 0");
        System.out.println(left.stringify(0));
        System.out.println("# stringify with indent size 2");
        System.out.println(left.stringify(2));

        // diff
        System.out.print("# diff");
        System.out.println(Jsons.prettyPrintDiff(left.diff(right)));

        // intersect, subtract and merge
        System.out.println("# intersect");
        System.out.println(Jsons.intersect(left, right).stringify(StrOpt.VALUES_ONLY));
        System.out.println("# subtract");
        System.out.println(Jsons.subtract(left, right).stringify(StrOpt.VALUES_ONLY));
        System.out.println("# merge");
        System.out.println(Jsons.merge(left, right).stringify(StrOpt.VALUES_ONLY));

        // flatten and loadFlattened
        System.out.println("# flatten");
        LinkedHashMap<String, Json> flattened = left.flatten();
        System.out.println(Jsons.prettyPrintFlattened(flattened));
        JsonObj restored = JsonObj.instance().loadFlattened(flattened);
        if (left.equals(restored)) {
            System.out.println("# the original and the restored are equal");
        } else {
            assert false;
        }
    }


    private static final String RESOURCE_DIR = "src/main/resource/example/";

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
