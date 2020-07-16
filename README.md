# Json-den-java

Json-den-java is a library that parses (Decodes), stringifies (ENcodes) and  manipulates JSON.

Its distinctive features include

* Method `equals` checks the value equality (not the reference equality).
    `clone` deeply copies JSON values.
    `diff` identifies different parts of two JSON values.
    These methods properly handle hierarchical structure of JSON values.
* It provides a method `getx` which can query JSON nodes
    located deep in hierarchical structures with a single path.
    For example, you can use `json.getx("how.deep.is.your.love")`
    instead of `json.get("how").get("deep").get("is").get("your").get("love")`.
* It provides a method `setx` which creates parent nodes as needed,
    like the UNIX shell command `mkdir -p`, and then updates the JSON node at the specified path.
    For example, `json.setx("how.deep.is.your.love", "10,000ft")` sets "10,000ft"
    at "how.deep.is.your.love"
    even when the node corresponding to 'how', 'deep', 'is' or 'your' is currently absent.
* It supports binary operations `intersect`, `subtract` and `merge` on JSON objects and arrays.
* It supports C-style line and block comments in JSON texts. Instead of the common approach,
    minifying JSON texts before parsing, its parser recognizes comments
    so that it can preserve and report the original row and column offsets of tokens 
    on syntax errors, which is imposible in the minifying approach.
* It preserves block comments that are specified so;
    start comments you want to preserve with "/\*\*", not just with "/\*".
    Unlike the ordinary ones, such comments are not erased while parsing, 
    kept in internal representation, and printed back when stringified.

For more information, download the source code, run `gen-apidocs.sh` included, 
and see the generated API documentation.

Json-den-java is under BSD-2 license.

## Examples

### Sample JSON files

<table>

<tr>
<th> left.json </th>
<th> </th>
<th> right.json </th>
</tr>

<tr>

<td>
<pre>

{
    "l": {},
    "c": 7,
    "d": true,
    "o": {
        "l": "left",
        "c": [ true, false ],
        "d": 1.1,
        "d2": [ false, true ]
    },
    "a": [
        {   
            "r": {
                "R": "R"
            },
            "c": {
                "C": null
            },
            "d": {
                "D": "O"
            }
        },
        [ 1, 2, 3 ],
        [ 10, 20, 30 ]
    ]   
}      
</pre>   
</td>
<td> </td>
<td>
<pre>

{
    "r": [],
    "c": 7,
    "d": false,
    "o": {
        "r": "right",
        "c": [ true, false ],
        "d": 3.3,
        "d2": [ true, false ]
    },
    "a": [
        {   
            "l": {
                "L": "L"
            },
            "c": {
                "C": null
            },
            "d": {
                "D": "X"
            }
        },
        [ 1, 2, 3 ],
        [ 30, 20, 10, 0 ],
        {
            "r": "R"
        }
    ]
}
</pre>
</td>

</tr>
</table>

### Parse and stringify

### Diff

### Intersect, subtract and merge

## Maven Configuration

You can get the latest stable version from Maven Central by adding the following dependency 
in your pom.xml:
```xml
    <dependency>
      <groupId>com.github.hyunikn</groupId>
      <artifactId>jsonden</artifactId>
      <version>0.9.3</version>
    </dependency>
```
