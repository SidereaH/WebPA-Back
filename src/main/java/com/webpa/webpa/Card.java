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
    private Boolean isAvaible;
    private String originCounrty;
    private Date warranty;
    private Partner partner;
    private String color;
    private String articul;
    private String img_url;
}
