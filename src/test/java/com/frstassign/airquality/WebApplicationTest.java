package com.frstassign.airquality;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class WebApplicationTest {

	@Autowired
    private MockMvc mockMvc;
    
    @Autowired
	private AirqualityController controller;

	@Test
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/"))
                                        .andDo(print())
                                        .andExpect(status().isOk())
                                        .andReturn();

        String content = result.getModelAndView().getViewName();
        assertEquals("airqualityindex", content);
	}
	@Test
	public void badRegion() throws Exception {
		this.mockMvc.perform(get("/setRegion?name=jdnkjan&submit=0"))
                .andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Bad Name")));
	}

	@Test
	public void goodRegion() throws Exception {
		this.mockMvc.perform(get("/setRegion?name=porto&submit=0"))
                .andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Air Quality:")));
	}
}