package com.exmple.jwt.Dao;

import com.exmple.jwt.Metier.AppRole;
import com.exmple.jwt.Metier.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TaskRepository extends JpaRepository<Task,Long> {

}
