Json-den-java
=============
Json-den-java is a JSON parser and stringifier (Decoder and ENcoder) library.

Its distinctive features include

1. Method `equals` checks the value equality, not the reference equality, and method `clone` deeply copies values.
    They properly handle hierarchical structures of JSON values.
2. Its parser directly handles line and block comments instead of the common approach,
    minifying JSON texts before parsing,
    so that it can preserve and report the original row and column offsets of tokens on syntax errors.
3. It provides convenient methods which query and update subnodes located deep in hierarchical structures
    with a single dot delimited path.
    For example, you can use `json.getx("how.deep.is.your.love")` instead of the commonly used form
    `json.get("how").get("deep").get("is").get("your").get("love")`.

Git-clone it, run `gen-apidocs.sh` included, and see the generated API documentation.

Json-den-java is under the BSD-2 license.

'
