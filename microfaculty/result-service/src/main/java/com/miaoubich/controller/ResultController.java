package com.miaoubich.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.miaoubich.entity.Result;
import com.miaoubich.service.ResultService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/grade")
@Tag(name = "Students grades")//title the api's in swagger
public class ResultController {

	@Autowired
	private ResultService resultService;

	@PostMapping("/add")
	@Operation(summary = "Add new grade", responses = {
			@ApiResponse(description = "Add grade success", responseCode = "201",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))),
			@ApiResponse(description = "Grade not found", responseCode = "409", content = @Content)
	})
	public ResponseEntity<Result> addResult(@RequestBody Result result) {
		Result persistedGrade = resultService.upsertResult(result);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(persistedGrade.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);

//		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(persistedGrade);
		return new ResponseEntity<Result>(persistedGrade, headers, HttpStatus.CREATED);
	}

	@PostMapping("/addGrades")
	@Operation(summary = "Add grades")
	public ResponseEntity<String> addResults(@RequestBody List<Result> results) {
		String persistedList = resultService.createResults(results);

		return new ResponseEntity<String>(persistedList, HttpStatus.CREATED);
	}

	@GetMapping("/{gradeId}")
	@Operation(summary = "Search a grade")
	public ResponseEntity<Result> getResultById(@PathVariable int resultId) {
		Result grade = resultService.getResult(resultId);
		if (grade != null)
			return new ResponseEntity<Result>(grade, HttpStatus.FOUND);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found or doesn't exist!");
	}

	@GetMapping("/list")
	@Operation(summary = "Print out all the students grades")
	public ResponseEntity<List<Result>> getAllResultes() {
		return new ResponseEntity<List<Result>>(resultService.getAllResults(), HttpStatus.FOUND);
	}

	@PutMapping("/update")
	@Operation(summary = "Update an existing grade")
	public ResponseEntity<Result> updateResult(@RequestBody Result result) {
		return new ResponseEntity<Result>(resultService.upsertResult(result), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{gradeId}")
	@Operation(summary = "Delete an existing grade")
	public ResponseEntity<String> deleteResult(@PathVariable int resultId) {
		return ResponseEntity.ok(resultService.deleteResult(resultId));
	}

}
