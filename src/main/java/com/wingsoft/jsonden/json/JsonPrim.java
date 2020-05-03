package com.wingsoft.jsonden.json;

abstract class JsonPrim extends Json {

    public String toString() {
        return text;
    }

    protected JsonPrim(String text) {
        this.text = text;
    }

    private String text;
}
