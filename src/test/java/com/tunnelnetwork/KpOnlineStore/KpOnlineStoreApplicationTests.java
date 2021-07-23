package com.tunnelnetwork.KpOnlineStore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KpOnlineStoreApplicationTests {

	@Autowired
	ObjectMapper objectMapper;

	@Test
	public void givenOneJsonFormat_whenDeserialized_thenProductObjectCreated() throws Exception {

		// Given
		Product product = objectMapper.readValue("{\n"
		  + "\"createdAt\": \"2021-06-28 10:44:32\",\n"
		  + "\"updatedAt\": \"2021-06-28 10:44:32\",\n"  
			+ "\"productName\": \"Fan Poster\",\n" 
			+ "\"description\": \"A poster for fans.\",\n" 
			+ "\"category\": \"Poster\",\n" 
			+ "\"price\": 20,\n" 
			+ "\"status\": \"available\",\n" 
			+ "\"image\": \"/img/img.png\",\n" 
			+ "\"rating\": 5,\n" 
			+ "\"numberOfSold\": 150,\n" 
			+ "\"tags\": [\"ENHYPEN\"],\n" 
			+ "\"comments\": [\n"
			+ "   {\n"
			+ "    \"commentUserId\": 1,\n"
			+ "    \"userName\": \"John\",\n"
			+ "    \"userComment\": \"Test\",\n"
			+ "    \"createdAt\": \"2021-06-28 10:44:32\",\n"
			+ "    \"updatedAt\": \"2021-06-28 10:44:32\"\n"
			+ "   }\n"
			+ " ]\n" 
			+ "}", Product.class);

		// Then
		assertEquals("Fan Poster", product.getProductName());
		assertEquals("Poster", product.getCategory());
		assertEquals(20, product.getPrice());
	}

}
