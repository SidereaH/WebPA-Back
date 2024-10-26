package com.webpa.webpa;
import lombok.Data;
import java.io.Serializable;
import java.util.*;

@Data
public class Card implements Serializable{
    private Long id;
    private Date placedAt;
    private String name;
    private String description;
    private Category category;
    private boolean isAvaible;
    private String originCounrty;
    private Date warranty;
    private Partner partner;
    private String color;
    private String articul;
    private String img_url;
    private Seller seller;

    public Card(Long id, Date placedAt, String name, String description, Category category, boolean isAvaible, String originCounrty, Date warranty, Partner partner, String color, String articul, String img_url, Seller seller) {
        this.id = id;
        this.placedAt = placedAt;
        this.name = name;
        this.description = description;
        this.category = category;
        this.isAvaible = isAvaible;
        this.originCounrty = originCounrty;
        this.warranty = warranty;
        this.partner = partner;
        this.color = color;
        this.articul = articul;
        this.img_url = img_url;
        this.seller = seller;
    }

}
