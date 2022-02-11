package com.miaoubich.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import com.miaoubich.entity.Student;
import com.miaoubich.request.CreateStudentRequest;
import com.miaoubich.response.CustomResponseMessage;
import com.miaoubich.response.StudentResponse;
import com.miaoubich.service.StudentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/student")
@Slf4j
public class StudentController {

	@Autowired
	private StudentService studentService;

	@PostMapping("/add")
	public ResponseEntity<CustomResponseMessage> createStudent(@RequestBody CreateStudentRequest studentRequest) {

//		studentService.addStudent(studentRequest);
		HttpHeaders header = new HttpHeaders();
		header.set("Awesome", "studentDayOne");

		return new ResponseEntity<CustomResponseMessage>(studentService.addStudent(studentRequest), header,
				HttpStatus.CREATED);
	}

	@PostMapping("/addList")
	public ResponseEntity<String> addStudents(@RequestBody List<Student> students) {
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.set("Awesome", "dayTwo");

		studentService.addStudentList(students);
		return new ResponseEntity<String>("Students data stored successfully.", responseHeader, HttpStatus.CREATED);
	}

	@GetMapping("/{studentId}")
	public StudentResponse printSingleStudent(@PathVariable Long studentId) {// (@PathVariable("studentId") Long
																				// studentId)
		try {
			return studentService.getStudentById(studentId);
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getList")
	public ResponseEntity<List<Student>> getAllStudents() {
		return new ResponseEntity<List<Student>>(studentService.getStudents(), HttpStatus.FOUND);
	}

	@PutMapping("/update")
	public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
		return new ResponseEntity<Student>(studentService.EditStudent(student), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{studentId}")
	public ResponseEntity<String> deleteStudent(@PathVariable Long studentId) {
//		studentService.deleteStudent(studentId);
//
//		return ResponseEntity.ok("Student successfully deleted!");
		
		return studentService.deleteStudent(studentId);
	}
}
