package com.github.hyunikn.jsonden;

import com.github.hyunikn.jsonden.exception.ParseError;
import com.github.hyunikn.jsonden.exception.UnreachablePath;

import com.github.hyunikn.jsonden.parser.MyParseTreeVisitor;

import java.io.IOException;

import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
  * A subclass of {@link com.github.hyunikn.jsonden.Json Json} which represents JSON objects.
  * It remembers the order of member addition, and prints them in that order when stringified.
  */
public class JsonObj extends JsonNonLeaf {

    // ===================================================
    // Public

    /**
      * Gets an empty {@code JsonObj}.
      */
    public static JsonObj instance() {
        return new JsonObj();
    }

    /**
      * Gets a {@code JsonObj} from a map.
      * @param map must not be null.
      */
    public static JsonObj instance(LinkedHashMap<String, Json> map) {
        if (map == null) {
            throw new IllegalArgumentException("map must not be null");
        }
        return new JsonObj(map);
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
        return that.myMap.equals(this.myMap);
    }

    /**
      * Gets the hash code.
      */
    @Override
    public int hashCode() {
        return myMap.hashCode();
    }

    /**
      * Deep copy.
      */
    @Override
    public JsonObj clone() {
        JsonObj clone = new JsonObj();  // What if Json.clone() or just clone() where clone uses This constructor?
                                        // Shoud This constructor call its parent's (and hence all its aoncestors')
                                        // automatically?
        for (String key: myMap.keySet()) {
            Json val = myMap.get(key);
            clone.myMap.put(key, val.clone());    // deep copy
        }

        clone.copyAnnotationsOf(this);

        return clone;
    }

    /**
      * Parses the string into a {@code JsonObj}.
      * Json-den does not allow those object member keys which have a dot (.) character in it or
      * have the form of hash(#) followed by an integer. Json-den uses such keys for special purposes
      * (see {@code getx} and {@code setx}).
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
      * @param key must not be null.
      */
    public Json get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        } else {
            return myMap.get(key);
        }
    }

    /**
      * Deletes the member of the key.
      * @param key must not be null.
      * @return this {@code JsonObj} for method chaining
      */
    public JsonObj delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("failed to delete: key cannot be null");
        } else {
            myMap.remove(key);
            return this;
        }
    }

    /**
      * Removes the member of the key and returns its value.
      * @param key must not be null.
      * @return the removed value of the key, or null if no such value.
      */
    public Json remove(String key) {
        if (key == null) {
            throw new IllegalArgumentException("failed to remove: key cannot be null");
        } else {
            return myMap.remove(key);
        }
    }

    /**
      * Sets the value of the key.
      * @param key must not be null, must not have a dot (.) character in it and must not have the form of
      * hash(#) followed by an integer.
      * @param value if {@code elem} is null then it is understood as a JsonNull.
      * @return this {@code JsonObj} for method chaining
      */
    public JsonObj set(String key, Json value) {
        if (key == null) {
            throw new IllegalArgumentException("failed to set: key cannot be null");
        }
        if (key.indexOf('.') >= 0) {
            throw new IllegalArgumentException("failed to set: " +
                    "Json-den does not allow dot(.) characters in JSON object member keys");
        }
        if (MyParseTreeVisitor.getArrElemIndex(key) != null) {
            throw new IllegalArgumentException("failed to set: " +
                    "Json-den does not allow object member keys which have the form of " +
                    "hash(#) followed by an integer: " + key);
        }

        if (value == null) {
            value = new JsonNull();
        }

        myMap.put(key, value);
        return this;
    }
    /** short for {@code set(key, new JsonBool(b))} */
    public JsonObj set(String key, boolean b) {
        return set(key, new JsonBool(b));
    }
    /** short for {@code set(key, new JsonNum(n))} */
    public JsonObj set(String key, byte n) {
        return set(key, new JsonNum(n));
    }
    /** short for {@code set(key, new JsonNum(n))} */
    public JsonObj set(String key, short n) {
        return set(key, new JsonNum(n));
    }
    /** short for {@code set(key, new JsonNum(n))} */
    public JsonObj set(String key, int n) {
        return set(key, new JsonNum(n));
    }
    /** short for {@code set(key, new JsonNum(n))} */
    public JsonObj set(String key, long n) {
        return set(key, new JsonNum(n));
    }
    /** short for {@code set(key, new JsonNum(n))} */
    public JsonObj set(String key, float n) {
        return set(key, new JsonNum(n));
    }
    /** short for {@code set(key, new JsonNum(n))} */
    public JsonObj set(String key, double n) {
        return set(key, new JsonNum(n));
    }
    /** short for {@code set(key, new JsonNum(n))} */
    public JsonObj set(String key, BigInteger n) {
        return set(key, new JsonNum(n));
    }
    /** short for {@code set(key, new JsonNum(n))} */
    public JsonObj set(String key, BigDecimal n) {
        return set(key, new JsonNum(n));
    }
    /** short for {@code set(key, new JsonStr(s))} */
    public JsonObj set(String key, String s) {
        return set(key, new JsonStr(s));
    }

    /**
      * Gets the set of keys.
      * Iterating the set yields the keys in the order of addition (see {@link java.util.LinkedHashSet LinkedHashSet}).
      */
    public LinkedHashSet<String> keySet() {
        return new LinkedHashSet<>(myMap.keySet());
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
    public LinkedHashMap<String, Json> getMap() { return new LinkedHashMap(myMap); }

    // ---------------------------------------------------

    /**
      * Erases all the members.
      * @return this {@code JsonObj} for method chaining
      */
    @Override
    public JsonObj clear() {
        myMap.clear();
        return this;
    }

    /**
      * Gets diff of this and that {@code JsonObj}s.
      * @return map of paths to the different values.
      */
    public Map<String, List<Json>> diff(JsonObj that) {
        return super.diff(that);
    }

    /**
      * Gets terminal nodes whose paths are common of this and that {@code Json}s and whose values are different.
      * @return map of paths to the different values.
      */
    public Map<String, List<Json>> diffAtCommonPaths(JsonObj that) {
        return super.diffAtCommonPaths(that);
    }

    /** Intersects two sets of flatten results of this and that {@code JsonObj}s.
      * Result consists of terminal nodes that are in both this and that {@code JsonObj}s,
      */
    public LinkedHashMap<String, Json> intersectFlattened(JsonObj that) throws UnreachablePath {
        return super.intersectFlattened(that);
    }

    /** Intersects this and that {@code JsonObj}s.
      * Result {@code JsonObj} consists of terminal nodes that are in both this and that {@code JsonObj}s,
      * and possibly null nodes in arrays which are not actually included in the intersection result but
      * are there to fill the holes in the arrays.
      * The update is in-place, that is, changes this {@code JsonObj}.
      */
    public JsonObj intersect(JsonObj that) throws UnreachablePath {
        return (JsonObj) super.intersect(that);
    }

    /** Subtracts two sets of flatten results of this and that {@code JsonObj}s.
      * Result consists of terminal nodes that are in this {@code JsonObj} but not in that {@code JsonObj},
      */
    public LinkedHashMap<String, Json> subtractFlattened(JsonObj that) throws UnreachablePath {
        return super.subtractFlattened(that);
    }

    /** Subtracts that {@code JsonObj} from this.
      * Result {@code JsonObj} consists of terminal nodes that are in this {@code JsonObj} but
      * not in that {@code JsonObj},
      * and possibly null nodes in arrays which are not actually included in the subtraction result but
      * are there to fill the holes in the arrays.
      * The update is in-place, that is, changes this {@code JsonObj}.
      */
    public JsonObj subtract(JsonObj that) throws UnreachablePath {
        return (JsonObj) super.subtract(that);
    }

    /** Merges (overwrites) that {@code JsonObj} into this.
      * Result {@code JsonObj} consists of terminal nodes that are in this {@code JsonObj} but
      * not in that {@code JsonObj} in addition to those in that {@code JsonObj}.
      * The update is in-place, that is, changes this {@code JsonObj}.
      */
    public JsonObj merge(JsonObj that) throws UnreachablePath {
        return (JsonObj) super.merge(that);
    }

    /**
      * Sets {@code val} at the path, possibly deep in a nested structure, creating parent nodes as needed.
      * This behavior is reminiscent of the UNIX shell command {@code mkdir} with {@code -p} option.
      * Each of the created parent nodes is either a {@code JsonArr} or a {@code JsonObj} depending on whether
      * the next path segment is a hash(#) followed by an integer (array element index) or not.
      * For example, {@code eo.setx("a.b.#0.c.d", val)} for an empty {@code JsonObj} {@code eo}
      * results in {@code {"a":{"b": [ {"c":"{"d": val}} ]}}} with newly created four parent nodes
      * corresponding to a, b, #0 and c, respectively.
      * @param path dot delimited segments of a path to a subnode.
      *  Each segment represents either an object member key or an array element index (hash followed by an integer).
      * @return this {@code Json} for method chaining
      * @throws com.github.hyunikn.jsonden.exception.UnreachablePath when the missing parent nodes cannot be created.
      */
    public JsonObj setx(String path, Json val) throws UnreachablePath {
        return (JsonObj) super.setx(path, val);
    }
    /** short for {@code setx(path, new JsonBool(b))} */
    public JsonObj setx(String path, boolean b) throws UnreachablePath {
        return setx(path, new JsonBool(b));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonObj setx(String path, byte n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonObj setx(String path, short n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonObj setx(String path, int n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonObj setx(String path, long n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonObj setx(String path, float n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonObj setx(String path, double n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonObj setx(String path, BigInteger n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonNum(n))} */
    public JsonObj setx(String path, BigDecimal n) throws UnreachablePath {
        return setx(path, new JsonNum(n));
    }
    /** short for {@code setx(path, new JsonStr(s))} */
    public JsonObj setx(String path, String s) throws UnreachablePath {
        return setx(path, new JsonStr(s));
    }

    /**
      * Same as {@code loadFlattened(flattened, true)}
      */
    public JsonObj loadFlattened(LinkedHashMap<String, Json> flattened) throws UnreachablePath {
        return (JsonObj) super.loadFlattened(flattened, true);
    }

    /**
      * Loads a flattened {@code Json} value into this object.
      * This method causes in-place modification of the current object.
      * If the current object already has a value at a path in {@code flattened} then the value is overwritten
      * by the one in {@code flattened}.
      * @param flattened a flattened {@code Json} value obtained by
      *   {@link com.github.hyunikn.jsonden.JsonNonLeaf#flatten flatten},
      *   {@link com.github.hyunikn.jsonden.JsonNonLeaf#intersect intersect},
      *   {@link com.github.hyunikn.jsonden.JsonNonLeaf#subtract subtract}, etc.
      * @param clone whether to clone the {@code Json} values while loading or not.
      * @return the current object for method chaining.
      */
    public JsonObj loadFlattened(LinkedHashMap<String, Json> flattened, boolean clone) throws UnreachablePath {
        return (JsonObj) super.loadFlattened(flattened, clone);
    }

    /**
      * Whether this object is empty or not.
      */
    @Override
    public boolean isTerminal() {
        return myMap.isEmpty();
    }

    /**
      * Deletes a subnode at {@code path} if any, and returns itself for method chaining.
      */
    @Override
    public JsonObj delx(String path) {
        return (JsonObj) super.delx(path);
    }

    // ===================================================
    // Protected

    protected final LinkedHashMap<String, Json> myMap;

    protected JsonObj() {
        myMap = new LinkedHashMap<>();
    }

    protected JsonObj(LinkedHashMap<String, Json> map) {
        this.myMap = new LinkedHashMap<>(map);
    }

    @Override
    protected void flattenInner(LinkedHashMap<String, Json> accum, String pathToMe) {

        String prefix;
        if (pathToMe == null) {
            prefix = "";
        } else {
            prefix = pathToMe + ".";
        }

        Set<String> keys = myMap.keySet();
        if (keys.isEmpty()) {
            if (pathToMe != null) {
                accum.put(pathToMe, this);
            }
        } else {
            for (String key: keys) {
                Json val = myMap.get(key);
                if (val instanceof JsonNonLeaf) {
                    ((JsonNonLeaf) val).flattenInner(accum, prefix + key);
                } else {
                    accum.put(prefix + key, val);
                }
            }
        }
    }

    @Override
    protected String getTypeName() {
        return "object";
    }

    @Override
    protected void write(StringBuffer sbuf, StrOpt opt, int indentLevel) {

        int indentSize = opt.indentSize;
        boolean useIndents = indentSize != 0;

        if (indentLevel < 0) {
            // negative indent size indicates that we are right after a key in an object
            indentLevel *= -1;
        } else {
            if (!opt.omitComments) {
                writeComment(sbuf, commentLines, indentSize, indentLevel);
            }
            if (!opt.omitRemarks) {
                writeRemark(sbuf, remarkLines, indentSize, indentLevel);
            }
            writeIndent(sbuf, indentSize, indentLevel);
        }

        if (myMap.isEmpty()) {
            sbuf.append("{}");
            return;
        }

        sbuf.append('{');
        if (useIndents) {
            sbuf.append('\n');
        }

        Set<Map.Entry<String, Json>> entries = myMap.entrySet();
        if (opt.sortObjectMembers > 0) {
            Set<Map.Entry<String, Json>> sorted = new TreeSet<>(ascComparator);
            entries.stream().forEach(x -> sorted.add(x));
            entries = sorted;
        } else if (opt.sortObjectMembers < 0) {
            Set<Map.Entry<String, Json>> sorted = new TreeSet<>(descComparator);
            entries.stream().forEach(x -> sorted.add(x));
            entries = sorted;
        }

        boolean first = true;
        for (Map.Entry<String, Json> e: entries) {
            String key = e.getKey();
            Json val = e.getValue();
            if (first) {
                first = false;
            } else {
                if (useIndents) {
                    sbuf.append(",\n");
                } else {
                    sbuf.append(",");
                }
            }

            if (!opt.omitComments) {
                writeComment(sbuf, val.commentLines(), indentSize, indentLevel + 1);
            }
            if (!opt.omitRemarks) {
                writeRemark(sbuf, val.remarkLines(), indentSize, indentLevel + 1);
            }

            writeIndent(sbuf, indentSize, indentLevel + 1);
            sbuf.append('"');
            sbuf.append(key);
            sbuf.append("\":");
            if (useIndents) {
                sbuf.append(' ');
            }
            val.write(sbuf, opt, -(indentLevel + 1));
        }

        if (useIndents) {
            sbuf.append('\n');
        }
        writeIndent(sbuf, indentSize, indentLevel);
        sbuf.append('}');
    }

    @Override
    protected Json getChild(String key) {
        return myMap.get(key);
    }

    // ===================================================
    // Private

    private static Comparator<Map.Entry<String, Json>> ascComparator = new Comparator<Map.Entry<String, Json>>() {
        public int compare(Map.Entry<String, Json> e1, Map.Entry<String, Json> e2) {
            return e1.getKey().compareTo(e2.getKey());
        }
    };

    private static Comparator<Map.Entry<String, Json>> descComparator = new Comparator<Map.Entry<String, Json>>() {
        public int compare(Map.Entry<String, Json> e1, Map.Entry<String, Json> e2) {
            return e2.getKey().compareTo(e1.getKey());
        }
    };


}

