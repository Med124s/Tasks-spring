package com.exmple.jwt.Service;

import com.exmple.jwt.Metier.AppRole;
import com.exmple.jwt.Metier.AppUser;
import org.springframework.stereotype.Service;


public interface AccountService {
    public AppUser saveUser(AppUser user);
    public AppRole saveRole(AppRole role);
    public void addRoleToUser(String username,String Role);
    public AppUser findUserByUserName(String userName);
}
