package ttb.assg.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ttb.assg.common.NotFoundException;
import ttb.assg.customer.model.dto.CustomerDTO;
import ttb.assg.customer.model.dto.update.CustomerAddressUpdateDTO;
import ttb.assg.customer.model.dto.update.CustomerPhoneUpdateDTO;
import ttb.assg.customer.model.dto.update.CustomerUpdateDTO;
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

    @Test
    public void givenCustomerExists_whenGetCustomerByCustomerNo_thenReturnCustomerDTO() {
        String customerNo = "C00001";
        CustomerInfo customerInfo = new CustomerInfo(
                customerNo, "CI", "1234567890123", "Mr.", "John", "Doe", "Mr.", "John", "Doe",
                LocalDate.of(1990, 1, 1), "123", "Acme Corp", new BigDecimal("50000.0"), "test", LocalDateTime.now(), "test", LocalDateTime.now());
        when(customerInfoRepository.findByCustomerNo(customerNo)).thenReturn(customerInfo);

        CustomerDTO result = customerService.getCustomerByCustomerNo(customerNo);

        assertEquals(customerNo, result.getCustomerNo());

    }

    @Test
    public void givenCustomerDoesNotExist_whenGetCustomerByCustomerNo_thenThrowNotFoundException() {
        String customerNo = "C00002";

        when(customerInfoRepository.findByCustomerNo(customerNo)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> customerService.getCustomerByCustomerNo(customerNo));
    }


    @Test
    public void givenExistingCustomer_whenUpdateCustomer_thenCustomerUpdated() {
        String customerNo = "C00001";
        String staffId = "staff123";
        CustomerInfo existingCustomer = new CustomerInfo(
                customerNo, "CI", "1234567890123", "Mr.", "John", "Doe", "Mr.", "John", "Doe",
                LocalDate.of(1990, 1, 1), "123", "Acme Corp", new BigDecimal("50000.0"), "test", LocalDateTime.now(), "test", LocalDateTime.now()
        );
        CustomerUpdateDTO updateDTO = getCustomerUpdateDTO();

        ArgumentCaptor<List<CustomerAddress>> addressCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<CustomerPhone>> phoneCaptor = ArgumentCaptor.forClass(List.class);

        when(customerInfoRepository.findByCustomerNo(customerNo)).thenReturn(existingCustomer);
        when(customerAddressRepository.findByAddressSeqAndCustomerInfo_CustomerNo(1, customerNo)).thenReturn(new CustomerAddress());
        when(customerPhoneRepository.findByPhoneSeqAndCustomerInfo_CustomerNo(1, customerNo)).thenReturn(new CustomerPhone());
        when(customerInfoRepository.save(any(CustomerInfo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerDTO updatedCustomer = customerService.updateCustomer(customerNo, updateDTO, staffId);

        verify(customerAddressRepository).saveAll(addressCaptor.capture());
        verify(customerPhoneRepository).saveAll(phoneCaptor.capture());

        List<CustomerAddress> capturedAddresses = addressCaptor.getValue();
        List<CustomerPhone> capturedPhones = phoneCaptor.getValue();

        assertEquals("9999999999999", updatedCustomer.getIdNo());
        assertEquals(1, capturedAddresses.size());
        assertEquals("11111", capturedAddresses.get(0).getPostalCode());

        assertEquals(1, capturedPhones.size());
        assertEquals("0909090909", capturedPhones.get(0).getPhoneNo());
    }

    private static CustomerUpdateDTO getCustomerUpdateDTO() {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        updateDTO.setIdNo("9999999999999");

        CustomerAddressUpdateDTO addressUpdateDTO = new CustomerAddressUpdateDTO();
        addressUpdateDTO.setAddressSeq(1);
        addressUpdateDTO.setPostalCode("11111"); // Update postalCode
        updateDTO.setAddresses(List.of(addressUpdateDTO));

        CustomerPhoneUpdateDTO phoneUpdateDTO = new CustomerPhoneUpdateDTO();
        phoneUpdateDTO.setPhoneSeq(1);
        phoneUpdateDTO.setPhoneNo("0909090909"); // Update phoneNo
        updateDTO.setPhones(List.of(phoneUpdateDTO));
        return updateDTO;
    }

    @Test
    public void givenNonExistingCustomer_whenUpdateCustomer_thenThrowNotFoundException() {
        String customerNo = "C00002";
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        when(customerInfoRepository.findByCustomerNo(customerNo)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> customerService.updateCustomer(customerNo, updateDTO, "staff123"));
    }

    @Test
    public void givenNonExistingAddress_whenUpdateCustomer_thenThrowNotFoundException() {
        String customerNo = "C00001";
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        CustomerAddressUpdateDTO addressUpdateDTO = new CustomerAddressUpdateDTO();
        addressUpdateDTO.setAddressSeq(999); // Non-existing address
        updateDTO.setAddresses(List.of(addressUpdateDTO));
        CustomerInfo existingCustomer = new CustomerInfo(
                customerNo, "CI", "1234567890123", "Mr.", "John", "Doe", "Mr.", "John", "Doe",
                LocalDate.of(1990, 1, 1), "123", "Acme Corp", new BigDecimal("50000.0"), "test", LocalDateTime.now(), "test", LocalDateTime.now()
        );
        when(customerInfoRepository.findByCustomerNo(customerNo)).thenReturn(existingCustomer);
        when(customerAddressRepository.findByAddressSeqAndCustomerInfo_CustomerNo(999, customerNo)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> customerService.updateCustomer(customerNo, updateDTO, "staff123"));
    }

    @Test
    public void givenNonExistingPhone_whenUpdateCustomer_thenThrowNotFoundException() {
        String customerNo = "C00001";
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        CustomerPhoneUpdateDTO phoneUpdateDTO = new CustomerPhoneUpdateDTO();
        phoneUpdateDTO.setPhoneSeq(999); // Non-existing phone
        updateDTO.setPhones(List.of(phoneUpdateDTO));
        CustomerInfo existingCustomer = new CustomerInfo(
                customerNo, "CI", "1234567890123", "Mr.", "John", "Doe", "Mr.", "John", "Doe",
                LocalDate.of(1990, 1, 1), "123", "Acme Corp", new BigDecimal("50000.0"), "test", LocalDateTime.now(), "test", LocalDateTime.now()
        );
        when(customerInfoRepository.findByCustomerNo(customerNo)).thenReturn(existingCustomer);
        when(customerPhoneRepository.findByPhoneSeqAndCustomerInfo_CustomerNo(999, customerNo)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> customerService.updateCustomer(customerNo, updateDTO, "staff123"));
    }
}

