package com.dansmultipro.restapi.payload;

import lombok.Data;

@Data
public class Login {
    private String username;
    private String password;
}