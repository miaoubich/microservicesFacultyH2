package com.miaoubich.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.miaoubich.entity.Result;
import com.miaoubich.repository.ResultRepository;

@Service
public class ResultService {

	@Autowired
	private ResultRepository resultRepository;

	public Result upsertResult(Result result) {

		if (getResult(result.getId()) != null){
			Result existingGrade = resultRepository.findById(result.getId()).get();
			existingGrade.setMark(result.getMark());
			existingGrade.setSubject(result.getSubject());
			resultRepository.save(existingGrade);

			return existingGrade;
		}
		return resultRepository.save(result);
	}

	public Result getResult(int resultId) {
		return resultRepository.findById(resultId).orElse(null);
	}

	public String createResults(List<Result> results) {
		resultRepository.saveAll(results);

		return "Grades saved successfully!";
	}

	public List<Result> getAllResults() {
		return resultRepository.findAll();
	}

	public String deleteResult(int resultId) {

		if (getResult(resultId) != null) {
			resultRepository.deleteById(resultId);
			return "Grade successfully deleted!";
		}else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade doesn't exist or was already deleted!");
	}
}
