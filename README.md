Json-den-java
=============
Json-den-java is a JSON parser and stringifier (Decoder and ENcoder) library.

Its distinctive features include

1. Method `equals` checks the value equality, not the reference equality, and method `clone` deeply copies values.
    They properly handle hierarchical structures of JSON values.
2. It provides convenient methods `getx`, `setx`, `appendx`, and so on which query or update JSON nodes
    located deep in hierarchical structures with a single dot delimited path.
    For example, you can use `json.getx("how.deep.is.your.love")`
    instead of the common form `json.get("how").get("deep").get("is").get("your").get("love")`.
3. It provides convenient methods `setxp` and `appendxp` which creates parent nodes as needed,
    like the UNIX shell command `mkdir -p`, while updating JSON nodes located deep in hierarchical structures.
    For example, `json.setxp("how.deep.is.your.love", val)` sets `val` at the path
    even when the node corresponding to "your" is currently absent.
    (But, `setx` mentioned above throws an exception in such cases.)
4. It supports C-style line and block comments in JSON texts. Instead of the common approach,
    minifying JSON texts before parsing, its parser recognizes comments
    so that it can preserve and report the original row and column offsets of tokens on syntax errors,
    which is imposible in the minifying approach.

Git-clone it, run `gen-apidocs.sh` included, and see the generated API documentation.

Json-den-java is under the BSD-2 license.

'
