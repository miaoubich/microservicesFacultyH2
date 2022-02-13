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

import com.miaoubich.entity.Grade;
import com.miaoubich.service.GradeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/grade")
@Tag(name = "Students grades")//title the api's in swagger
public class GradeController {

	@Autowired
	private GradeService gradeService;

	@PostMapping("/add")
	@Operation(summary = "Add new grade", responses = {
			@ApiResponse(description = "Add grade success", responseCode = "201",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grade.class))),
			@ApiResponse(description = "Grade not found", responseCode = "409", content = @Content)
	})
	public ResponseEntity<Grade> addGrade(@RequestBody Grade grade) {
		Grade persistedGrade = gradeService.upsert(grade);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(persistedGrade.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);

//		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(persistedGrade);
		return new ResponseEntity<Grade>(persistedGrade, headers, HttpStatus.CREATED);
	}

	@PostMapping("/addGrades")
	@Operation(summary = "Add grades")
	public ResponseEntity<String> addGrades(@RequestBody List<Grade> grades) {
		String persistedList = gradeService.createGrades(grades);

		return new ResponseEntity<String>(persistedList, HttpStatus.CREATED);
	}

	@GetMapping("/{gradeId}")
	@Operation(summary = "Search a grade")
	public ResponseEntity<Grade> getGradeById(@PathVariable int gradeId) {
		Grade grade = gradeService.getGrade(gradeId);
		if (grade != null)
			return new ResponseEntity<Grade>(grade, HttpStatus.FOUND);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found or doesn't exist!");
	}

	@GetMapping("/list")
	@Operation(summary = "Print out all the students grades")
	public ResponseEntity<List<Grade>> getAllGrades() {
		return new ResponseEntity<List<Grade>>(gradeService.getAllGrades(), HttpStatus.FOUND);
	}

	@PutMapping("/update")
	@Operation(summary = "Update an existing grade")
	public ResponseEntity<Grade> updateGrade(@RequestBody Grade grade) {
		return new ResponseEntity<Grade>(gradeService.upsert(grade), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{gradeId}")
	@Operation(summary = "Delete an existing grade")
	public ResponseEntity<String> deleteGrade(@PathVariable int gradeId) {
		return ResponseEntity.ok(gradeService.deleteGrade(gradeId));
	}

}
