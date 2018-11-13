package com.upgrade.api.campsite.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.upgrade.api.campsite.CampsiteReservationApplication;
import com.upgrade.api.campsite.dto.ReservationDto;
import com.upgrade.api.campsite.utility.constants.CommonConstants;

/**
 * @author Ashish.Patel
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CampsiteReservationApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/**
 * This Class will be test for CampsiteController layer.
 */
public class CampsiteControllerTest {
	
	private static Gson gson = new Gson();

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

	}

	private static final String UTF8 = "utf-8";
	private static final String responseCode = "$.responseCode";
	private static final String responseMessage = "$.responseMessage";
	private static final String error = "$.error";
	private static final String errorDiscription = "$.errorDiscription";
	
	//get all available dates Testing
	@Test
	public void getAvailabeDates() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.get("/campsite/dates/").contentType(MediaType.APPLICATION_JSON)
				.content("").characterEncoding(UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath(responseCode).value(HttpStatus.OK.value()))
				.andExpect(jsonPath(responseMessage).value(CommonConstants.AVAILABLE_DATE_SUCCESS))
				.andExpect(jsonPath(error).value(false));
	}
	
	//Invalid StartDate and EndDate Testing
	@Test
	public void createInvalidStartDateEndDate() throws Exception{
		
		ReservationDto dto = new ReservationDto("Ashish Patel","patel.ashish610@gmail.com","2018-12-","2018-12-","");
		String input = gson.toJson(dto);
		mockMvc.perform(MockMvcRequestBuilders.post("/campsite/reservation/").contentType(MediaType.APPLICATION_JSON)
				.content(input).characterEncoding(UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath(responseCode).value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
				.andExpect(jsonPath(responseMessage).value(CommonConstants.VALIDATION_FAIL_MESSAGE))
				.andExpect(jsonPath(errorDiscription).value(CommonConstants.START_END_DATE_FORMAT_INVALID))
				.andExpect(jsonPath(error).value(true));
	}
	
	//Invalid date combination Testing
	@Test
	public void createInvalidStartandEndDate() throws Exception{
		
		ReservationDto dto = new ReservationDto("Ashish Patel","patel.ashish610@gmail.com","2018-12-02","2018-12-01","");
		String input = gson.toJson(dto);
		mockMvc.perform(MockMvcRequestBuilders.post("/campsite/reservation/").contentType(MediaType.APPLICATION_JSON)
				.content(input).characterEncoding(UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath(responseCode).value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
				.andExpect(jsonPath(responseMessage).value(CommonConstants.VALIDATION_FAIL_MESSAGE))
				.andExpect(jsonPath(error).value(true));
	}
	
	//Success Reservation Testing
	@Test
	public void createReservationSuccess() throws Exception{
		
		ReservationDto dto = new ReservationDto("Ashish Patel","patel.ashish610@gmail.com","2018-12-01","2018-12-02","");
		String input = gson.toJson(dto);
		mockMvc.perform(MockMvcRequestBuilders.post("/campsite/reservation/").contentType(MediaType.APPLICATION_JSON)
				.content(input).characterEncoding(UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath(responseCode).value(HttpStatus.CREATED.value()))
				.andExpect(jsonPath(error).value(false));
	}
	
//	@Test
//	public void createReservationAlreadybooked() throws Exception{
//		
//		ReservationDto dto = new ReservationDto("Ashish Patel","patel.ashish610@gmail.com","2018-12-01","2018-12-02","");
//		String input = gson.toJson(dto);
//		mockMvc.perform(MockMvcRequestBuilders.post("/campsite/reservation/").contentType(MediaType.APPLICATION_JSON)
//				.content(input).characterEncoding(UTF8).accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath(responseCode).value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
//				.andExpect(jsonPath(responseMessage).value(CommonConstants.VALIDATION_FAIL_MESSAGE))
//				.andExpect(jsonPath(errorDiscription).value(CommonConstants.TIMEFRAME_RESERVED))
//				.andExpect(jsonPath(error).value(true));
//	}
	
	//Update fail reservation Testing
	@Test
	public void updateReservationFail() throws Exception{
		
		ReservationDto dto = new ReservationDto("Ashish Patel","patel.ashish61012@gmail.com","2018-12-01","2018-12-02","");
		String input = gson.toJson(dto);
		mockMvc.perform(MockMvcRequestBuilders.put("/campsite/reservation/44").contentType(MediaType.APPLICATION_JSON)
				.content(input).characterEncoding(UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath(responseCode).value(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath(responseMessage).value(CommonConstants.RESERVATION_ID_NOT_FOUND))
				.andExpect(jsonPath(errorDiscription).value(CommonConstants.RESERVATION_ID_NOT_AVAILABLE))
				.andExpect(jsonPath(error).value(true));
	}
	
	
	//Delete reservation Fail Testing
	@Test
	public void deleteReservationFail() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/campsite/reservation/44").contentType(MediaType.APPLICATION_JSON)
				.content("").characterEncoding(UTF8).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath(responseCode).value(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath(responseMessage).value(CommonConstants.RESERVATION_ID_NOT_FOUND))
				.andExpect(jsonPath(errorDiscription).value(CommonConstants.RESERVATION_ID_NOT_AVAILABLE))
				.andExpect(jsonPath(error).value(true));
	}
	
	
	
}
