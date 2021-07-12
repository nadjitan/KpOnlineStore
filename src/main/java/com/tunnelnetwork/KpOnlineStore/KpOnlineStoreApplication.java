package com.tunnelnetwork.KpOnlineStore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.DAO.CommentRepository;
import com.tunnelnetwork.KpOnlineStore.DAO.ProductRepository;
import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Models.User;
import com.tunnelnetwork.KpOnlineStore.Models.UserRole;
import com.tunnelnetwork.KpOnlineStore.User.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class KpOnlineStoreApplication {

	// @Autowired
  // private InMemoryUserDetailsManager inMemoryUserDetailsManager;

	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(KpOnlineStoreApplication.class, args);
		
	}

	@Autowired
	private UserRepository userRepository;

	public void createAdmin() {
		try {
			//inMemoryUserDetailsManager.createUser(User.withUsername("admin").password(bCryptPasswordEncoder().encode("admin")).authorities("ADMIN").build());
			
			User user = new User();
			user.setEmail("tunnelnetworkkpop@gmail.com");
			user.setEnabled(true);
			user.setFirstName("Admin");
			user.setLastName("admin");
			user.setPassword(bCryptPasswordEncoder().encode("admin"));
			user.setUserRole(UserRole.ADMIN);

			userRepository.saveAndFlush(user);

			System.out.println("Admin saved!");
		} catch (Exception e) {
			System.out.println("Admin not created:" + e.getMessage());
		}
	}

	@Bean
	CommandLineRunner runner(ProductRepository productRepository, CommentRepository commentRepository){
		createAdmin();
		
		return args -> {
			// Read json and write to db
			ObjectMapper mapper = new ObjectMapper();
			// Enable time formatting during mapping of file
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			TypeReference<List<Product>> typeReference = new TypeReference<List<Product>>(){};
			
			InputStream inputStream = TypeReference.class.getResourceAsStream("/json/products.json");

			// Start saving to product database
			try {
				List<Product> products = mapper.readValue(inputStream,typeReference);

				productRepository.save(products);

				System.out.println("Products Saved!");
			} catch (IOException e){
				System.out.println("Unable to save products: " + e.getMessage());
			}
		};
	}
}
