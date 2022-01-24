package com.miaoubich.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.miaoubich.response.AddressResponse;

//To call address service via url
//value: is this current feign client name which we called it address-feign (call it whatever you like)
//Before registering the Address microservice with eureka we use its url
/*@FeignClient(url = "${address.service.url}", value = "address-feign", path="/api/address") */
//After registering Address microservice with eurka we use its name as a value
/*
@FeignClient(value = "address-service", path="/api/address") 
public interface AddressFeignClient {

	@GetMapping("/{addressId}")
	public AddressResponse printSingleAddress(@PathVariable Long addressId);
	
}
*/
//Integrate api-gateway with feign client by changing the 'value' to the name of the api gateway application, this way we call any service through api-gateway
@FeignClient(value = "api-gateway") 
public interface AddressFeignClient {

	@GetMapping("/address-service/api/address/{addressId}")
	public AddressResponse printSingleAddress(@PathVariable Long addressId);
	
}
