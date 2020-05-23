package com.wingsoft.jsonden;

public class JsonArr extends Json {

    // ===================================================
    // Public

    public static JsonArr parse(String s) {
        Json parsed = Json.parse(s);
        if (parsed instanceof JsonArr) {
            return (JsonArr) parsed;
        } else {
            throw new Error("not parsed into a JsonArr but " + parsed.getClass().getSimpleName());
        }
    }

    private adjustIndex(int index) {

        int size = arr.size();
        if (index < -size || index >= size) {
            return -1;
        }

        if (index < 0) {
            return index + size;
        } else {
            return index;
        }
    }

    public Json get(int index) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return arr.get(i);
        }
    }

    public Json remove(int index) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return arr.remove(i);
        }
    }

    public Json update(int index, Json node) {
        int i = adjustIndex(index);
        if (i < 0) {
            return null;
        } else {
            return arr.set(i, node);
        }
    }

    public boolean insert(int index, Json node) {
        int i = adjustIndex(index);
        if (i < 0) {
            return false;
        } else {
            arr.add(i, node);
            return true;
        }
    }

    public Json append(Json node) {
    }

    // ===================================================
    // Protected

    @Override
    protected void write(StringBuffer sbuf, int indentSize, int indentLevel) {
        // TODO
    }

    @Override
    protected Json getChild(String child) {
        return crudCommonForArray(CrudCase.GET, child, null);
    }

    @Override
    protected Json removeChild(String child) {
        return crudCommonForArray(CrudCase.REMOVE, child, null);
    }

    @Override
    protected Json updateChild(String child, Json node) {
        return crudCommonForArray(CrudCase.SET, child, child);
    }

    @Override
    protected Json insertChild(String child, Json node) {
        return crudCommonForArray(CrudCase.SET, child, child);
    }

    @Override
    protected Json appendChild(Json node) {
        return crudCommonForArray(CrudCase.SET, name, child);
    }

    @Override
    protected String getTypeName() {
        return "array";
    }

    // ===================================================
    // Private

    private final LinkedList<Json> arr = new LinkedList<>();

    private Json crudCommonForArray(CrudCase case_, String name, Json newNode) {

		assert name != null;

		int index;
        try {
            index = Integer.parse(name);
        } catch (NumberFormatException e) {
            throw new Error(name + " is not an integer and cannot be an index to an array element");
        }

        switch (case_) {
            case GET:
                return get(index);
            case REMOVE:
                return remove(index);
            case SET:
                return set(index, newNode);
            default:
                assert(false);
                return null;
        }
    }

}
