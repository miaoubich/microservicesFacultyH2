package com.miaoubich.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.miaoubich.entity.Grade;
import com.miaoubich.repository.GradeRepository;

@Service
public class GradeService {

	@Autowired
	private GradeRepository gradeRepository;

	public Grade upsert(Grade grade) {

		if (getGrade(grade.getId()) != null){
			Grade existingGrade = gradeRepository.findById(grade.getId()).get();
			existingGrade.setMark(grade.getMark());
			existingGrade.setSubject(grade.getSubject());
			gradeRepository.save(existingGrade);

			return existingGrade;
		}
		return gradeRepository.save(grade);
	}

	public Grade getGrade(int id) {
		return gradeRepository.findById(id).orElse(null);
	}

	public String createGrades(List<Grade> grades) {
		gradeRepository.saveAll(grades);

		return "Grades saved successfully!";
	}

	public List<Grade> getAllGrades() {
		return gradeRepository.findAll();
	}

	public String deleteGrade(int gradeId) {

		if (getGrade(gradeId) != null) {
			gradeRepository.deleteById(gradeId);
			return "Grade successfully deleted!";
		}else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade doesn't exist or was already deleted!");
	}
}
