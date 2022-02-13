package com.miaoubich.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miaoubich.entity.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer>{

}
