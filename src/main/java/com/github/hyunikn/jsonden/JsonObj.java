package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;

import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
  * A subclass of {@link com.github.hyunikn.jsonden.Json Json} which represents JSON objects.
  * It remembers the order of member addition, and prints them in that order when stringified.
  */
public class JsonObj extends Json {

    // ===================================================
    // Public

    /**
      * Constructs an empty {@code JsonObj}.
      */
    public JsonObj() {
        map = new LinkedHashMap<>();
    }

    /**
      * Constructs a {@code JsonObj} from a map.
      */
    public JsonObj(LinkedHashMap<String, Json> map) {
        if (map == null) {
            throw new IllegalArgumentException("source map cannot be null");
        }
        this.map = new LinkedHashMap<>(map);
    }

    /**
      * Checks the value (not reference) equality
      */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        JsonObj that = (JsonObj) o;
        return that.map.equals(this.map);
    }

    /**
      * Gets the hash code.
      */
    @Override
    public int hashCode() {
        return map.hashCode();
    }

    /**
      * Deep copy
      */
    @Override
    public Object clone() {
        String[] cl;
        JsonObj clone = new JsonObj();  // What if Json.clone() or just clone() where clone uses This constructor?
                                        // Shoud This constructor call its parent's (and hence all its aoncestors')
                                        // automatically?
        for (String key: map.keySet()) {
            Json val = map.get(key);
            Json valClone = (Json) val.clone();

            cl = val.commentLines();
            if (cl != null) {
                valClone.setCommentLines(cl);
            }

            clone.map.put(key, valClone);    // deep copy
        }

        cl = this.commentLines();
        if (cl != null) {
            clone.setCommentLines(cl);
        }

        return clone;
    }

    /**
      * Parses the string into a {@code JsonObj}.
      * @param s string to parse
      * @return a JsonObj if s legally represent a JSON object.
      * @throws com.github.hyunikn.jsonden.exception.ParseError when s does not legally represent a Json object.
      */
    public static JsonObj parse(String s) throws ParseError {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonObj) {
            return (JsonObj) parsed;
        } else {
            throw new ParseError(ParseError.CASE_UNEXPECTED_JSON_TYPE,
				"not parsed into a JsonObj but " + parsed.getClass().getSimpleName());
        }
    }

    /**
      * Gets the value of the key.
      */
    public Json get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        } else {
            return map.get(key);
        }
    }

    /**
      * Deletes the member of the key.
      */
    public Json delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        } else {
            return map.remove(key);
        }
    }

    /**
      * Sets the value of the key.
      * @return the old Json of the key if any, otherwise {@code null}
      */
    public Json set(String key, Json value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        } else if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        } else {
            return map.put(key, value);
        }
    }

    /**
      * Gets the set of keys.
      * Iterating the set yields the keys in the order of addition (see LinkedHashSet).
      */
    public LinkedHashSet<String> keySet() {
        return new LinkedHashSet<>(map.keySet());
    }

    /**
      * Returns {@code true}, overriding {@code isObj()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public boolean isObj() { return true; }

    /**
      * Returns {@code this}, overriding {@code asObj()} of {@link com.github.hyunikn.jsonden.Json Json}.
      */
    @Override
    public JsonObj asObj() { return this; }

    /**
      * Returns the members as a map.
      */
    @Override
    public LinkedHashMap<String, Json> getMap() { return new LinkedHashMap(map); }

    // ---------------------------------------------------

    /**
      * Erases all the members.
      */
    public void clear() {
        map.clear();
    }

    // ===================================================
    // Protected

    protected final LinkedHashMap<String, Json> map;

    @Override
    protected String getTypeName() {
        return "object";
    }

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {

        boolean useIndents = indentSize != 0;

        if (indentLevel < 0) {
            // negative indent size indicates that we are right after a key in an object
            indentLevel *= -1;
        } else {
            writeComment(sbuf, commentLines, indentSize, indentLevel);
            writeIndent(sbuf, indentSize, indentLevel);
        }

        sbuf.append('{');
        if (useIndents) {
            sbuf.append('\n');
        }

        boolean first = true;
        for (String key: map.keySet()) {
            Json val = map.get(key);
            if (first) {
                first = false;
            } else {
                if (useIndents) {
                    sbuf.append(",\n");
                } else {
                    sbuf.append(",");
                }
            }

            writeComment(sbuf, val.commentLines(), indentSize, indentLevel + 1);

            writeIndent(sbuf, indentSize, indentLevel + 1);
            sbuf.append('"');
            sbuf.append(key);
            sbuf.append("\":");
            if (useIndents) {
                sbuf.append(' ');
            }
            val.write(sbuf, indentSize, -(indentLevel + 1));
        }

        if (useIndents) {
            sbuf.append('\n');
        }
        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append('}');
    }

    @Override
    protected Json getChild(String key) {
        return map.get(key);
    }

    // ===================================================
    // Private


}

