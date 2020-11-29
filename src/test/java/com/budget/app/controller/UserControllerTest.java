package com.budget.app.controller;

import com.budget.app.entity.User;
import com.budget.app.model.Response;
import com.budget.app.model.user.LoginResponse;
import com.budget.app.responseMessage.ResponseMessage;
import com.budget.app.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @Value("${jwt.test.token}")
    private String authToken;

    private Optional<User> user;

    private static ObjectMapper MAPPER;

    @BeforeAll
    public static void beforeAll() {

        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setup() {

        user = Optional.of(new User("test.user@gmail.com", "testabc", "Test", "User", LocalDate.parse("1990-01-01"), "Male", "ROLE_USER"));
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        return MAPPER.readValue(json, clazz);
    }

    @Test
    public void registerUserTest() throws Exception {

        MvcResult mvcResult =  mockMvc
                .perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(user.get())))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        String content = mvcResult.getResponse().getContentAsString();
        Response mvcResponse = mapFromJson(content, Response.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(mvcResponse.getMessage(), ResponseMessage.USER_CREATION_SUCCESS.toString());
        Assertions.assertEquals(mvcResponse.getStatusCode(), HttpStatus.OK.value());
    }

    @Test
    public void authenticateUserTest() throws Exception {

        Mockito
                .when(userService.findUserByEmail("test.user@gmail.com"))
                .thenReturn(user);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/user/who")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        String content = mvcResult.getResponse().getContentAsString();

        LoginResponse result = mapFromJson(content, LoginResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(result.getUser().getFirstName(), "Test");
        Assertions.assertEquals(result.getUser().getRole(), "ROLE_USER");

    }

}
