package com.budget.app.controller;

import com.budget.app.entity.User;
import com.budget.app.exceptions.IncorrectDetailsException;
import com.budget.app.exceptions.NotFoundException;
import com.budget.app.model.Response;
import com.budget.app.model.user.ForgotPasswordRequest;
import com.budget.app.model.user.LoginResponse;
import com.budget.app.model.user.UpdatePasswordRequest;
import com.budget.app.model.user.UpdateUserRequest;
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

        System.out.println("request: " + mapToJson(user.get()));
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

    @Test
    public void updateUserTest() throws Exception {

        int userId = 1;
        UpdateUserRequest request = new UpdateUserRequest("Test1", "User1", LocalDate.parse("1990-02-02"), "FEMALE");

        Mockito
                .doNothing()
                .when(userService)
                .updateUser(userId, request);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.put("/user/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer" + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Response response = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(ResponseMessage.UPDATE_USER_SUCCESS.toString(), response.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

    }

    @Test
    public void updateUserWithIncorrectIdTest() throws Exception {

        UpdateUserRequest request = new UpdateUserRequest("Test1", "User1", LocalDate.parse("1990-02-02"), "FEMALE");

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString()))
                .when(userService)
                .updateUser(Mockito.anyInt(), Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.put("/user/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Response response = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString(), response.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    public void updateUserWithIncorrectDetailTest() throws Exception {

        UpdateUserRequest request = new UpdateUserRequest("", "User1", LocalDate.parse("1990-02-02"), "FEMALE");

        Mockito
                .doThrow(new Exception())
                .when(userService)
                .updateUser(Mockito.anyInt(), Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.put("/user/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);

    }

    @Test
    public void forgotPasswordTest() throws Exception {

        ForgotPasswordRequest request = new ForgotPasswordRequest("test.user@gmail.com", LocalDate.parse("1990-01-01"), "random1");

        Mockito
                .doNothing()
                .when(userService)
                .forgotPassword(Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/user/forgot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Response response = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(ResponseMessage.PASSWORD_RESET_SUCCESS.toString(), response.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

    }

    @Test
    public void forgotPasswordWithIncorrectEmailTest() throws Exception {

        ForgotPasswordRequest request = new ForgotPasswordRequest("test.user@gmail.com", LocalDate.parse("1990-01-01"), "random1");

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.USER_NOT_FOUND.toString()))
                .when(userService)
                .forgotPassword(Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/user/forgot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Response response = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(ResponseMessage.USER_NOT_FOUND.toString(), response.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());

    }

    @Test
    public void forgotPasswordWithIncorrectDetailsTest() throws Exception {

        ForgotPasswordRequest request = new ForgotPasswordRequest("test.user@gmail.com", LocalDate.parse("1990-01-01"), "random1");

        Mockito
                .doThrow(new IncorrectDetailsException(ResponseMessage.FORGOT_PASSWORD_DETAILS_MISMATCH.toString()))
                .when(userService)
                .forgotPassword(Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/user/forgot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Response response = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        Assertions.assertEquals(ResponseMessage.FORGOT_PASSWORD_DETAILS_MISMATCH.toString(), response.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());

    }

    @Test
    public void updatePasswordTest() throws Exception {

        UpdatePasswordRequest request = new UpdatePasswordRequest("random", "random1");

        Mockito
                .doNothing()
                .when(userService)
                .updatePassword(Mockito.anyInt(), Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.put("/user/password/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Response response = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(ResponseMessage.PASSWORD_RESET_SUCCESS.toString(), response.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

    }

    @Test
    public void updatePasswordWithIncorrectUserId() throws Exception {

        UpdatePasswordRequest request = new UpdatePasswordRequest("random", "random1");

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString()))
                .when(userService)
                .updatePassword(Mockito.anyInt(), Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.put("/user/password/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Response response = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString(), response.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());

    }

    @Test
    public void updatePasswordWithIncorrectDetailsTest() throws Exception {

        UpdatePasswordRequest request = new UpdatePasswordRequest("random", "random1");

        Mockito
                .doThrow(new IncorrectDetailsException(ResponseMessage.OLD_PASSWORD_MISMATCH.toString()))
                .when(userService)
                .updatePassword(Mockito.anyInt(), Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.put("/user/password/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Response response = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        Assertions.assertEquals(ResponseMessage.OLD_PASSWORD_MISMATCH.toString(), response.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());

    }

}
