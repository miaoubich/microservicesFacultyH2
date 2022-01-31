package com.miaoubich.unit.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.miaoubich.entity.Address;
import com.miaoubich.request.CreateAddressRequest;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // use this to prevent running H2 server
public class AddressServiceApplicationTestsIT {

	private final Logger log = LoggerFactory.getLogger(AddressServiceApplicationTestsIT.class);
	/*
	 * The following are framework for integration test:
	 */
	// 1. TestRestTemplate --> come with @SpringBootTest
	// 2. Rest Assured --> a library for calling APIs and validate response calls

	@Autowired
	private TestRestTemplate restTemplate;
	
	private long addressId = 5;

	// works with SpringBootTest.WebEnvironment.RANDOM_PORT
	/*
	 * @LocalServerPort private int port;
	 */

	@Test
	public void createNewAddressTest() {
		CreateAddressRequest addressRequest = addressRequest();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		//HttpEntity do auto-converting Objet to json format
		HttpEntity<CreateAddressRequest> request = new HttpEntity<CreateAddressRequest>(addressRequest, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/api/address/add", request, String.class);
		
		//check what headers are returns if it exists
		log.info("headers: ", response.getHeaders());
		/*
		//if you the list of headers is printed then assert it as following
		assertEquals("expectedHeader", response.getHeaders() //here you get a list of headers
											.get("headerKey") // get the header with key = headerKey
											.get(0)); // get the first header from the list of headers
		*/
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void getAddressTest() throws Exception {

		String expectedResult = "{\r\n" + "    \"addressId\": 1,\r\n" + "    \"street\": \"rue abbache mohamed\",\r\n"
				+ "    \"city\": \"OUED FODDA\"\r\n" + "}";

//		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/address/1", String.class);
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8181/api/address/1",
				String.class);

//		String responseObject = restTemplate.getForObject("http://localhost:8181/api/address/1", String.class);

		log.info("Response StatusCode(): " + response.getStatusCode()); // 200 OK
		log.info("Response Body(): " + response.getBody());
//		System.out.println("responseObject: " + responseObject); 

		JSONAssert.assertEquals(expectedResult, response.getBody(), false); // false refers to strict comparison with
																			// spaces
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
	}

	@Test
	public void getAllAddressesTest() throws Exception {
		String espectedResult = "[{\"id\":1,\"street\":\"rue abbache mohamed\",\"city\":\"OUED FODDA\"},"
								+ "{\"id\":2,\"street\":\"Ulica franko\",\"city\":\"Split\"},"
								+ "{\"id\":3,\"street\":\"Ulica bisha\",\"city\":\"Zagreb\"},"
								+ "{\"id\":4,\"street\":\"MEAN\",\"city\":\"Sibanik\"},"
								+ "{\"id\":5,\"street\":\"Ul dubravacka 10060\",\"city\":\"Zagreb\"}"
								+ "]";

		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8181/api/address/getList", String.class);

		System.out.println("Response Status():" + response.getStatusCode());
		System.out.println("Response body(): " + response.getBody());
		
		JSONAssert.assertEquals(espectedResult, response.getBody(), false);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.FOUND);
	}
	
	private CreateAddressRequest addressRequest() {
		CreateAddressRequest addressRequest = new CreateAddressRequest();
		addressRequest.setStreet(buildAddress().getStreet());
		addressRequest.setCity(buildAddress().getCity());

		return addressRequest;
	}
	
	private Address buildAddress() {
		Address address = new Address();

		address.setId(addressId);
		address.setStreet("Ul dubravacka 10060");
		address.setCity("Zagreb");

		return address;
	}
}

