package com.webpa.webpa;

import lombok.Data;
@Data
public class Partner {
    private int partner_id;
    private String partner_name;

    public Partner(int partner_id, String partner_name) {
        this.partner_id = partner_id;
        this.partner_name = partner_name;
    }
}