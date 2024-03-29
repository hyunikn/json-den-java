package io.github.hyunikn.jsonden.parser;

import io.github.hyunikn.jsonden.*;
import io.github.hyunikn.jsonden.parser.antlrgen.JsonParse;
import io.github.hyunikn.jsonden.parser.antlrgen.JsonParseBaseVisitor;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

public class MyParseTreeVisitor extends JsonParseBaseVisitor<Json> {

    private static final String[] RTT = new String[0];

    private static String unescape(String s) {
        StringBuffer sb = new StringBuffer();

        int sLen = s.length();
        for (int i = 0; i < sLen; i++) {
            char c = s.charAt(i);
            if (c == '\\' && (i + 1) < sLen) {
                char c2 = s.charAt(i + 1);
                switch (c2) {
                    case '"':
                        sb.append('"');
                        i++;
                        break;
                    case '\\':
                        sb.append('\\');
                        i++;
                        break;
                    case '/':
                        sb.append('/');
                        i++;
                        break;
                    case 'b':
                        sb.append('\b');
                        i++;
                        break;
                    case 'f':
                        sb.append('\f');
                        i++;
                        break;
                    case 'n':
                        sb.append('\n');
                        i++;
                        break;
                    case 'r':
                        sb.append('\r');
                        i++;
                        break;
                    case 't':
                        sb.append('\t');
                        i++;
                        break;
                    case 'u':
                        if (i + 5 < sLen) {
                            int j;
                            for (j = 2; j < 6; j++) {
                                char c3 = s.charAt(i + j);
                                if ('a' <= c3 && c3 <= 'f' || 'A' <= c3 && c3 <= 'F' || '0' <= c3 && c3 <= '9') {
                                } else {
                                    break;
                                }
                            }
                            if (j == 6) {
                                String hexStr = s.substring(i + 2, i + 6);
                                int hex = Integer.parseInt(hexStr, 16);
                                sb.append((char) hex);
                                i += 5;
                            } else {
                                sb.append(c);
                            }

                        } else {
                            sb.append(c);
                        }
                        break;
                    default:
                        sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    private String stripQuoteMarks(String s) {
        int len = s.length();
        assert len >= 2;
        return s.substring(1, len - 1);
    }

    private String stripRemarkMarks(String s) {
        int len = s.length();
        assert len >= 5;
        return s.substring(3, len - 2);
    }

    private String getLocation(TerminalNode tn) {
        Token tk = tn.getSymbol();
        return String.format("(%d,%d)", tk.getLine(), tk.getCharPositionInLine());
    }

    @Override public Json visitJson(JsonParse.JsonContext ctx) {
        return visit(ctx.remarkedValue());
    }

    private List<String> getRemarkLineList(TerminalNode tn) {
        String text = tn.getText();
        //Token tk = tn.getSymbol();
        //int row = tk.getLine(); // 1...
        //int col = text.substring(1).indexOf("/"); // 0..

        String remark = stripRemarkMarks(text);
        String[] lines = remark.split("\n", -1);
        int nLines = lines.length;

        List<String> lineList = new LinkedList<>();

        for (int i = 0; i < nLines; i++) {
            String line = lines[i].trim();

            if (i == 0) {
                if (line.length() == 0) {
                    lineList.add("");
                } else {
                    lineList.add(" " + line);
                }
                continue;
            }

            if (i == nLines - 1 && line.length() == 0) {
                continue;
            }

            if (line.charAt(0) == '*') {
                String line1 = line.substring(1);
                lineList.add(line1);
            } else {
                throw new Error("a remark line does not start with '*' at line " + (tn.getSymbol().getLine() + i));
            }
        }

        return lineList;
    }

    public static Integer getArrElemIndex(String s) {
        assert s != null;

        // array element index must start with a hash(#) character.
        if (s.indexOf('#') == 0) {
            try {
                int i = Integer.valueOf(s.substring(1));
                if (i < 0) {
                    return null;
                } else {
                    return i;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override public Json visitRemarkedValue(JsonParse.RemarkedValueContext ctx) {
        Json value = visitValue(ctx.value());
        TerminalNode tn = ctx.REMARK();
        if (tn != null) {
            List<String> lineList = getRemarkLineList(tn);
            if (lineList.size() > 0) {
                value.setRemarkLines(lineList.toArray(RTT));
            }
        }
        return value;
    }

    @Override public Json visitValue(JsonParse.ValueContext ctx) {

        if (ctx.STRING() != null) {
            return JsonStr.instance(unescape(stripQuoteMarks(ctx.STRING().getText())));
        } else if (ctx.NUMBER() != null) {
            return JsonNum.instance(ctx.NUMBER().getText());
        } else if (ctx.TRUE() != null) {
            return JsonBool.instance(true);
        } else if (ctx.FALSE() != null) {
            return JsonBool.instance(false);
        } else if (ctx.NULL() != null) {
            return JsonNull.instance();
        } else if (ctx.obj() != null) {
            return visitObj(ctx.obj());
        } else if (ctx.arr() != null) {
            return visitArr(ctx.arr());
        }

        throw new Error("unreachable: value type");
    }

    @Override public Json visitObj(JsonParse.ObjContext ctx) {
        JsonObj jo = JsonObj.instance();

        for (JsonParse.RemarkedPairContext cp: ctx.remarkedPair()) {

            JsonParse.PairContext pc = cp.pair();
            String key = stripQuoteMarks(unescape(pc.STRING().getText()));
            if (key.indexOf('.') >= 0) {
                throw new Error("Json-den does not allow dot(.) characters in JSON object member keys: '" +
                        key + "' at " + getLocation(pc.STRING()));
            }
            if (getArrElemIndex(key) != null) {
                throw new Error("Json-den does not allow object member keys which are of the form, " +
                        "hash(#) followed by an integer: " + key);
            }

            Json val = visitValue(pc.value());
            Json old = jo.get(key);
            if (old != null) {
                throw new Error("a duplicate key '" + key + "' at " + getLocation(pc.STRING()));
            }
            jo.set(key, val);

            TerminalNode tn = cp.REMARK();
            if (tn != null) {
                List<String> lineList = getRemarkLineList(tn);
                if (lineList.size() > 0) {
                    val.setRemarkLines(lineList.toArray(RTT));
                }
            }
        }

        return jo;
    }

    @Override public Json visitRemarkedPair(JsonParse.RemarkedPairContext ctx) {
        throw new Error("unreachable: visiting remarked pair");
    }

    @Override public Json visitPair(JsonParse.PairContext ctx) {
        throw new Error("unreachable: visiting pair");
    }

    @Override public Json visitArr(JsonParse.ArrContext ctx) {
        JsonArr ja = JsonArr.instance();

        for (JsonParse.RemarkedValueContext cv: ctx.remarkedValue()) {
            ja.append(visitRemarkedValue(cv));
        }

        return ja;
    }
}
