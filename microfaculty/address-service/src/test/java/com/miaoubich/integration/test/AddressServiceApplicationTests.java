package com.miaoubich.integration.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaoubich.entity.Address;
import com.miaoubich.repository.AddressRepository;
import com.miaoubich.request.CreateAddressRequest;
import com.miaoubich.response.AddressResponse;
import com.miaoubich.service.AddressService;

@SpringBootTest
@AutoConfigureMockMvc
class AddressServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AddressService addressService;
	@MockBean
	private AddressRepository addressRepository;
	
	private final Logger log = LoggerFactory.getLogger(AddressServiceApplicationTests.class);
	private ObjectMapper mapper = new ObjectMapper();
	private long addressId = 1;
	
	@Test
	public void addSingleAddressTest() throws Exception {
		CreateAddressRequest addressRequest = addressRequest();
		String jsonString = mapper.writeValueAsString(addressRequest);
		
		when(addressService.addAddress(any())).thenReturn(addressResponse());  
		
		log.info("Street: " + addressResponse().getStreet());
		log.info("City: " + addressResponse().getCity());
		
		this.mockMvc.perform(post("/api/address/add").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city").value(addressResponse().getCity()))
				.andExpect(jsonPath("$.street").value(addressResponse().getStreet()));
	}
	
	@Test
	void getSingleAddressTest() throws Exception {
		AddressResponse addressResponse = new AddressResponse(buildAddress());
		when(addressService.getAddressById(any())).thenReturn(addressResponse);
		
		this.mockMvc.perform(get("/api/address/" + addressId))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city").value(addressResponse.getCity()))
				.andExpect(jsonPath("$.street").value(addressResponse.getStreet()));
		
	}

	private Address buildAddress() {
		Address address = new Address();
		
		address.setId(addressId);
		address.setStreet("Ul dubravacka 10060");
		address.setCity("Zagreb");
		
		return address;
	}

	private CreateAddressRequest addressRequest() {
		CreateAddressRequest addressRequest = new CreateAddressRequest();
		addressRequest.setStreet(buildAddress().getStreet());
		addressRequest.setCity(buildAddress().getCity());
		
		return addressRequest;
	}
	
	private AddressResponse addressResponse() {
		return new AddressResponse(buildAddress());
	}
}
