package com.tunnelnetwork.KpOnlineStore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KpOnlineStoreApplicationTests {

	@Test
	public void givenOneJsonFormat_whenDeserialized_thenProductObjectsCreated() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		Product product = mapper.readValue("{\n"  
			+ "  \"name\": \"Ceiling Light\",\n" 
			+ "  \"category\": \"Light\",\n" 
			+ "  \"price\": 20\n" 
			+ "}", Product.class);

		assertEquals("Ceiling Light", product.getProductName());
		assertEquals("Light", product.getCategory());
		assertEquals(20, product.getPrice());
	}

}
