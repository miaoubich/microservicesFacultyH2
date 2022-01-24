package com.miaoubich.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.miaoubich.entity.Student;
import com.miaoubich.repository.StudentRepository;
import com.miaoubich.request.CreateStudentRequest;
import com.miaoubich.response.CustomResponseMessage;
import com.miaoubich.response.StudentResponse;

@Service
public class StudentService {

	private final Logger logger = LoggerFactory.getLogger(StudentService.class);
	@Autowired
	private StudentRepository studentRepository;

//	@Autowired
//	private WebClient webClient;

//	@Autowired
//	private AddressFeignClient addressFeignClient;

	@Autowired
	private CommonService commonService;

	public CustomResponseMessage addStudent(CreateStudentRequest createStudentRequest) {

		Student student = new Student();
		student.setFirstname(createStudentRequest.getFirstname());
		student.setLastname(createStudentRequest.getLastname());
		student.setEmail(createStudentRequest.getEmail());

		student.setAddressId(createStudentRequest.getAddressId());
		student = studentRepository.save(student);

		StudentResponse studentResponse = new StudentResponse(student);

		// by using WebClient we use the defined bellow method getAddressById
		/*
		 * studentResponse.setAddressResponse(getAddressById(student.getAddressId()));
		 */
		// or we use feignClient
		studentResponse.setAddressResponse(commonService.getAddressById(student.getAddressId()));

//		return studentResponse;

		CustomResponseMessage message = new CustomResponseMessage();
		message.setResponse("Student added successfully!");

		return message;
	}

	public void addStudentList(List<Student> students) {
		studentRepository.saveAll(students);
	}

	public StudentResponse getStudentById(long studentId) {

		// for testing purpose
		logger.info("This log is comming from getStudentById(studentId) method.");

		Student student = studentRepository.findById(studentId).get();
		StudentResponse studentResponse = new StudentResponse(student);

//		studentResponse.setAddressResponse(getAddressById(student.getAddressId()));
		// or we use feignClient
		studentResponse.setAddressResponse(commonService.getAddressById(student.getAddressId()));

		return studentResponse;
	}

	public List<Student> getStudents() {
		logger.info("From student Service.");

		return studentRepository.findAll();
	}

	public Student EditStudent(Student student) {
		Student existStudent = studentRepository.findById(student.getId()).get();

		existStudent.setFirstname(student.getFirstname());
		existStudent.setLastname(student.getLastname());
		existStudent.setEmail(student.getEmail());
		existStudent.setAddressId(student.getAddressId());

		return studentRepository.save(existStudent);

		// To return a studentResponse we do 
		  
		/*
		 * studentRepository.save(existStudent); 
		 * StudentResponse studentResponse = new StudentResponse(existStudent); 
		 * return studentResponse;
		 */
		 
	}

	public ResponseEntity<String> deleteStudent(long studentId) {
		studentRepository.deleteById(studentId);
		
		return ResponseEntity.ok("Student successfully deleted!");
	}

	// Get Address using WebClient
	/*
	 * public AddressResponse getAddressById(long addressId) { Mono<AddressResponse>
	 * address = webClient.get().uri("/" + addressId).retrieve()
	 * .bodyToMono(AddressResponse.class);
	 * 
	 * return address.block(); }
	 */

//	/*
//	 * Because in this method only we are making a call to the address service then
//	 * we'll apply the circuit breaker here
//	 */
//	@CircuitBreaker(name = "addressService", // addressService: is the name we provide for the circuit breaker instance
//												// in application.properties
//			fallbackMethod = "fallbackToGetSingleAddressById")
//	public AddressResponse getAddressById(long addressId) {
//		AddressResponse addressResponse = addressFeignClient.printSingleAddress(addressId);
//		return addressResponse;
//	}
//
//	//the callback method should have the same signature as the one annotated with @CircuitBreaker
//	public AddressResponse fallbackToGetSingleAddressById(long addressId, Throwable e) {// Throwable is optional
//		return new AddressResponse(); //this is a dummy response
//	}
}
