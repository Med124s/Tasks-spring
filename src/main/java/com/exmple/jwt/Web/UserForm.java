package com.exmple.jwt.Web;

import lombok.Data;

@Data
public class UserForm {

    private String username;
    private  String password;
    private String confPassword;
}
