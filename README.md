Json-den-java
=============
Json-den-java is a JSON parser and stringifier (Decoder and ENcoder) library.
It parses, manipulates, and stringifies JSON.

Its distinctive features include

. Unlike most JSON library, its parsing rule directly handles line and block comments without minifying JSON texts before parsing, so that it can preserve and tell the original row and column offsets on syntax errors.
. It preserves comment blocks of a special form: it maintains and prints them while stringifying JSON values.
. It provides a convenient 'getx' method which queries subnodes located deep in hierarchical structures. For example, you can use json.getx("how.deep.is.your.love") instead of json.get("how").get("deep").get("is").get("your").get("love").
. Equals method checks the value equality (not the reference equality). It considers hierarchical structures of JSON values.
. Clone method deeply copies JSON values.

Json-den-java is under the BSD-2 license.

'
