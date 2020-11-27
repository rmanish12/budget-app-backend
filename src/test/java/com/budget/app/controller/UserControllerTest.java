package com.budget.app.controller;

import com.budget.app.entity.User;
import com.budget.app.model.Response;
import com.budget.app.model.user.LoginRequest;
import com.budget.app.model.user.LoginResponse;
import com.budget.app.model.user.UserInfoOnLogin;
import com.budget.app.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    ObjectMapper MAPPER = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    public void registerUserTest() throws Exception {
        Response response = new Response(HttpStatus.OK.value(), "User has been successfully registered", LocalDateTime.now());

//        User user = new User("abc123@gmail.com", "testabc", "Test", "User", LocalDate.parse("1995-12-15"), "Male", "ROLE_USER");

        String request = "{\n" +
                "\t\"firstName\": \"Test\",\n" +
                "\t\"lastName\": \"User\",\n" +
                "\t\"email\": \"test123.user@gmail.com\",\n" +
                "\t\"password\": \"random\",\n" +
                "\t\"gender\": \"Male\",\n" +
                "\t\"dateOfBirth\": \"1995-12-15\"\n" +
                "}";

        System.out.println("object: " + request);
        MvcResult mvcResult =  mockMvc
                .perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        String content = mvcResult.getResponse().getContentAsString();
//        Response mvcResponse = mapFromJson(content, Response.class);
//        System.out.println("mvcResponse: " +  mvcResponse);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void loginUserTest() throws Exception {

        LoginRequest loginRequest = new LoginRequest("test.user@gmail.com", "random");

        UserInfoOnLogin userInfo = new UserInfoOnLogin(1, "Test", "ROLE_USER");
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAuthToken("abcd");
        loginResponse.setUser(userInfo);

//        Mockito
//                .when(userService.findUserByEmail("test.user@gmail.com"))
//                .thenReturn(new LoginResponse());

        System.out.println("login request: " + mapToJson(loginRequest));
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(loginRequest)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), status);

    }

}
