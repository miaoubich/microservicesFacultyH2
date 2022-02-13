package com.miaoubich;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaoubich.entity.Result;
import com.miaoubich.service.ResultService;

@SpringBootTest
@AutoConfigureMockMvc
public class ResultServiceApplicationWebMvcTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ResultService resultService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void addResultTest() throws Exception {
		Result result = buildResult();
		when(resultService.upsertResult(any(Result.class))).thenReturn(result);
		
		String resultJson = mapper.writeValueAsString(result);
		
		this.mockMvc.perform(post("/api/grade/add").content(resultJson).contentType(MediaType.APPLICATION_JSON))
						.andDo(print())
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.subject").value(result.getSubject()))
						.andExpect(jsonPath("$.mark").value(20))
						.andExpect(header().stringValues("Location", "http://localhost/api/grade/add/1"));
	}

	private Result buildResult() {
		Result result = new Result(1, "Math", 20);
		return result;
	}
}
