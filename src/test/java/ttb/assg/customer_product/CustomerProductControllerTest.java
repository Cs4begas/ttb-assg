package ttb.assg.customer_product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ttb.assg.common.NotFoundException;
import ttb.assg.customer.constant.CustomerConstants;
import ttb.assg.customer.constant.ProductType;
import ttb.assg.customer_product.model.entity.dto.ProductHoldingsDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerProductController.class)
public class CustomerProductControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerProductService customerProductService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void givenValidRequest_whenCreateProductHolding_thenReturnOk() throws Exception {
        String customerNo = "C00001";
        String staffId = "staff123";
        ProductHoldingsDTO productHoldingsDTO = new ProductHoldingsDTO();
        productHoldingsDTO.setProductType(ProductType.SA);
        productHoldingsDTO.setAccountNo("A00001");
        productHoldingsDTO.setAccountName("Savings Account");
        productHoldingsDTO.setAccountOpenDate(LocalDate.now());

        ProductHoldingsDTO createdProductHolding = new ProductHoldingsDTO();
        createdProductHolding.setProductType(ProductType.SA);
        createdProductHolding.setAccountNo("A00001");
        createdProductHolding.setAccountName("Savings Account");
        createdProductHolding.setAccountOpenDate(LocalDate.now());

        doReturn(createdProductHolding).when(customerProductService).createProductHolding(anyString(), any(ProductHoldingsDTO.class), anyString());

        mockMvc.perform(post("/api/v1/customers/{customerNo}/product-holdings", customerNo)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(productHoldingsDTO))
                        .header(CustomerConstants.STAFF_ID, staffId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productType").value(productHoldingsDTO.getProductType().name()))
                .andExpect(jsonPath("$.accountNo").value(productHoldingsDTO.getAccountNo()))
                .andExpect(jsonPath("$.accountName").value(productHoldingsDTO.getAccountName()))
                .andExpect(jsonPath("$.accountOpenDate").value(productHoldingsDTO.getAccountOpenDate().toString()));
    }

    @Test
    void givenInvalidRequest_whenCreateProductHolding_thenReturnBadRequest() throws Exception {
        String customerNo = "C00001";
        String staffId = "staff123";
        ProductHoldingsDTO productHoldingsDTO = new ProductHoldingsDTO();

        mockMvc.perform(post("/api/v1/customers/{customerNo}/product-holdings", customerNo)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(productHoldingsDTO))
                        .header(CustomerConstants.STAFF_ID, staffId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenMissingStaffIdHeader_whenCreateProductHolding_thenReturnBadRequest() throws Exception {
        String customerNo = "C00001";
        ProductHoldingsDTO productHoldingsDTO = new ProductHoldingsDTO();
        productHoldingsDTO.setProductType(ProductType.AL);
        productHoldingsDTO.setAccountNo("A00001");
        productHoldingsDTO.setAccountName("Savings Account");
        productHoldingsDTO.setAccountOpenDate(LocalDate.now());

        mockMvc.perform(post("/api/v1/customers/{customerNo}/product-holdings", customerNo)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(productHoldingsDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenProductHoldingsExist_whenGetProductHoldingsByCustomerNo_thenReturnOk() throws Exception {
        String customerNo = "C00001";
        List<ProductHoldingsDTO> productHoldingsDTOs = List.of(
                new ProductHoldingsDTO(),
                new ProductHoldingsDTO()
        );
        when(customerProductService.getProductHoldingsByCustomerNo(anyString())).thenReturn(productHoldingsDTOs);

        mockMvc.perform(get("/api/v1/customers/{customerNo}/product-holdings", customerNo)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(productHoldingsDTOs.size()));
    }

    @Test
    void givenNoProductHoldings_whenGetProductHoldingsByCustomerNo_thenReturnNotFound() throws Exception {
        String customerNo = "C00002";
        when(customerProductService.getProductHoldingsByCustomerNo(anyString())).thenThrow(new NotFoundException("Product holdings not found for customer: " + customerNo));

        mockMvc.perform(get("/api/v1/customers/{customerNo}/product-holdings", customerNo)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenCustomerNo_whenGetProductHoldings_thenReturnOk() throws Exception {
        String customerNo = "C00001";
        List<ProductHoldingsDTO> productHoldingsDTOs = List.of(
                new ProductHoldingsDTO(ProductType.SA, "A00001", "Savings Account", LocalDate.now()),
                new ProductHoldingsDTO(ProductType.TD, "A00002", "Term Deposit", LocalDate.now())
        );
        when(customerProductService.getProductHoldings(customerNo, null, null)).thenReturn(productHoldingsDTOs);

        mockMvc.perform(get("/api/v1/customers/product-holdings")
                        .param("customerNo", customerNo)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(productHoldingsDTOs.size()));
    }

    @Test
    public void givenProductType_whenGetProductHoldings_thenReturnOk() throws Exception {
        String productType = ProductType.SA.name();
        List<ProductHoldingsDTO> productHoldingsDTOs = List.of(
                new ProductHoldingsDTO(ProductType.SA, "A00001", "Savings Account", LocalDate.now())
        );
        when(customerProductService.getProductHoldings(null, productType, null)).thenReturn(productHoldingsDTOs);

        mockMvc.perform(get("/api/v1/customers/product-holdings")
                        .param("productType", productType)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(productHoldingsDTOs.size()));
    }

    @Test
    public void givenAccountNo_whenGetProductHoldings_thenReturnOk() throws Exception {
        String accountNo = "A00001";
        List<ProductHoldingsDTO> productHoldingsDTOs = List.of(
                new ProductHoldingsDTO(ProductType.SA, accountNo, "Savings Account", LocalDate.now())
        );
        when(customerProductService.getProductHoldings(null, null, accountNo)).thenReturn(productHoldingsDTOs);

        mockMvc.perform(get("/api/v1/customers/product-holdings")
                        .param("accountNo", accountNo)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(productHoldingsDTOs.size()));
    }

    @Test
    public void givenAllParams_whenGetProductHoldings_thenReturnOk() throws Exception {
        String customerNo = "C00001";
        String productType = ProductType.SA.name();
        String accountNo = "A00001";
        List<ProductHoldingsDTO> productHoldingsDTOs = List.of(
                new ProductHoldingsDTO(ProductType.SA, accountNo, "Savings Account", LocalDate.now())
        );
        when(customerProductService.getProductHoldings(customerNo, productType, accountNo)).thenReturn(productHoldingsDTOs);

        mockMvc.perform(get("/api/v1/customers/product-holdings")
                        .param("customerNo", customerNo)
                        .param("productType", productType)
                        .param("accountNo", accountNo)
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(productHoldingsDTOs.size()));
    }

    @Test
    public void givenNoParameters_whenGetProductHoldings_thenReturnAllProductHoldings() throws Exception {
        List<ProductHoldingsDTO> productHoldingsDTOs = List.of(
                new ProductHoldingsDTO(ProductType.SA, "A00001", "Savings Account", LocalDate.now()),
                new ProductHoldingsDTO(ProductType.TD, "A00002", "Term Deposit", LocalDate.now())
        );
        when(customerProductService.getProductHoldings(null, null, null)).thenReturn(productHoldingsDTOs);

        mockMvc.perform(get("/api/v1/customers/product-holdings")
                        .contentType(CustomerConstants.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(productHoldingsDTOs.size()));
    }
}
