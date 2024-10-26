package com.webpa.webpa;

import lombok.Data;

@Data
public class Seller {
    //params
    private String  name;
    public Seller(String name){
        this.name = name;
    }
}
