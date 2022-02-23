package com.exmple.jwt.Dao;

import com.exmple.jwt.Metier.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


@RepositoryRestResource
public interface UserRepository extends JpaRepository<AppUser,Long> {
    public AppUser findByUserName(String username);
}
