Json-den-java
=============
Json-den-java is a library that parses (Decodes), stringifies (ENcodes) and  manipulates JSON.

Its distinctive features include

1. Method `equals` checks the value equality, not the reference equality. Method `clone` deeply copies JSON values.
    They properly handle hierarchical structures of JSON values.
2. It provides methods `diff`, `intersect` and `subtract` which identify identical or different parts of
    two JSON values.
3. It provides a method `getx` which can query JSON nodes
    located deep in hierarchical structures with a single path.
    For example, you can use `json.getx("how.deep.is.your.love")`
    instead of `json.get("how").get("deep").get("is").get("your").get("love")`.
4. It provides a method `setx` which creates parent nodes as needed,
    like the UNIX shell command `mkdir -p`, and then updates the JSON node at the specified path.
    For example, `json.setx("how.deep.is.your.love", val)` sets `val` at "how.deep.is.your.love"
    even when the node corresponding to 'how', 'deep', 'is' or 'your' is currently absent.
5. It supports C-style line and block comments in JSON texts. Instead of the common approach,
    minifying JSON texts before parsing, its parser recognizes comments
    so that it can preserve and report the original row and column offsets of tokens on syntax errors,
    which is imposible in the minifying approach.
6. It preserves block comments that are specified so
    (start comments you want to preserve with "/\*\*", not just with "/\*".)
    Unlike the ordinary ones, such comments are not erased while parsing, kept in internal representation,
    and printed back when stringified.

Git-clone it, run `gen-apidocs.sh` included, and see the generated API documentation.

Json-den-java is under the BSD-2 license.

'
