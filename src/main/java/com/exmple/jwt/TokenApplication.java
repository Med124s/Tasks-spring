package com.exmple.jwt;

import com.exmple.jwt.Dao.RoleRepository;
import com.exmple.jwt.Dao.TaskRepository;
import com.exmple.jwt.Dao.UserRepository;
import com.exmple.jwt.Metier.AppRole;
import com.exmple.jwt.Metier.Task;
import com.exmple.jwt.Metier.AppUser;
import com.exmple.jwt.Service.AccountService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class TokenApplication implements CommandLineRunner {

	@Autowired
	private TaskRepository takRepos;
	@Autowired
	private UserRepository userRepos;
	@Autowired
	private RoleRepository roleRepos;
	@Autowired
	private AccountService accountService;
	@Autowired
	private RepositoryRestConfiguration repositoryRestConfiguration;
	public static void main(String[] args) {
		SpringApplication.run(TokenApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder getBCPE(){
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {
		repositoryRestConfiguration.exposeIdsFor(AppUser.class,Task.class);


		Stream.of("T1","T2","T3").forEach(t->{
			takRepos.save(new Task(null,t));
		});
		takRepos.findAll().forEach(t->{
			System.out.println(t);
		});

		accountService.saveUser(new AppUser(null,"user","1234",null));
		accountService.saveUser(new AppUser(null,"admin","1234",null));

		accountService.saveRole(new AppRole(null,"ADMIN"));
		accountService.saveRole(new AppRole(null,"USER"));

		accountService.addRoleToUser("admin","USER");
		accountService.addRoleToUser("admin","ADMIN");
		accountService.addRoleToUser("user","USER");

		userRepos.findAll().forEach(u->{
			System.out.println(u);
		});
	}
}
