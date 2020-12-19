package com.budget.app.controller;

import com.budget.app.exceptions.NotFoundException;
import com.budget.app.model.Response;
import com.budget.app.model.budgetItem.AddBudgetItemsRequest;
import com.budget.app.model.budgetItem.BudgetItemRequest;
import com.budget.app.responseMessage.ResponseMessage;
import com.budget.app.service.BudgetItemService;
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
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest
public class BudgetItemControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private BudgetItemService budgetItemService;

    @Autowired
    private WebApplicationContext context;

    @Value("${jwt.test.token}")
    private String authToken;

    private static ObjectMapper MAPPER;

    @BeforeAll
    public static void beforeAll() {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setup() {
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
    public void addBudgetItemsTest() throws Exception {

        AddBudgetItemsRequest request = new AddBudgetItemsRequest(1, Arrays.asList(
                new BudgetItemRequest(1, 1, 10000, "Salary", LocalDate.parse("2020-12-01")),
                new BudgetItemRequest(2, 2, 3000, "Grocery", LocalDate.parse("2020-12-03"))
        ));

        Mockito
                .doNothing()
                .when(budgetItemService)
                .addBudgetItems(Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/budget")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response expectedResponse = new Response(HttpStatus.OK.value(), ResponseMessage.ADD_BUDGET_ITEMS_SUCCESS.toString(), LocalDateTime.now());
        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());

    }

    @Test
    public void addBudgetItemsWithIncorrectUserIdTest() throws Exception {

        AddBudgetItemsRequest request = new AddBudgetItemsRequest(1, Arrays.asList(
                new BudgetItemRequest(1, 1, 10000, "Salary", LocalDate.parse("2020-12-01")),
                new BudgetItemRequest(2, 2, 3000, "Grocery", LocalDate.parse("2020-12-03"))
        ));

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString()))
                .when(budgetItemService)
                .addBudgetItems(Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/budget")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response expectedResponse = new Response(HttpStatus.NOT_FOUND.value(), ResponseMessage.USER_WITH_ID_NOT_FOUND.toString(), LocalDateTime.now());
        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());

    }

    @Test
    public void addBudgetItemsWithIncorrectTypeIdTest() throws Exception {

        AddBudgetItemsRequest request = new AddBudgetItemsRequest(1, Arrays.asList(
                new BudgetItemRequest(1, 1, 10000, "Salary", LocalDate.parse("2020-12-01")),
                new BudgetItemRequest(2, 2, 3000, "Grocery", LocalDate.parse("2020-12-03"))
        ));

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.BUDGET_TYPE_NOT_FOUND.toString()))
                .when(budgetItemService)
                .addBudgetItems(Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/budget")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response expectedResponse = new Response(HttpStatus.NOT_FOUND.value(), ResponseMessage.BUDGET_TYPE_NOT_FOUND.toString(), LocalDateTime.now());
        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());

    }

    @Test
    public void addBudgetItemsWithIncorrectCategoryId() throws Exception {

        AddBudgetItemsRequest request = new AddBudgetItemsRequest(1, Arrays.asList(
                new BudgetItemRequest(1, 1, 10000, "Salary", LocalDate.parse("2020-12-01")),
                new BudgetItemRequest(2, 2, 3000, "Grocery", LocalDate.parse("2020-12-03"))
        ));

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.CATEGORY_NOT_FOUND.toString()))
                .when(budgetItemService)
                .addBudgetItems(Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/budget")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response expectedResponse = new Response(HttpStatus.NOT_FOUND.value(), ResponseMessage.CATEGORY_NOT_FOUND.toString(), LocalDateTime.now());
        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());

    }

}
