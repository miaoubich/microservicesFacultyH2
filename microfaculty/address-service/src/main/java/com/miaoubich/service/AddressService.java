package com.miaoubich.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.miaoubich.entity.Address;
import com.miaoubich.repository.AddressRepository;
import com.miaoubich.request.CreateAddressRequest;
import com.miaoubich.response.AddressResponse;

@Service
public class AddressService {
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private Environment environment;

	private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

	public AddressResponse addAddress(CreateAddressRequest addressRequest) {
		Address address = new Address();

		address.setStreet(addressRequest.getStreet());
		address.setCity(addressRequest.getCity());

		addressRepository.save(address);

		return new AddressResponse(address);
	}

	public List<Address> addAddressList(List<Address> addresses) {
		return addressRepository.saveAll(addresses);
	}

	public AddressResponse getAddressById(Long addressId) {
		//host --> will be the pod name in K8S
		String host = environment.getProperty("HOSTNAME");
		String port = environment.getProperty("local.server.port");
		
		Address address = addressRepository.getById(addressId);
		address.setEnvironment(port + ", " + host);
		
		return new AddressResponse(address);
	}

	public List<Address> getAddresses() {
		return addressRepository.findAll();
	}

	public Address EditAddress(Address address) {
		Address existingAddress = addressRepository.getById(address.getId());

		existingAddress.setStreet(address.getStreet());
		existingAddress.setCity(address.getCity());

		return addressRepository.save(existingAddress);
	}

	public ResponseEntity<String> deleteAddress(Long addressId) {
		addressRepository.deleteById(addressId);
		
		return ResponseEntity.ok("Address successfully deleted!");
	}
}
