package com.tunnelnetwork.KpOnlineStore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Service.CommentService;
import com.tunnelnetwork.KpOnlineStore.Service.ProductService;


@SpringBootApplication
public class KpOnlineStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(KpOnlineStoreApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ProductService productService, CommentService commentService) {
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

				productService.save(products);

				System.out.println("Products Saved!");
			} catch (IOException e){
				System.out.println("Unable to save products: " + e.getMessage());
			}
		};
	}
}
