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
Following are code snippets in com.github.hyunikn.jsonden.example.Example class and their console
outputs when run.

### Sample JSON files

<table>
<tr>
<th> left.json </th>
<th> </th>
<th> right.json </th>
</tr>
<tr>
<td>

```javascript
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
```

</td>

<td> </td>

<td>

```javascript
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
```

</td>
</tr>
</table>

### Parse and stringify

**Code:**:
```java
package com.github.hyunikn.jsonden.example;

import com.github.hyunikn.jsonden.*;
import com.github.hyunikn.jsonden.exception.*;

import java.io.File;
import java.io.FileInputStream;

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
```

**Output:**:

```
# stringify with indent size 0
{"l":{},"c":7,"d":true,"o":{"l":"left","c":[true,false],"d":1.1,"d2":[false,true]},"a":[{"r":{"R":"R"},"c":{"C":null},"d":{"D":"O"}},[1,2,3],[10,20,30]]}
# stringify with indent size 2
{
  "l": {},
  "c": 7,
  "d": true,
  "o": {
    "l": "left",
    "c": [
      true,
      false
    ],
    "d": 1.1,
    "d2": [
      false,
      true
    ]
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
    [
      1,
      2,
      3
    ],
    [
      10,
      20,
      30
    ]
  ]
}
```

### Diff

**Code:**
```java
        // diff
        System.out.print("# diff");
        System.out.println(Jsons.prettyPrintDiff(left.diff(right)));
```

**Output:**
```
# diff

l:
L: {}
R: <none>

d:
L: true
R: false

o.l:
L: "left"
R: <none>

o.d:
L: 1.1
R: 3.3

o.d2.#0:
L: false
R: true

o.d2.#1:
L: true
R: false

a.#0.r.R:
L: "R"
R: <none>

a.#0.d.D:
L: "O"
R: "X"

a.#2.#0:
L: 10
R: 30

a.#2.#2:
L: 30
R: 10

r:
L: <none>
R: []

o.r:
L: <none>
R: "right"

a.#0.l.L:
L: <none>
R: "L"

a.#2.#3:
L: <none>
R: 0

a.#3.r:
L: <none>
R: "R"
```

### Intersect, subtract and merge

**Code:**
```java
        // intersect, subtract and merge
        System.out.println("# intersect");
        System.out.println(Jsons.intersect(left, right).stringify(4));
        System.out.println("# subtract");
        System.out.println(Jsons.subtract(left, right).stringify(4));
        System.out.println("# merge");
        System.out.println(Jsons.merge(left, right).stringify(4));
```

**Output:**
```
# intersect
{
    "c": 7,
    "o": {
        "c": [
            true,
            false
        ]
    },
    "a": [
        {
            "c": {
                "C": null
            }
        },
        [
            1,
            2,
            3
        ],
        [
            null,
            20
        ]
    ]
}
# subtract
{
    "l": {},
    "d": true,
    "o": {
        "l": "left",
        "d": 1.1,
        "d2": [
            false,
            true
        ]
    },
    "a": [
        {
            "r": {
                "R": "R"
            },
            "d": {
                "D": "O"
            }
        },
        null,
        [
            10,
            null,
            30
        ]
    ]
}
# merge
{
    "l": {},
    "c": 7,
    "d": false,
    "o": {
        "l": "left",
        "c": [
            true,
            false
        ],
        "d": 3.3,
        "d2": [
            true,
            false
        ],
        "r": "right"
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
                "D": "X"
            },
            "l": {
                "L": "L"
            }
        },
        [
            1,
            2,
            3
        ],
        [
            30,
            20,
            10,
            0
        ],
        {
            "r": "R"
        }
    ],
    "r": []
}
```

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
