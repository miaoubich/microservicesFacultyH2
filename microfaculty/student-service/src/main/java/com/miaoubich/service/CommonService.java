package com.miaoubich.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miaoubich.config.AddressFeignClient;
import com.miaoubich.response.AddressResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CommonService {

	private final Logger logger = LoggerFactory.getLogger(CommonService.class);
	int counter = 1;
	@Autowired
	private AddressFeignClient addressFeignClient;

	/*
	 * Because in this method only we are making a call to the address service then
	 * we'll apply the circuit breaker here
	 */
	@CircuitBreaker(name = "addressService", // addressService: is the name we provide for the circuitBreaker instances
												// in application.properties
			fallbackMethod = "fallbackToGetSingleAddressById")
	public AddressResponse getAddressById(long addressId) {

		logger.info("Counter = " + counter);
		counter++;
		AddressResponse addressResponse = addressFeignClient.printSingleAddress(addressId);
		return addressResponse;
	}

	// the callback method should have the same signature as the one annotated with @CircuitBreaker
	public AddressResponse fallbackToGetSingleAddressById(long addressId, Throwable e) {// Throwable is optional, it's for test only
		logger.error("Exception Error : " + e);
		
		return new AddressResponse(); // this is a dummy response
	}
}
