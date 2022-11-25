package com.example.milk.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    private Integer oid;
    private Integer uid;
    private Integer mid;
    private String oTime;
    private String oNo;
    private Integer oPrice;
    private String oType;
    private Integer oCount;

}
