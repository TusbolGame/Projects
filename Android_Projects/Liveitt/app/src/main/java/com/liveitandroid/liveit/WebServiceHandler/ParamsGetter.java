package com.liveitandroid.liveit.WebServiceHandler;

import java.io.File;

public class ParamsGetter {
    private File file = null;
    private String key;
    private String values;

    public ParamsGetter(String key, String values) {
        setKey(key);
        setValues(values);
    }

    public ParamsGetter(String key, File values) {
        setKey(key);
        setFile(values);
    }

    public String getValues() {
        return this.values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
