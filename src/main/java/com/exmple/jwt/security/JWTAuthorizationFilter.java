package com.exmple.jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Pour Web Front End comme Angular, config headers
        response.addHeader("Access-Control-Allow-Origin","*");//cross origin allowed

        response.addHeader("Access-Control-Allow-Headers",
                        "Origin ," +
                            "Accept," +
                           " X-Requested-With,Content-Type, " +
                                "Access-Control-Request-Method, "+
                                "Access-Control-Request-Headers, "+
                           "authorization");


        //Exposer:كشف permette au applications frontend de lire ces entéte (il faut l'autorisé)
        response.addHeader("Access-Control-Expose-Headers","Access-Control-Allow-Origin,Access-Control-Allow-Credentials, authorization");
        //Le premier requete qui vient on client c'est OPTION, donc on dire c'est pa la penne d'utiliser les régles de security
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK); //Statut Code OK 200,Il repond avec ces entéte, voila le domaine..
        }
        else{

        //getting jwt in header
        String jwt = request.getHeader(SecurityConstant.HEADER_STRING);
        //if true do other filter
        if(jwt == null || !jwt.startsWith(SecurityConstant.TOKEN_PREFIX)){
            filterChain.doFilter(request,response); //do le filter
            return;
        }
        //on fait analyse sur les claims
        Claims claims = Jwts.parser()
                        .setSigningKey(SecurityConstant.SECRET)
                        //Removing Prefix bearer
                        .parseClaimsJws(jwt.replace(SecurityConstant.TOKEN_PREFIX,""))
                        .getBody();//return le contenu de token
        //getting userName from claim subject
        String username = claims.getSubject();
        //getting roles from claims and casting ro arraylist map key value
        ArrayList<Map<String,String>>roles = (ArrayList<Map<String, String>>) claims.get("roles");
        Collection<GrantedAuthority>authorities = new ArrayList<>();
        roles.forEach(r->{
            authorities.add(new SimpleGrantedAuthority(r.get("authority")));//geting keys of roles
        });
        //Vérrifier c'est le token que j'ai est ce que valid ou no
        UsernamePasswordAuthenticationToken userAuthenticated =
                new UsernamePasswordAuthenticationToken(username,null,authorities);
        //charger l'utilisateur authentifier en context de sécurity (syaq) de spring
        SecurityContextHolder.getContext().setAuthentication(userAuthenticated);
        filterChain.doFilter(request,response);
        }
    }
}
