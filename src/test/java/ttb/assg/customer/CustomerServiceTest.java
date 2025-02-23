package ttb.assg.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ttb.assg.common.NotFoundException;
import ttb.assg.customer.model.dto.CustomerDTO;
import ttb.assg.customer.model.entity.CustomerAddress;
import ttb.assg.customer.model.entity.CustomerInfo;
import ttb.assg.customer.model.entity.CustomerPhone;
import ttb.assg.customer.model.mapper.CustomerAddressMapper;
import ttb.assg.customer.model.mapper.CustomerInfoMapper;
import ttb.assg.customer.model.mapper.CustomerPhoneMapper;
import ttb.assg.customer.repository.CustomerAddressRepository;
import ttb.assg.customer.repository.CustomerInfoRepository;
import ttb.assg.customer.repository.CustomerPhoneRepository;
import ttb.assg.utils.JsonFileReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Spy
    private CustomerInfoRepository customerInfoRepository;

    @Spy
    private CustomerAddressRepository customerAddressRepository;

    @Spy
    private CustomerPhoneRepository customerPhoneRepository;

    @InjectMocks
    private CustomerService customerService;

    private static String MOCK_STAFF = "95016";

    @Autowired
    private JsonFileReader jsonFileReader;

    @Test
    public void createCustomer_success() throws IOException {
        CustomerDTO customerDTO = createCustomerSuccessDTO();
        CustomerInfo customerInfo = CustomerInfoMapper.INSTANCE.toCustomerInfoForCreate(customerDTO, MOCK_STAFF);
        List<CustomerAddress> customerAddresses = customerDTO.getAddresses().stream()
                .map(customerAddressDTO -> CustomerAddressMapper.INSTANCE.toCustomerAddressForCreate(customerAddressDTO, MOCK_STAFF))
                .toList();
        List<CustomerPhone> customerPhones = customerDTO.getPhones().stream()
                .map(customerPhoneDTO -> CustomerPhoneMapper.INSTANCE.toCustomerPhoneForCreate(customerPhoneDTO, MOCK_STAFF))
                .toList();


        when(customerInfoRepository.save(any(CustomerInfo.class))).thenReturn(customerInfo);
        when(customerAddressRepository.saveAll(anyList())).thenReturn(customerAddresses);
        when(customerPhoneRepository.saveAll(anyList())).thenReturn(customerPhones);

        CustomerDTO result = customerService.createCustomer(customerDTO, MOCK_STAFF);

        assertNotNull(result);
        assertEquals(customerDTO.getCustomerNo(), result.getCustomerNo());

        verify(customerInfoRepository).save(any(CustomerInfo.class));
        verify(customerAddressRepository).saveAll(anyList());
        verify(customerPhoneRepository).saveAll(anyList());
    }

    @Test
    public void testCreateCustomer_customerExists_throwsException() throws IOException {
        CustomerDTO customerDTO = createCustomerSuccessDTO();
        CustomerInfo existingCustomerInfo = new CustomerInfo();

        when(customerInfoRepository.findByCustomerNo(customerDTO.getCustomerNo()))
                .thenReturn(existingCustomerInfo);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.createCustomer(customerDTO, MOCK_STAFF));
        assertEquals("Customer already exists", exception.getMessage());

        verify(customerInfoRepository, never()).save(any(CustomerInfo.class));
        verify(customerAddressRepository, never()).saveAll(anyList());
        verify(customerPhoneRepository, never()).saveAll(anyList());
    }

    @Test
    public void testCreateCustomer_customerAddressesFail_rollback() throws IOException {
        CustomerDTO customerDTO = createCustomerSuccessDTO();

        when(customerInfoRepository.save(any(CustomerInfo.class))).thenThrow(new RuntimeException("Address save failed"));

        assertThrows(RuntimeException.class, () -> customerService.createCustomer(customerDTO, MOCK_STAFF));

        verify(customerInfoRepository).save(any(CustomerInfo.class));
        verify(customerAddressRepository, never()).saveAll(anyList());
        verify(customerPhoneRepository, never()).saveAll(anyList());
    }

    @Test
    public void testCreateCustomer_customerPhonesFail_rollback() throws IOException {
        CustomerDTO customerDTO = createCustomerSuccessDTO();
        CustomerInfo customerInfo = CustomerInfoMapper.INSTANCE.toCustomerInfoForCreate(customerDTO, MOCK_STAFF);
        List<CustomerAddress> customerAddresses = customerDTO.getAddresses().stream()
                .map(customerAddressDTO -> CustomerAddressMapper.INSTANCE.toCustomerAddressForCreate(customerAddressDTO, MOCK_STAFF))
                .toList();

        when(customerInfoRepository.save(any(CustomerInfo.class))).thenReturn(customerInfo);
        when(customerAddressRepository.saveAll(anyList())).thenReturn(customerAddresses);
        when(customerPhoneRepository.saveAll(anyList())).thenThrow(new RuntimeException("Phone save failed"));

        assertThrows(RuntimeException.class, () -> customerService.createCustomer(customerDTO, MOCK_STAFF));

        verify(customerInfoRepository).save(any(CustomerInfo.class));
        verify(customerAddressRepository).saveAll(anyList());
        verify(customerPhoneRepository).saveAll(anyList());
    }

    private CustomerDTO createCustomerSuccessDTO() throws IOException {
        return  jsonFileReader.readJsonFileToObject("create_customer_success.json", CustomerDTO.class);
    }


    @Test
    public void givenCustomersExist_whenGetCustomers_thenReturnCustomerDTOs() {
        // Given
        List<CustomerInfo> customerInfos = List.of(
                new CustomerInfo("C00001", "CI", "1234567890123", "Mr.", "John", "Doe", "Mr.", "John", "Doe",
                        LocalDate.of(1990, 1, 1), "123", "Acme Corp", new BigDecimal("50000.0"), "test", LocalDateTime.now(), "test", LocalDateTime.now()),
                new CustomerInfo("C00002", "PP", "AB1234567", "Ms.", "Jane", "Smith", "Ms.", "Jane", "Smith",
                        LocalDate.of(1985, 5, 10), "456", "Beta Inc", new BigDecimal("50000.0"), "test", LocalDateTime.now(), "test", LocalDateTime.now())
        );
        when(customerInfoRepository.findAll()).thenReturn(customerInfos);

        // When
        List<CustomerDTO> result = customerService.getCustomers();

        // Then
        assertEquals(2, result.size());
        assertEquals(customerInfos.get(0).getCustomerNo(), result.get(0).getCustomerNo());
        assertEquals(customerInfos.get(1).getCustomerNo(), result.get(1).getCustomerNo());
        // Add more assertions as needed

    }

    @Test
    public void givenNoCustomers_whenGetCustomers_thenThrowNotFoundException() {
        // Given
        when(customerInfoRepository.findAll()).thenReturn(Collections.emptyList());

        // When and Then
        assertThrows(NotFoundException.class, () -> customerService.getCustomers());
    }
}

