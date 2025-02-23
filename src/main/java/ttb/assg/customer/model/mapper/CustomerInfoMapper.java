package ttb.assg.customer.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ttb.assg.customer.model.dto.CustomerDTO;
import ttb.assg.customer.model.dto.update.CustomerUpdateDTO;
import ttb.assg.customer.model.entity.CustomerInfo;

@Mapper(uses = {CustomerAddressMapper.class, CustomerPhoneMapper.class})
public interface CustomerInfoMapper {
    CustomerInfoMapper INSTANCE = Mappers.getMapper(CustomerInfoMapper.class);

    @Mapping(target = "createBy", expression = "java(staffId)")
    @Mapping(target = "updateBy", expression = "java(staffId)")
    CustomerInfo toCustomerInfoForCreate(CustomerDTO customerDTO, String staffId);

    @Mapping(target = "customerNo", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", expression = "java(staffId)")
    void toCustomerInfoForUpdate(CustomerUpdateDTO customerUpdateDTO, @MappingTarget CustomerInfo customerInfo, String staffId);

    @Mapping(target = "addresses", source = "customerAddresses")
    @Mapping(target = "phones", source = "customerPhones")
    CustomerDTO toCustomerDTOFetch(CustomerInfo customerInfo);

    CustomerDTO toCustomerDTO(CustomerInfo customerInfo);
}
