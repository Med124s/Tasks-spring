package com.exmple.jwt.security;


import com.exmple.jwt.Metier.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager auth) {
        super();
        this.authenticationManager = auth;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AppUser appuser = null;
        try {
            //Desérializer la requete en forma JSON using Jaxon object OBJECTMAPPER()
            appuser = new ObjectMapper().readValue(request.getInputStream(),AppUser.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("***************************");
        System.out.println("UserName :"+appuser.getUserName());
        System.out.println("pass :"+appuser.getPassword());
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appuser.getUserName(),appuser.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //Gestion de TOKEN

        //Get formation of user principal ==userName
        User springUser = (User) authResult.getPrincipal();
        //Now we generate token building jwt and effect claims "revendication" "مطالبة"
        String jwt = Jwts.builder()
                     .setSubject(springUser.getUsername())
                     .setExpiration(
                             new Date(System.currentTimeMillis()+SecurityConstant.EXPIRATION_TIME)
                     ).signWith(SignatureAlgorithm.HS256,SecurityConstant.SECRET)
                //new claim personaliser peux rappell ce que vous voullez
                        .claim("roles",springUser.getAuthorities())
                        .compact(); //Damj jwt header,payload,signiature
        response.addHeader(SecurityConstant.HEADER_STRING,SecurityConstant.TOKEN_PREFIX+jwt);
    }
}
