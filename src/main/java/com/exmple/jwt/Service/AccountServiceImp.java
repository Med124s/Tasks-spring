package com.exmple.jwt.Service;

import com.exmple.jwt.Dao.RoleRepository;
import com.exmple.jwt.Dao.UserRepository;
import com.exmple.jwt.Metier.AppRole;
import com.exmple.jwt.Metier.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImp implements AccountService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public AppUser saveUser(AppUser user) {
        //Crypter le mot pass
        String hashPw = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPw);
        return userRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String rl) {
        AppUser user = userRepository.findByUserName(username);
        AppRole role = roleRepository.findByRoleName(rl);
        user.getRoles().add(role);
    }

    @Override
    public AppUser findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
