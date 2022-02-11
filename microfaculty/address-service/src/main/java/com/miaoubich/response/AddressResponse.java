package com.miaoubich.response;

import com.miaoubich.entity.Address;

public class AddressResponse {

	private long addressId;
	private String street;
	private String city;
	private String environment;
	
	
	
	public AddressResponse(Address address) {
		this.addressId = address.getId();
		this.street = address.getStreet();
		this.city = address.getCity();
		this.environment = address.getEnvironment();
	}
	
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
}
