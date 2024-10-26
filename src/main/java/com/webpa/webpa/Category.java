package com.webpa.webpa;

import lombok.Data;

@Data
public class Category {
    private long category_id;
    private String category_name;

    public Category(long category_id, String category_name) {
        this.category_id = category_id;
        this.category_name = category_name;
    }
}
