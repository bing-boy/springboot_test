package com.example.test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.SpringbootStudyApplication;
import com.example.demo.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootStudyApplication.class)
@WebAppConfiguration //开启web上下文测试
public class MockMvcWebTests {

	private static Logger logger = LoggerFactory.getLogger(MockMvcWebTests.class);

	@Autowired
	private WebApplicationContext webApplicationContext; //注入WebApplicationContext

	private MockMvc mockMvc;

	@Before //junit注解，表示在测试方法执行前完成，这里是将webApplicationContext注入给webAppContextSetup方法，然后调用build方法产生一个MockMvc实例，赋给变量mockMvc，从而将变量供给测试方法使用
	public void setupMockMvc() {
		logger.info("setupMockMvc() 构建mockmvc");
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity()) //为mockmvc开启springsecurity支持
				.build();
	}

	/*@Test
	public void homePage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/userList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("users"))
				.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.is(Matchers.empty())));
	
	}*/

	@Test
	@WithMockUser(username = "bing", password = "bing", roles = "READER")
	//@WithUserDetails("bing")
	public void homePage() throws Exception {
		mockMvc.perform(get("/user/")).andExpect(status().isOk())
				.andExpect(view().name("userList"))
				.andExpect(model().attributeExists("users")).andExpect(model().attribute("users", is(empty())))
				.andDo(print());

	}

	@Test
	public void postUser() throws Exception {
		mockMvc.perform(post("/user/").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "bingboy")
				.param("pass", "bing").param("age", "20")).andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/user/")).andDo(print());
		
		User expectUser = new User();
		expectUser.setId(1L);
		expectUser.setName("bingboy");
		expectUser.setPass("bing");
		expectUser.setAge(20);
		
		mockMvc.perform(get("/user/")).andExpect(status().isOk()).andExpect(view().name("userList"))
				.andExpect(model().attributeExists("users"))
				.andExpect(model().attribute("users",
						contains(samePropertyValuesAs(expectUser))))
				.andDo(print());
	}

}
