package com.guillot.bsp30.lumps;

import java.util.Map.Entry;

public class EPair implements Entry<String, String> {

    public static final String REGEX = "^\"(.*)\" \"(.*)\"$";

    public static final String FORMAT = "   \"%s\": \"%s\"\n";

    private final String key;

    private String value;

    public EPair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        String oldValue = this.value;
        this.value = value;
        return oldValue;
    }
}
