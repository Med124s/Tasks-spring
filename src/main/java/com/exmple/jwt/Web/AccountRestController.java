package com.exmple.jwt.Web;

import com.exmple.jwt.Dao.RoleRepository;
import com.exmple.jwt.Dao.UserRepository;
import com.exmple.jwt.Metier.AppUser;
import com.exmple.jwt.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountService accountService;
    @PostMapping(path = "/register")
    public AppUser saveUser(@RequestBody UserForm userForm){
        if(!userForm.getPassword().equals(userForm.getConfPassword()))
            throw new RuntimeException("Password Not confirmer");
        AppUser appUser = accountService.findUserByUserName(userForm.getUsername());
        if(appUser!=null)
            throw new RuntimeException("User Exist Deja");
        AppUser userApp = new AppUser();
        userApp.setUserName(userForm.getUsername());
        System.out.println(userApp.getUserName());
        userApp.setPassword(userForm.getPassword());
        accountService.saveUser(userApp);
        accountService.addRoleToUser(userApp.getUserName(),"USER");
        return userApp;
    }
    @GetMapping(path = "/Search/{userName}")
    public AppUser search(@PathVariable String userName){
        return this.userRepository.findByUserName(userName);
    }

}
