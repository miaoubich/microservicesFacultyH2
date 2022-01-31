package com.miaoubich.unit.test;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // use this to prevent running H2 server
public class AddressServiceApplicationTestsIT {

	/*
	 * The following are framework for integration test: */
	 // 1. TestRestTemplate --> come with @SpringBootTest 
	 // 2. Rest Assured --> a library for calling APIs and validate response calls
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	   //works with SpringBootTest.WebEnvironment.RANDOM_PORT
	/*  @LocalServerPort
		private int port;
	*/

	@Test
	public void getAddressTest() throws Exception {

		String expectedResult = "{\r\n" + 
								"    \"addressId\": 1,\r\n" + 
								"    \"street\": \"rue abbache mohamed\",\r\n" + 
								"    \"city\": \"OUED FODDA\"\r\n" + 
								"}";
		
//		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/address/1", String.class);
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8181/api/address/1", String.class);
		
//		String responseObject = restTemplate.getForObject("http://localhost:8181/api/address/1", String.class);
		
		System.out.println("Response StatusCode(): " + response.getStatusCode()); // 200 OK
		System.out.println("Response Body(): " + response.getBody()); 
//		System.out.println("responseObject: " + responseObject); 
		
		
		JSONAssert.assertEquals(expectedResult, response.getBody(), false); //false refers to strict comparison with spaces
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		
		
	}
}
