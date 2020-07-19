package com.github.hyunikn.jsonden;

/** Options of {@code stringify()} method.
  *  (1) indentSize (int)
  *  (2) initialIndentLevel (int)
  *  (3) omitRemarks (boolean)
  *  (4) omitComments (boolean)
  */
public class StrOpt {

    /** Default {@code stringify()} options:
      * indentSize=4, initialIndentLevel=0, omitRemarks=false, omitComments=false
     */
    public static StrOpt DEFAULT = new StrOpt();

    /** Serializing {@code stringify()} options:
      * indentSize=0, initialIndentLevel=0, omitRemarks=false, omitComments=false
     */
    public static StrOpt SERIALIZING = new StrOpt().indentSize(0);

    /** Values only {@code stringify()} options:
      * indentSize=4, initialIndentLevel=0, omitRemarks=true, omitComments=true
     */
    public static StrOpt VALUES_ONLY = new StrOpt().omitRemarks(true).omitComments(true);

    /** Getter */
    public int indentSize() {
        return indentSize;
    }

    /** Getter */
    public int initialIndentLevel() {
        return initialIndentLevel;
    }

    /** Getter */
    public boolean omitRemarks() {
        return omitRemarks;
    }

    /** Getter */
    public boolean omitComments() {
        return omitComments;
    }

    /** Setter */
    public StrOpt indentSize(int indentSize) {
        this.indentSize = indentSize;
        return this;
    }

    /** Setter */
    public StrOpt initialIndentLevel(int initialIndentLevel) {
        this.initialIndentLevel = initialIndentLevel;
        return this;
    }

    /** Setter */
    public StrOpt omitRemarks(boolean omitRemarks) {
        this.omitRemarks = omitRemarks;
        return this;
    }

    /** Setter */
    public StrOpt omitComments(boolean omitComments) {
        this.omitComments = omitComments;
        return this;
    }

    int indentSize = 4;
    int initialIndentLevel = 0;
    boolean omitRemarks = false;
    boolean omitComments = false;
}

