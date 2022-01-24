package com.miaoubich.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miaoubich.entity.Address;
import com.miaoubich.request.CreateAddressRequest;
import com.miaoubich.response.AddressResponse;
import com.miaoubich.service.AddressService;

@RestController
@RequestMapping("/api/address")
@RefreshScope //allow us to read the test value in case it's been changed in address-service-dev.properties
public class AddressController {

	@Autowired
	private AddressService addressService;
	
//	@Value("${address.test}")
//	private String test;
	

	@PostMapping("/add")
	public AddressResponse createAddress(@RequestBody CreateAddressRequest addressRequest) {
		return addressService.addAddress(addressRequest);
	}

	@PostMapping("/addList")
	public ResponseEntity<List<Address>> addAddresses(@RequestBody List<Address> addresses) {
		return new ResponseEntity<List<Address>>(addressService.addAddressList(addresses), HttpStatus.CREATED);
	}

	@GetMapping("/{addressId}")
	public AddressResponse printSingleAddress(@PathVariable Long addressId) {
		return addressService.getAddressById(addressId);
	}

	@GetMapping("/getList")
	public ResponseEntity<List<Address>> getAllAddresses() {
		return new ResponseEntity<List<Address>>(addressService.getAddresses(), HttpStatus.FOUND);
	}

	@PutMapping("/update")
	public ResponseEntity<Address> updateAddress(@RequestBody Address address) {
		return new ResponseEntity<Address>(addressService.EditAddress(address), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{addressId}")
	public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
		addressService.deleteAddress(addressId);

		return ResponseEntity.ok("Address successfully deleted!");
	}
	
//	@GetMapping("/test")
//	public String test() {
//		return test;
//	}
}
