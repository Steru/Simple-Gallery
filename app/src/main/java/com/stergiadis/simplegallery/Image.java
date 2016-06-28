package com.stergiadis.simplegallery;

import java.io.Serializable;

/**
 * Created by Steru on 2016-06-29.
 */
public class Image implements Serializable {
    private String name;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
