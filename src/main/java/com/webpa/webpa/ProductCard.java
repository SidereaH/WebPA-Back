package com.webpa.webpa;
import lombok.Data;
import java.io.Serializable;
import java.util.*;
import java.net.URL;

@Data
public class ProductCard implements Serializable{
    private Long id;
    private String name;
    private double price;
    private URL image;
    private HashMap<String,String> mainInfo;
    private HashMap<String,String> mainCharacteristics;
    private HashMap<String,String> additionalInformation;
    private String description;    
}
