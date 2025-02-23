package ttb.assg.customer.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ttb.assg.customer.model.dto.CustomerDTO;
import ttb.assg.customer.model.entity.CustomerInfo;

@Mapper(uses = {CustomerAddressMapper.class, CustomerPhoneMapper.class})
public interface CustomerInfoMapper {
    CustomerInfoMapper INSTANCE = Mappers.getMapper(CustomerInfoMapper.class);

    @Mapping(target = "createBy", expression = "java(staffId)")
    @Mapping(target = "updateBy", expression = "java(staffId)")
    CustomerInfo toCustomerInfoForCreate(CustomerDTO customerDTO, String staffId);

    @Mapping(target = "updateBy", expression = "java(staffId)")
    CustomerInfo toCustomerInfoForUpdate(CustomerDTO customerDTO, String staffId);

    @Mapping(target = "addresses", source = "customerAddresses")
    @Mapping(target = "phones", source = "customerPhones")
    CustomerDTO toCustomerDTO(CustomerInfo customerInfo);
}
