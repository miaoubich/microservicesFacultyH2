package com.miaoubich;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miaoubich.controller.StudentController;
import com.miaoubich.entity.Student;
import com.miaoubich.repository.StudentRepository;
import com.miaoubich.request.CreateStudentRequest;
import com.miaoubich.response.CustomResponseMessage;
import com.miaoubich.response.StudentResponse;
import com.miaoubich.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
class StudentServiceApplicationTests {

	private final Logger logger = LoggerFactory.getLogger(StudentServiceApplicationTests.class);
	@Autowired
	private StudentController studentController;
	@MockBean
	private StudentService studentService;
	@MockBean
	private StudentRepository studentRepository;
	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();
	private long studentId = 5;

	@Test
	void addStudentUsingMockitoTest() {
		CreateStudentRequest studentRequest = createStudentRequest();

		CustomResponseMessage expectedResponse = new CustomResponseMessage();
		expectedResponse.setResponse("Student added successfully!");

		when(studentService.addStudent(any())).thenReturn(expectedResponse);
		ResponseEntity<CustomResponseMessage> response = studentController.createStudent(studentRequest);

		assertEquals(response.getBody().getResponse(), expectedResponse.getResponse());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("[Awesome:\"dayOne\"]", response.getHeaders().toString());
	}

	@Test
	void addStudentUsingMockitoAndMockMvcTest() throws Exception {
		CreateStudentRequest studentRequest = createStudentRequest();

		String jsonRequest = mapper.writeValueAsString(studentRequest);

		CustomResponseMessage expectedResponse = new CustomResponseMessage();
		expectedResponse.setResponse("Student added successfully!");

		when(studentService.addStudent(any())).thenReturn(expectedResponse);

		this.mockMvc.perform(post("/api/student/add").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.response").value(expectedResponse.getResponse()));
	}

	@Test
	public void printSingleStudentTest() throws Exception {
		StudentResponse studentResponse = new StudentResponse(buildStudent());
		String jsonstudentResponse = new Gson().toJson(studentResponse);
		
		logger.info("StudentId: " + studentResponse.getId());
		when(studentService.getStudentById(studentId)).thenReturn(studentResponse);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/student/{studentId}", studentResponse.getId())
		// .param("name", "Ali") // add this if there parameters in the url)
		).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value(studentResponse.getFirstName()))
				.andExpect(content().json(jsonstudentResponse));
	}

	@Test
	public void getStudentsUsingMockitoAndMockMvcTest() throws Exception {
		//Convert a list object to json object
		String jsonStudentList = new Gson().toJson(listOfStudents());

		when(studentService.getStudents()).thenReturn(listOfStudents());
		this.mockMvc.perform(get("/api/student/getList"))
		.andDo(print())
			// check the list length
		.andExpect(jsonPath("$.length()", is(2))) 
		.andExpect(status().isFound())
		// check the lastname of the first element from the list
		.andExpect(jsonPath("$.[1].lastname").value("CNN"))
		// Check the resulted list content with the original list
		.andExpect(content().json(jsonStudentList));

	}

	@Test
	public void getStudentsUsingMockitoTest() throws Exception {

//		when(studentRepository.findAll()).thenReturn(Stream.of(student1, student2).collect(Collectors.toList()));
		when(studentService.getStudents()).thenReturn(listOfStudents());

		ResponseEntity<List<Student>> response = studentController.getAllStudents();

		assertEquals(2, studentController.getAllStudents().getBody().size());
		assertEquals(302, response.getStatusCodeValue());
		assertEquals(HttpStatus.FOUND, studentController.getAllStudents().getStatusCode());

	}

	@Test
	public void updateStudentTest() throws Exception {
		Student student = buildStudent();
		Student updateStudent = updateStudent();

		String jsonString = mapper.writeValueAsString(student);

		when(studentService.EditStudent(any())).thenReturn(updateStudent);

		this.mockMvc.perform(put("/api/student/update").contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void deleteStudentTest() throws Exception {

		ResponseEntity<String> responseEntity = ResponseEntity.ok("Student successfully deleted!");

		when(studentService.deleteStudent(studentId)).thenReturn(responseEntity);

		this.mockMvc.perform(delete("/api/student/delete/" + studentId)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(responseEntity.getBody()));

		/*
		 * ResponseEntity<String> response = studentController.deleteStudent(studentId);
		 * assertEquals("Student successfully deleted!", response.getBody());
		 * assertEquals(HttpStatus.OK, response.getStatusCode());
		 * 
		 * logger.info("StatusCode: " + response.getStatusCode());
		 */

	}

	private Student buildStudent() {
		Student student = new Student();

		student.setId(studentId);
		student.setFirstname("Darin");
		student.setLastname("Bouzar");
		student.setEmail("ali@bouzar.org");

		return student;
	}

	private Student updateStudent() {
		Student student = new Student();

		student.setId(studentId);
		student.setFirstname("Lina");
		student.setLastname("Bouzar");
		student.setEmail("lina@bouzar.org");

		return student;
	}

	private CreateStudentRequest createStudentRequest() {
		CreateStudentRequest studentRequest = new CreateStudentRequest();

		studentRequest.setFirstname("Darin");
		studentRequest.setLastname("Bouzar");
		studentRequest.setEmail("ali@bouzar.org");
		studentRequest.setAddressId((long) 6);

		return studentRequest;
	}

	private List<Student> listOfStudents() {
		List<Student> students = new ArrayList<>();

		Student student1 = new Student();
		student1.setFirstname("Ali");
		student1.setLastname("Bouzar");
		student1.setEmail("ali@bouzar.org");
		student1.setAddressId((long) 6);

		Student student2 = new Student();
		student2.setFirstname("Yeah");
		student2.setLastname("CNN");
		student2.setEmail("cnn@bouzar.org");
		student2.setAddressId((long) 5);

		students.add(student1);
		students.add(student2);

		return students;
	}
}
