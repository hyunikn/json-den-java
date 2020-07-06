Json-den-java
=============
Json-den-java is a JSON parser and stringifier (Decoder and ENcoder) library.

Its distinctive features include

1. Method `equals` checks the value equality, not the reference equality, and method `clone` deeply copies JSON values.
    They properly handle hierarchical structures of JSON values.
2. It provides a convenient method `getx` which queries JSON nodes
    located deep in hierarchical structures with a single dot delimited path.
    For example, you can use `json.getx("how.deep.is.your.love")`
    instead of `json.get("how").get("deep").get("is").get("your").get("love")`.
3. It provides a convenient method `setx` which creates parent nodes as needed,
    like the UNIX shell command `mkdir -p`, while updating JSON nodes located deep in hierarchical structures.
    For example, `json.setx("how.deep.is.your.love", val)` sets `val` at the path "how.deep.is.your.love"
    even when the node corresponding to 'how', 'deep', 'is' or 'your' is currently absent.
4. It supports C-style line and block comments in JSON texts. Instead of the common approach,
    minifying JSON texts before parsing, its parser recognizes comments
    so that it can preserve and report the original row and column offsets of tokens on syntax errors,
    which is imposible in the minifying approach.

Git-clone it, run `gen-apidocs.sh` included, and see the generated API documentation.

Json-den-java is under the BSD-2 license.

'
