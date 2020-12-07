package com.budget.app.controller;

import com.budget.app.entity.BudgetTypes;
import com.budget.app.model.Response;
import com.budget.app.model.budgetType.GetBudgetTypesResponse;
import com.budget.app.responseMessage.ResponseMessage;
import com.budget.app.service.BudgetTypeService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class BudgetTypeControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private BudgetTypeService budgetTypeService;

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
    public void getBudgetTypesTest() throws Exception {

        List<BudgetTypes> types = Arrays.asList(new BudgetTypes(1, "INCOME"),
                new BudgetTypes(2, "EXPENSE"));

        GetBudgetTypesResponse budgetTypes = new GetBudgetTypesResponse(types);

        Mockito
                .when(budgetTypeService.getBudgetTypes())
                .thenReturn(budgetTypes);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/types")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        GetBudgetTypesResponse response = mapFromJson(mvcResult.getResponse().getContentAsString(), GetBudgetTypesResponse.class);

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        Assertions.assertEquals(types.get(0).getType(), response.getBudgetTypes().get(0).getType());
        Assertions.assertEquals(types.get(1).getType(), response.getBudgetTypes().get(1).getType());

    }

    @Test
    public void getBudgetTypesExceptionTest() throws Exception {

        Mockito
                .doThrow(new Exception(ResponseMessage.GET_BUDGET_TYPES_FAILURE.toString()))
                .when(budgetTypeService)
                .getBudgetTypes();

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/types")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        Response response = mapFromJson(mvcResult.getResponse().getContentAsString(), Response.class);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
        Assertions.assertEquals(ResponseMessage.SERVER_ERROR.toString(), response.getMessage());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());

    }

}
