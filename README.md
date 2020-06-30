Json-den-java
=============
Json-den-java is a JSON parser and stringifier (Decoder and ENcoder) library.

Its distinctive features include

1. Its parsing rule directly handles line and block comments without minifying JSON texts before parsing,
    so that it can preserve and report the original row and column offsets of tokens on syntax errors.
2. It preserves comment blocks of a special form: it maintains and prints them while stringifying JSON values.
    Enclose comment blocks you want to preserve with `/**` and `*/`.
3. It provides a convenient `getx` method which queries subnodes located deep in hierarchical structures.
    For example, you can use `json.getx("how.deep.is.your.love")` instead of commonly used
    `json.get("how").get("deep").get("is").get("your").get("love")`.
4. `equals` method checks the value equality, not the reference equality.
    It considers hierarchical structures of JSON values.
5. `clone` method deeply copies JSON values.

Git-clone it, run `gen-apidocs.sh` included, and see the generated API documentation.

Json-den-java is under the BSD-2 license.

'
