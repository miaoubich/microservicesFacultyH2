package com.miaoubich.unit.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.miaoubich.entity.Address;
import com.miaoubich.response.AddressResponse;
import com.miaoubich.service.AddressService;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressServiceWebMvcTestTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AddressService addressService;

	private long addressId = 2;
	
	@Test
	public void getAddressTest() throws Exception {
		String jsonAddress = new Gson().toJson(buildAddress());
		AddressResponse addressResponse = new AddressResponse(buildAddress());
		
		when(addressService.getAddressById(any())).thenReturn(addressResponse);
	
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/address/{addressId}", addressId))
		.andExpect(status().isOk())
		//.andExpect(header().longValue("Content-Length", 26))
		.andExpect(header().stringValues("Content-Type", "\"application/json\""))
		.andExpect(content().json(jsonAddress))
		.andExpect(header().string("ETag", "\"miaoubich\""));
	}
	
	@Test
	public void deleteAddressTest() throws Exception {
		ResponseEntity<String> responseEntity = ResponseEntity.ok("Address successfully deleted!");
		when(addressService.deleteAddress(any())).thenReturn(responseEntity);

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/address/delete/{id}", addressId))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(responseEntity.getBody()));
	}
	
	private Address buildAddress() {
		Address address = new Address();

		address.setId(addressId);
		address.setStreet("Ul dubravacka 10060");
		address.setCity("Zagreb");

		return address;
	}
}
