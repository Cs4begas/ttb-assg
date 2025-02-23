package ttb.assg.customer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ttb.assg.common.NotFoundException;
import ttb.assg.common.ValidationException;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerInfoRepository customerInfoRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerPhoneRepository customerPhoneRepository;

    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO, String staffId) {
        CustomerInfo existingCustomerInfo = customerInfoRepository.findByCustomerNo(customerDTO.getCustomerNo());
        if (existingCustomerInfo != null) {
            logger.error("{} already exists", customerDTO.getCustomerNo());
            throw new ValidationException("Customer already exists");
        }
        CustomerInfo customerInfo = CustomerInfoMapper.INSTANCE.toCustomerInfoForCreate(customerDTO, staffId);
        customerInfo.setCreateBy(staffId);
        List<CustomerAddress> customerAddresses = customerDTO.getAddresses().stream()
                .map(addressDTO -> CustomerAddressMapper.INSTANCE.toCustomerAddressForCreate(addressDTO, staffId))
                .toList();

        List<CustomerPhone> customerPhones = customerDTO.getPhones().stream()
                .map(phoneDTO -> CustomerPhoneMapper.INSTANCE.toCustomerPhoneForCreate(phoneDTO, staffId))
                .toList();

        customerInfoRepository.save(customerInfo);

        customerAddresses.forEach(customerAddress -> customerAddress.setCustomerInfo(customerInfo));
        customerPhones.forEach(customerPhone -> customerPhone.setCustomerInfo(customerInfo));


        customerAddressRepository.saveAll(customerAddresses);
        customerPhoneRepository.saveAll(customerPhones);

        CustomerDTO customerResponse = CustomerInfoMapper.INSTANCE.toCustomerDTO(customerInfo);
        customerResponse.setAddresses(customerAddresses.stream()
                .map(CustomerAddressMapper.INSTANCE::toCustomerAddressDTO)
                .toList());
        customerResponse.setPhones(customerPhones.stream()
                .map(CustomerPhoneMapper.INSTANCE::toCustomerPhoneDTO)
                .toList());

        logger.info("Customer has been created");

        return customerResponse;
    }

    public List<CustomerDTO> getCustomers() {
        List<CustomerInfo> customerInfos = customerInfoRepository.findAll();

        if(customerInfos.isEmpty()){
            throw new NotFoundException("No customers found");
        }

        List<CustomerDTO> customerDTOS = customerInfos.stream()
                .map(CustomerInfoMapper.INSTANCE::toCustomerDTO)
                .toList();


        return customerDTOS;

    }



}
