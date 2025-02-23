package ttb.assg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ttb.assg.customer.CustomerController;
import ttb.assg.customer.CustomerService;
import ttb.assg.customer.constant.CustomerConstants;
import ttb.assg.customer.constant.IdType;
import ttb.assg.customer.model.dto.CustomerDTO;

import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(JsonFileReader.class)
public class CustomerControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private JsonFileReader jsonFileReader;

    private CustomerDTO customerDTO;

    private HttpHeaders headers = new HttpHeaders();
    private static String MOCK_STAFF = "95016";



    @BeforeEach
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        customerDTO = createCustomerSuccessDTO();
        headers.add(CustomerConstants.STAFF_ID, MOCK_STAFF);
    }


    @Test
    public void givenValidCustomerDTO_whenCreateCustomer_thenReturnOk() throws Exception {
        CustomerDTO customerDTO = jsonFileReader.readJsonFileToObject("create_customer_success.json", CustomerDTO.class);
        when(customerService.createCustomer(any(CustomerDTO.class), anyString())).thenReturn(customerDTO);

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerNo").value(customerDTO.getCustomerNo()));
    }

    @Test
    public void givenNullIdType_whenCreateCustomer_thenReturnBadRequest() throws Exception {
        customerDTO.setIdType(null);
        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Disabled("This need to use json resource not objectmapper")
    public void givenIdTypeMoreThanThreeCharacters_whenCreateCustomer_thenReturnBadRequest() throws Exception {
        customerDTO.setIdType(IdType.valueOf("TTT"));
        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Disabled("This need to use json resource not objectmapper")
    public void givenInvalidIdTypeCharacters_whenCreateCustomer_thenReturnBadRequest() throws Exception {
        customerDTO.setIdType(IdType.valueOf("TTT"));
        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankIdNo_whenCreateCustomer_thenReturnBadRequest() throws Exception {
        customerDTO.setIdNo("");

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankTitleNameTh_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setTitleNameTh("");

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankFirstNameTh_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setFirstNameTh("");

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankLastNameTh_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setLastNameTh("");

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankTitleNameEn_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setTitleNameEn("");

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankFirstNameEn_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setFirstNameEn("");

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankLastNameEn_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setLastNameEn("");

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidBirthDate_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setBirthDate(null);

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankOccupationCode_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setOccupationCode("");

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankWorkingPlace_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setWorkingPlace("");

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidSalary_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setSalary(BigDecimal.valueOf(-100));

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenZeroSalary_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setSalary(new BigDecimal("0.00"));

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullAddresses_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setAddresses(null);

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullPhones_whenCreateCustomer_thenReturnBadRequest() throws Exception {

        customerDTO.setPhones(null);

        mockMvc.perform(post("/api/v1/customers")
                        .headers(headers)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());
    }

    private CustomerDTO createCustomerSuccessDTO() throws IOException {
        return jsonFileReader.readJsonFileToObject("create_customer_success.json", CustomerDTO.class);
    }
}