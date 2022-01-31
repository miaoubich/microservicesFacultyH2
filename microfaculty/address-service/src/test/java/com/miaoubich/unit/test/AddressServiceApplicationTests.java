package com.miaoubich.unit.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
	public void createSingleAddressTest() throws Exception {
		CreateAddressRequest addressRequest = addressRequest();
		String jsonString = mapper.writeValueAsString(addressRequest);

		when(addressService.addAddress(any())).thenReturn(addressResponse());

		log.info("Street: " + addressResponse().getStreet());
		log.info("City: " + addressResponse().getCity());

		this.mockMvc.perform(post("/api/address/add").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.city").value(addressResponse().getCity()))
				.andExpect(jsonPath("$.street").value(addressResponse().getStreet()));
	}

	@Test
	public void createListOfAddresses() throws Exception {
		when(addressService.addAddressList(any())).thenReturn(addressList());
		String jsonString = mapper.writeValueAsString(addressList());

		this.mockMvc.perform(post("/api/address/addList").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.[0].id").value(3))
				.andExpect(jsonPath("$.[0].city").value("Paris"))
				.andExpect(jsonPath("$.[0].street").value("Rue des champs elysees"))
				.andExpect(jsonPath("$.[1].id").value(4)).andExpect(jsonPath("$.[1].city").value("Sarcelles"))
				.andExpect(jsonPath("$.[1].street").value("Rue de champagne"));
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

	@Test
	public void getListOfAddresses() throws Exception {
		// POJO to Json
		String jsonAddressList = new Gson().toJson(addressList());

		when(addressService.getAddresses()).thenReturn(addressList());
		this.mockMvc.perform(get("/api/address/getList")).andDo(print()).andExpect(status().isFound())
				// we can check each element its content
				.andExpect(jsonPath("$.[0].street").value("Rue des champs elysees"))
				// or the whole result
				.andExpect(content().json(jsonAddressList));

	}

	@Test
	public void updateAddress() throws Exception {
		String jsonString = mapper.writeValueAsString(buildAddress());
		when(addressService.EditAddress(any())).thenReturn(updatedAddress());

		this.mockMvc.perform(put("/api/address/update").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.street").value(updatedAddress().getStreet()))
				.andExpect(jsonPath("$.city").value(updatedAddress().getCity()));
	}

	@Test
	public void deleteAddress() throws Exception {
		ResponseEntity<String> responseEntity = ResponseEntity.ok("Address successfully deleted!");
		when(addressService.deleteAddress(any())).thenReturn(responseEntity);

		/*
		 * if deleteAddress is a void method (returns nothing) then we do the following:
		 * doNothing.when(addressService).deleteAddress(any());
		 */
		this.mockMvc.perform(delete("/api/address/delete/" + addressId)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(responseEntity.getBody()));
	}
	
	@Test
	public void deleteAddress2() throws Exception {
		ResponseEntity<String> responseEntity = ResponseEntity.ok("Address successfully deleted!");
		when(addressService.deleteAddress(any())).thenReturn(responseEntity);

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/address/delete/{id}", addressId)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(responseEntity.getBody()));
	}

	private Address updatedAddress() {
		Address address = new Address();

		address.setId(addressId);
		address.setStreet("Updated street");
		address.setCity("ZagrebUpdate");

		return address;
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

	private List<Address> addressList() {
		List<Address> addresses = new ArrayList<>();

		Address address1 = new Address();
		address1.setId((long) 3);
		address1.setStreet("Rue des champs elysees");
		address1.setCity("Paris");

		Address address2 = new Address();
		address2.setId((long) 4);
		address2.setStreet("Rue de champagne");
		address2.setCity("Sarcelles");

		addresses.add(address1);
		addresses.add(address2);

		return addresses;
	}
}
