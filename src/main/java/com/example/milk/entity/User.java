package com.example.milk.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer uid;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String token;
}
