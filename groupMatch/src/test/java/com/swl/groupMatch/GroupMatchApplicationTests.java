package com.swl.groupMatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class GroupMatchApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

//	@Test
//	void contextLoads() {
//	}

	private MockMvc mockMvc;
	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

//	@Test
//	public void whenFileUploaded_thenVerifyStatus()
//			throws Exception {
//		MockMultipartFile file
//				= new MockMultipartFile(
//				"file",
//				"hello.txt",
//				MediaType.TEXT_PLAIN_VALUE,
//				"Hello, World!".getBytes()
//		);
//
//		mockMvc.perform(multipart("/upload").file(file))
//				.andExpect(status().isOk());
//	}

	@Test
	public void getStudentByEmail_invalidEmail() throws Exception {
		mockMvc.perform(get("/student/getStudentByEmail/{email}", "invalidemail@nyu.edu")).andExpect(status().isBadRequest());
	}

	@Test
	public void authStudent_invalidEmail() throws Exception {
		mockMvc.perform(get("/student/auth/{email}/{pw}", "invalidemail@nyu.edu", "invalidEmailPassword")).andExpect(status().isBadRequest());
	}

	@Test
	public void viewGroups_invalidEmail() throws Exception {
		mockMvc.perform(get("/student/viewGroups/{email}", "invalidemail@nyu.edu")).andExpect(status().isBadRequest());
	}

	@Test
	public void viewCourses_invalidEmail() throws Exception {
		mockMvc.perform(get("/student/viewCourses/{email}", "invalidemail@nyu.edu")).andExpect(status().isBadRequest());
	}

	@Test
	public void registerStudent_newValidStudent() throws Exception {
		mockMvc.perform(post("/student/register/{email}/{name}/{pw}", "newValidEmail@nyu.edu", "New Student", "newstudentpassword")).andExpect(status().isCreated());
	}

	@Test
	public void registerStudent_existingStudent() throws Exception {
		mockMvc.perform(post("/student/register/{email}/{name}/{pw}", "newValidEmail@nyu.edu", "New Student", "newstudentpassword")).andExpect(status().isBadRequest());
	}

	@Test
	public void delStudent_validStudent() throws Exception {
		mockMvc.perform(delete("/student/del/{email}/{pw}", "newValidEmail@nyu.edu","newstudentpassword")).andExpect(status().isOk());
	}

	@Test
	public void delStudent_invalidStudent() throws Exception {
		mockMvc.perform(delete("/student/del/{email}/{pw}", "invalidEmail@nyu.edu","invalidstudentpassword")).andExpect(status().isBadRequest());
	}

	@Test
	public void delCal_invalidTakesId() throws Exception {
		mockMvc.perform(delete("/student/delCal/{email}/{sem}", "invalidemail@nyu.edu","2023-05")).andExpect(status().isBadRequest());
	}

	@Test
	public void uploadCal_invalidCalendar()
			throws Exception {
		MockMultipartFile file
				= new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);

		mockMvc.perform(multipart("/student/uploadCal").file(file))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void registerAdmin_newValidAdmin() throws Exception {
		mockMvc.perform(post("/admin/register/{user}/{name}/{pw}", "newValidAdmin", "New Admin", "newadminpassword")).andExpect(status().isCreated());
	}

	@Test
	public void delAdmin_validAdmin() throws Exception {
		mockMvc.perform(delete("/admin/del/{user}/{pw}", "newValidAdmin","newadminpassword")).andExpect(status().isOk());
	}

	@Test
	public void delAdmin_invalidAdmin() throws Exception {
		mockMvc.perform(delete("/admin/del/{user}/{pw}", "invalidAdmin","invalidadminpassword")).andExpect(status().isBadRequest());
	}

}
