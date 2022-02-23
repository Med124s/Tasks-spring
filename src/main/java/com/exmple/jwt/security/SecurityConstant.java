package com.exmple.jwt.security;

public class SecurityConstant {
    public static final String SECRET="med@gmail.com";
    public static final Long EXPIRATION_TIME = Long.valueOf(864_000_000); //10 jour
    public static final String TOKEN_PREFIX="Bearer ";
    public static final String HEADER_STRING="Authorization";
}
