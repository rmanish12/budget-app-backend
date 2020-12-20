package com.budget.app.controller;

import com.budget.app.exceptions.NotFoundException;
import com.budget.app.model.Response;
import com.budget.app.model.budgetItem.*;
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

    @Test
    public void getBudgetItemsTest() throws Exception {

        GetBudgetItemsResponse expectedResponse = new GetBudgetItemsResponse("income", 2, Arrays.asList(
                new BudgetItemResponse(1, 10000, "Monthly Salary", LocalDate.parse("2020-12-01"), "Salary"),
                new BudgetItemResponse(2, 5000, "Bonus", LocalDate.parse("2020-12-02"), "Salary")
        ));

        Mockito
                .when(budgetItemService.getBudgetItems(Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/budget/1?fromDate=2020-12-01&toDate=2020-12-30&type=income&sortBy=amount&orderBy=desc&page=0&limit=5")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        GetBudgetItemsResponse actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), GetBudgetItemsResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(expectedResponse.getBudgetType(), actualResponse.getBudgetType());
        Assertions.assertEquals(expectedResponse.getTotalCount(), actualResponse.getTotalCount());
//        Assertions.assertEquals(expectedResponse.getBudgetType(), actualResponse.getBudgetType());

    }

    @Test
    public void getBudgetItemsWithIncorrectUserIdTest() throws Exception {

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString()))
                .when(budgetItemService)
                .getBudgetItems(Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/budget/1?fromDate=2020-12-01&toDate=2020-12-30&type=income&sortBy=amount&orderBy=desc&page=0&limit=5")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response expectedResponse = new Response(HttpStatus.NOT_FOUND.value(), ResponseMessage.USER_WITH_ID_NOT_FOUND.toString(), LocalDateTime.now());
        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());

    }

    @Test
    public void getMonthlyBudgetOverviewTest() throws Exception {

        MonthlyBudgetOverview expectedResponse = new MonthlyBudgetOverview();
        expectedResponse.setTotal(15000);
        expectedResponse.setIncome(20000);
        expectedResponse.setIncome(5000);

        Mockito
                .when(budgetItemService.getMonthlyBudgetOverview(Mockito.anyInt()))
                .thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/budget/monthly/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        MonthlyBudgetOverview actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), MonthlyBudgetOverview.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(expectedResponse.getExpense(), actualResponse.getExpense());
        Assertions.assertEquals(expectedResponse.getIncome(), actualResponse.getIncome());
        Assertions.assertEquals(expectedResponse.getExpense(), actualResponse.getExpense());

    }

    @Test
    public void getMonthlyBudgetOverviewWithIncorrectUserIdTest() throws Exception {

        Response expectedResponse = new Response(HttpStatus.NOT_FOUND.value(), ResponseMessage.USER_WITH_ID_NOT_FOUND.toString(), LocalDateTime.now());

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString()))
                .when(budgetItemService)
                .getMonthlyBudgetOverview(Mockito.anyInt());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/budget/monthly/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());

    }

    @Test
    public void updateBudgetItemTest() throws Exception {

        UpdateBudgetItemRequest request = new UpdateBudgetItemRequest();
        request.setAmount(1000);
        request.setDescription("Updating budget item");
        request.setDateOfTransaction(LocalDate.now());

        Response expectedResponse = new Response(HttpStatus.OK.value(), ResponseMessage.UPDATE_BUDGET_ITEM_SUCCESS.toString(), LocalDateTime.now());

        Mockito
                .doNothing()
                .when(budgetItemService)
                .updateBudgetItem(Mockito.anyInt(), Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.put("/budget/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());

    }

    @Test
    public void updateBudgetItemWithIncorrectUserIdTest() throws Exception {

        UpdateBudgetItemRequest request = new UpdateBudgetItemRequest();
        request.setAmount(1000);
        request.setDescription("Updating budget item");
        request.setDateOfTransaction(LocalDate.now());

        Response expectedResponse = new Response(HttpStatus.NOT_FOUND.value(), ResponseMessage.USER_WITH_ID_NOT_FOUND.toString(), LocalDateTime.now());

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.USER_WITH_ID_NOT_FOUND.toString()))
                .when(budgetItemService)
                .updateBudgetItem(Mockito.anyInt(), Mockito.any());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.put("/budget/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());

    }

    @Test
    public void deleteBudgetItemsTest() throws Exception {

        Response expectedResponse = new Response(HttpStatus.OK.value(), ResponseMessage.DELETE_BUDGET_ITEM_SUCCESS.toString(), LocalDateTime.now());

        Mockito
                .doNothing()
                .when(budgetItemService)
                .deleteBudgetItems(Mockito.anyList());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.delete("/budget?id=1,2,3")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());

    }

    @Test
    public void deleteBudgetItemsWithIncorrectBudgetIdTest() throws Exception {

        Response expectedResponse = new Response(HttpStatus.NOT_FOUND.value(), ResponseMessage.BUDGET_ITEM_NOT_FOUND.toString(), LocalDateTime.now());

        Mockito
                .doThrow(new NotFoundException(ResponseMessage.BUDGET_ITEM_NOT_FOUND.toString()))
                .when(budgetItemService)
                .deleteBudgetItems(Mockito.anyList());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.delete("/budget?id=1,2,3")
                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        Response actualResponse = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assertions.assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());

    }
}
