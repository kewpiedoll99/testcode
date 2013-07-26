package com.example.helloworld.core;

/**
 * User: barclayadunn
 * Date: 7/26/13
 * Time: 10:33 AM
 */
public class Saying {
    private final long id;
    private final String content;

    public Saying(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
