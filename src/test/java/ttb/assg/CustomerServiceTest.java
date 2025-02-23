package ttb.assg;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ttb.assg.customer.CustomerService;
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

import java.io.IOException;
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
}
