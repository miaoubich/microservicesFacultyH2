package com.miaoubich.integration.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.miaoubich.controller.AddressController;
import com.miaoubich.response.AddressResponse;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AddressController.class)
class AddressServiceApplicationTests {

	@Autowired
	private AddressController addressController;
	
	@Test
	void getSingleAddressTest() {
		
		AddressResponse address = addressController.printSingleAddress((long) 1);
		
		assertEquals("Dubrovnik", address.getCity());
	}

}
