package com.miaoubich.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miaoubich.entity.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer>{

}
