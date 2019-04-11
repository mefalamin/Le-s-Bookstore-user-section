package com.bookstore;

import com.bookstore.domain.User;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.UserService;
import com.bookstore.utility.SecurityUtility;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BookstoreApplication implements CommandLineRunner {

	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	public BookstoreApplication(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void run(String... strings) throws Exception {
		User user1 = new User();
		user1.setFirstName("Karim");
		user1.setLastName("Khan");
		user1.setUsername("k");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user1.setEmail("karim@gmail.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1,role1));

		userService.createUser(user1,userRoles);
	}
}
