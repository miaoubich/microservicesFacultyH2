package com.miaoubich.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miaoubich.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
