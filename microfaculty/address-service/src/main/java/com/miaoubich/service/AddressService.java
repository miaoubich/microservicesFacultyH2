package com.miaoubich.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miaoubich.entity.Address;
import com.miaoubich.repository.AddressRepository;
import com.miaoubich.request.CreateAddressRequest;
import com.miaoubich.response.AddressResponse;

@Service
public class AddressService {
	@Autowired
	private AddressRepository addressRepository;

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
		return new AddressResponse(addressRepository.getById(addressId));
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

	public void deleteAddress(Long addressId) {
		addressRepository.deleteById(addressId);
	}
}
