package ttb.assg.customer_product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ttb.assg.common.NotFoundException;
import ttb.assg.common.ValidationException;
import ttb.assg.customer.constant.ProductType;
import ttb.assg.customer.model.entity.CustomerInfo;
import ttb.assg.customer.repository.CustomerInfoRepository;
import ttb.assg.customer_product.model.entity.ProductHoldings;
import ttb.assg.customer_product.model.entity.dto.ProductHoldingsDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerProductServiceTest {

    @Mock
    private CustomerInfoRepository customerInfoRepository;

    @Mock
    private ProductHoldingsRepository productHoldingsRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CustomerProductService customerProductService;

    @Test
    public void givenValidRequest_whenCreateProductHolding_thenReturnSavedProductHoldingDTO() {
        // Given
        String customerNo = "C00001";
        String staffId = "staff123";
        ProductHoldingsDTO productHoldingsDTO = new ProductHoldingsDTO();
        productHoldingsDTO.setProductType(ProductType.IM);
        productHoldingsDTO.setAccountNo("A00001");
        productHoldingsDTO.setAccountName("Savings Account");
        productHoldingsDTO.setAccountOpenDate(LocalDate.now());

        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCustomerNo(customerNo);

        ProductHoldings productHoldings = new ProductHoldings();
        productHoldings.setId(new ProductHoldings.ProductHoldingsId(customerNo, "IM", "A00001"));
        productHoldings.setAccountName("Savings Account");
        productHoldings.setAccountOpenDate(productHoldingsDTO.getAccountOpenDate());

        when(customerInfoRepository.findByCustomerNo(customerNo)).thenReturn(customerInfo);
        when(productHoldingsRepository.saveAndFlush(any(ProductHoldings.class))).thenReturn(productHoldings);
        doNothing().when(entityManager).refresh(any(ProductHoldings.class));

        ProductHoldingsDTO result = customerProductService.createProductHolding(customerNo, productHoldingsDTO, staffId);

        assertEquals(productHoldingsDTO.getProductType(), result.getProductType());
        assertEquals(productHoldingsDTO.getAccountNo(), result.getAccountNo());
        assertEquals(productHoldingsDTO.getAccountName(), result.getAccountName());
        assertEquals(productHoldingsDTO.getAccountOpenDate(), result.getAccountOpenDate());

        verify(customerInfoRepository).findByCustomerNo(customerNo);
        verify(productHoldingsRepository).saveAndFlush(any(ProductHoldings.class));
        verify(entityManager).refresh(any(ProductHoldings.class));
    }

    @Test
    public void givenCustomerNotFound_whenCreateProductHolding_thenThrowNotFoundException() {
        // Given
        String customerNo = "C00002";
        ProductHoldingsDTO productHoldingsDTO = new ProductHoldingsDTO();
        when(customerInfoRepository.findByCustomerNo(customerNo)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> customerProductService.createProductHolding(customerNo, productHoldingsDTO, "staff123"));
    }

    @Test
    public void givenErrorDuringSave_whenCreateProductHolding_thenThrowException() {
        // Given
        String customerNo = "C00001";
        String staffId = "staff123";
        ProductHoldingsDTO productHoldingsDTO = new ProductHoldingsDTO();
        productHoldingsDTO.setProductType(ProductType.TD);
        productHoldingsDTO.setAccountNo("A00001");
        productHoldingsDTO.setAccountName("Savings Account");
        productHoldingsDTO.setAccountOpenDate(LocalDate.now());

        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCustomerNo(customerNo);

        when(customerInfoRepository.findByCustomerNo(customerNo)).thenReturn(customerInfo);
        when(productHoldingsRepository.saveAndFlush(any(ProductHoldings.class))).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> customerProductService.createProductHolding(customerNo, productHoldingsDTO, staffId));
    }

    @Test
    public void givenProductHoldingsExist_whenGetProductHoldingsByCustomerNo_thenReturnProductHoldingsDTOs() {
        // Given
        String customerNo = "C00001";
        List<ProductHoldings> productHoldings = List.of(
                new ProductHoldings(),
                new ProductHoldings()
        );
        when(productHoldingsRepository.findById_CustomerNo(customerNo)).thenReturn(productHoldings);

        List<ProductHoldingsDTO> result = customerProductService.getProductHoldingsByCustomerNo(customerNo);

        assertEquals(2, result.size());
    }

    @Test
    public void givenNoProductHoldings_whenGetProductHoldingsByCustomerNo_thenThrowNotFoundException() {
        String customerNo = "C00002";
        when(productHoldingsRepository.findById_CustomerNo(customerNo)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> customerProductService.getProductHoldingsByCustomerNo(customerNo));
    }

    @Test
    public void givenAllParameters_whenGetProductHoldings_thenReturnProductHoldingsDTOs() {
        String customerNo = "C00001";
        String productType = "SA";
        String accountNo = "A00001";
        List<ProductHoldings> productHoldings = List.of(
                new ProductHoldings(
                        new ProductHoldings.ProductHoldingsId(customerNo, productType, accountNo),
                        "Savings Account",
                        LocalDate.now(),
                        "staff123",
                        "staff123",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        when(productHoldingsRepository.findById_CustomerNoAndId_ProductTypeAndId_AccountNo(customerNo, productType, accountNo))
                .thenReturn(productHoldings);

        List<ProductHoldingsDTO> result = customerProductService.getProductHoldings(customerNo, productType, accountNo);

        assertEquals(1, result.size());
        assertEquals(ProductType.SA, result.get(0).getProductType());
        assertEquals(accountNo, result.get(0).getAccountNo());
    }

    @Test
    public void givenCustomerNoAndProductType_whenGetProductHoldings_thenReturnProductHoldingsDTOs() {
        String customerNo = "C00001";
        String productType = "SA";
        List<ProductHoldings> productHoldings = List.of(
                new ProductHoldings(
                        new ProductHoldings.ProductHoldingsId(customerNo, productType, "A00002"),
                        "Savings Account",
                        LocalDate.now(),
                        "staff123",
                        "staff123",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        when(productHoldingsRepository.findById_CustomerNoAndId_ProductType(customerNo, productType))
                .thenReturn(productHoldings);

        List<ProductHoldingsDTO> result = customerProductService.getProductHoldings(customerNo, productType, "");

        assertEquals(1, result.size());
        assertEquals(productType, result.get(0).getProductType().name());
    }

    @Test
    public void givenCustomerNoAndAccountNo_whenGetProductHoldings_thenReturnProductHoldingsDTOs() {
        String customerNo = "C00001";
        String accountNo = "A00001";
        List<ProductHoldings> productHoldings = List.of(
                new ProductHoldings(
                        new ProductHoldings.ProductHoldingsId(customerNo, "SA", accountNo),
                        "Savings Account",
                        LocalDate.now(),
                        "staff123",
                        "staff123",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        when(productHoldingsRepository.findById_CustomerNoAndId_AccountNo(customerNo, accountNo))
                .thenReturn(productHoldings);

        List<ProductHoldingsDTO> result = customerProductService.getProductHoldings(customerNo, "", accountNo);

        assertEquals(1, result.size());
        assertEquals(accountNo, result.get(0).getAccountNo());
    }

    @Test
    public void givenProductTypeAndAccountNo_whenGetProductHoldings_thenReturnProductHoldingsDTOs() {
        String productType = "SA";
        String accountNo = "A00001";
        List<ProductHoldings> productHoldings = List.of(
                new ProductHoldings(
                        new ProductHoldings.ProductHoldingsId("C00002", productType, accountNo),
                        "Savings Account",
                        LocalDate.now(),
                        "staff123",
                        "staff123",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        when(productHoldingsRepository.findById_ProductTypeAndId_AccountNo(productType, accountNo))
                .thenReturn(productHoldings);

        List<ProductHoldingsDTO> result = customerProductService.getProductHoldings("", productType, accountNo);

        assertEquals(1, result.size());
        assertEquals(ProductType.SA, result.get(0).getProductType());
        assertEquals(accountNo, result.get(0).getAccountNo());
    }

    @Test
    public void givenCustomerNoOnly_whenGetProductHoldings_thenReturnProductHoldingsDTOs() {
        String customerNo = "C00001";
        List<ProductHoldings> productHoldings = List.of(
                new ProductHoldings(
                        new ProductHoldings.ProductHoldingsId(customerNo, "SA", "A00001"),
                        "Savings Account",
                        LocalDate.now(),
                        "staff123",
                        "staff123",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        when(productHoldingsRepository.findById_CustomerNo(customerNo)).thenReturn(productHoldings);

        List<ProductHoldingsDTO> result = customerProductService.getProductHoldings(customerNo, "", "");

        assertEquals(1, result.size());
    }

    @Test
    public void givenProductTypeOnly_whenGetProductHoldings_thenReturnProductHoldingsDTOs() {
        String productType = "SA";
        List<ProductHoldings> productHoldings = List.of(
                new ProductHoldings(
                        new ProductHoldings.ProductHoldingsId("C00001", productType, "A00001"),
                        "Savings Account",
                        LocalDate.now(),
                        "staff123",
                        "staff123",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        when(productHoldingsRepository.findById_ProductType(productType)).thenReturn(productHoldings);

        List<ProductHoldingsDTO> result = customerProductService.getProductHoldings("", productType, "");

        assertEquals(1, result.size());
        assertEquals(productType, result.get(0).getProductType().name());
    }

    @Test
    public void givenAccountNoOnly_whenGetProductHoldings_thenReturnProductHoldingsDTOs() {
        String accountNo = "A00001";
        List<ProductHoldings> productHoldings = List.of(
                new ProductHoldings(
                        new ProductHoldings.ProductHoldingsId("C00001", "SA", accountNo),
                        "Savings Account",
                        LocalDate.now(),
                        "staff123",
                        "staff123",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        when(productHoldingsRepository.findById_AccountNo(accountNo)).thenReturn(productHoldings);

        List<ProductHoldingsDTO> result = customerProductService.getProductHoldings("", "", accountNo);

        assertEquals(1, result.size());
        assertEquals(accountNo, result.get(0).getAccountNo());
    }

    @Test
    public void givenNoParameters_whenGetProductHoldings_thenReturnAllProductHoldings() {
        // Given
        List<ProductHoldings> productHoldings = List.of(
                new ProductHoldings(
                        new ProductHoldings.ProductHoldingsId("C00001", "SA", "A00001"),
                        "Savings Account",
                        LocalDate.now(),
                        "staff123",
                        "staff123",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                new ProductHoldings(
                        new ProductHoldings.ProductHoldingsId("C00002", "TD", "A00002"),
                        "Term Deposit",
                        LocalDate.now(),
                        "staff123",
                        "staff123",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        when(productHoldingsRepository.findAll()).thenReturn(productHoldings);

        List<ProductHoldingsDTO> result = customerProductService.getProductHoldings(null, null, null);

        assertEquals(productHoldings.size(), result.size());
    }

}
