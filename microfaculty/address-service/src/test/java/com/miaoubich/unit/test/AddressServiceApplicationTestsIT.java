package com.miaoubich.unit.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.miaoubich.entity.Address;
import com.miaoubich.request.CreateAddressRequest;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // Use this to run H2 server at the same
																				// time while running the Test
public class AddressServiceApplicationTestsIT {

	private final Logger log = LoggerFactory.getLogger(AddressServiceApplicationTestsIT.class);
	/*
	 * The following are framework for integration test:
	 */
	// 1. TestRestTemplate --> come with @SpringBootTest
	// 2. Rest Assured --> a library for calling APIs and validate response calls

	@Autowired
	private TestRestTemplate restTemplate;

	private long addressId5 = 5;
	private long addressId6 = 6;
	private long addressId1 = 1;

	// works with SpringBootTest.WebEnvironment.RANDOM_PORT
	/*
	 * @LocalServerPort private int port;
	 */

	@Test
	@Order(1)
	public void createNewAddressTest() throws JSONException {
		CreateAddressRequest addressRequest = addressRequest();

		String expectedResult = "{\"addressId\":5," + "\"street\":\"Ul dubravacka 10060\"," + "\"city\":\"Zagreb\","
				+ "\"environment\":null}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// HttpEntity do auto-converting Object to json format, no need for ObjectMapper
		HttpEntity<CreateAddressRequest> request = new HttpEntity<CreateAddressRequest>(addressRequest, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/api/address/add", request,
				String.class);

		// check what headers are returns if it exists
		log.info("headers: ", response.getHeaders());

		// To print list of headers with key "headerKey"
		// log.info("Headers list: " + response.getHeaders().get("headerKey"));
		// To print the first item from the headers list
		// log.info("Headers list: " + response.getHeaders().get("headerKey").get(0));
		// To assert headers
		// assertEquals("expectedHeader",
		// response.getHeaders().get("headerKey").get(0));

		log.info("response.getBody(): " + response.getBody());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedResult, response.getBody(), false);
	}

	@Test
	@Order(2)
	public void createAddressesTest() throws JSONException {
		String expectedResult = "["
				+ "{\"id\":5,\"street\":\"Ul dubravacka 10060\",\"city\":\"Zagreb\",\"environment\":null},"
				+ "{\"id\":6,\"street\":\"Ulica Marksimir 10000\",\"city\":\"Split\",\"environment\":null}" + "]";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<List<Address>> request = new HttpEntity<List<Address>>(buildAddressList(), headers);

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8181/api/address/addList",
				request, String.class);

		log.info("Headers: " + response.getHeaders().get("addAddressesHeader").get(0));
		log.info("CodeStatus: " + response.getStatusCode());
		log.info("response.getBody(): " + response.getBody());

		assertEquals("fromAddList", response.getHeaders().get("addAddressesHeader").get(0));
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		JSONAssert.assertEquals(expectedResult, response.getBody(), false);
	}

	@Test
	@Order(3)
	public void getAddressTest() throws Exception {

		String expectedResult = "{\"addressId\":1," + "\"street\":\"rue abbache mohamed\"," + "\"city\":\"OUED FODDA\","
				+ "\"environment\":\"8181, null\"}";

//		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/address/1", String.class);
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8181/api/address/1", String.class);

//		String responseObject = restTemplate.getForObject("http://localhost:8181/api/address/1", String.class);

		log.info("Response StatusCode(): " + response.getStatusCode()); // 200 OK
		log.info("Response Body(): " + response.getBody());
//		System.out.println("responseObject: " + responseObject); 

		JSONAssert.assertEquals(expectedResult, response.getBody(), false); // false refers that we don't want strict
																			// comparison with
																			// spaces otherwise put true
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
	}

	@Test
	@Order(4)
	public void getAllAddressesTest() throws Exception {
		String espectedResult = "[{\"id\":1,\"street\":\"rue abbache mohamed\",\"city\":\"OUED FODDA\"},"
				+ "{\"id\":2,\"street\":\"Ulica franko\",\"city\":\"Split\"},"
				+ "{\"id\":3,\"street\":\"Ulica bisha\",\"city\":\"Zagreb\"},"
				+ "{\"id\":4,\"street\":\"MEAN\",\"city\":\"Sibanik\"},"
				+ "{\"id\":5,\"street\":\"Ul dubravacka 10060\",\"city\":\"Zagreb\"}" + "]";

		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8181/api/address/getList",
				String.class);

		log.info("Response Status():" + response.getStatusCode());
		log.info("Response body(): " + response.getBody());

		JSONAssert.assertEquals(espectedResult, response.getBody(), false);
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.FOUND);
	}

	@Test
	@Order(5)
	public void updateAddressTest() throws JSONException {
		String expectedResult = "{" + "\"id\":1," + "\"street\":\"Ulica Korcula update\","
				+ "\"city\":\"Split-Update\"," + "\"environment\":null" + "}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Address> request = new HttpEntity<Address>(updateAddress(), headers);

		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8181/api/address/update",
				HttpMethod.PUT, request, String.class);

		log.info("response.getBody(): " + response.getBody());
		log.info("StatusCodeValue: " + response.getStatusCodeValue());
		log.info("Update Headers: " + response.getHeaders().get("addressHeader").get(0));

		JSONAssert.assertEquals(expectedResult, response.getBody(), false);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("fromUpdateAddress", response.getHeaders().get("addressHeader").get(0));
	}

	@Test
	@Order(6)
	public void deleteAddress() {
		String expectedResult = "Address successfully deleted!";
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8181/api/address/delete/" + addressId1, HttpMethod.DELETE, request,
				String.class);
		
		log.info("response.getBody(): " + response.getBody());
		log.info("statusCode: " + response.getStatusCode());
		log.info("Headers: " + response.getHeaders());
		
		assertEquals(expectedResult, response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("text/plain;charset=UTF-8", response.getHeaders().get("Content-Type").get(0));
	}

	private CreateAddressRequest addressRequest() {
		CreateAddressRequest addressRequest = new CreateAddressRequest();
		addressRequest.setStreet(buildAddress().getStreet());
		addressRequest.setCity(buildAddress().getCity());

		return addressRequest;
	}

	private Address buildAddress() {
		Address address = new Address();

		address.setId(addressId5);
		address.setStreet("Ul dubravacka 10060");
		address.setCity("Zagreb");

		return address;
	}

	private Address buildAddress2() {
		Address address = new Address();

		address.setId(addressId6);
		address.setStreet("Ulica Marksimir 10000");
		address.setCity("Split");

		return address;
	}

	private List<Address> buildAddressList() {
		List<Address> addresses = new ArrayList<>();

		addresses.add(buildAddress());
		addresses.add(buildAddress2());

		return addresses;
	}

	private Address updateAddress() {
		Address address = new Address();

		address.setId(addressId1);
		address.setStreet("Ulica Korcula update");
		address.setCity("Split-Update");

		return address;
	}
}
