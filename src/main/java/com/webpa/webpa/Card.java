package com.webpa.webpa;
import lombok.Data;
import java.io.Serializable;
import java.util.*;
@Data
public class Card implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long id;
    private Date placedAt;
}
