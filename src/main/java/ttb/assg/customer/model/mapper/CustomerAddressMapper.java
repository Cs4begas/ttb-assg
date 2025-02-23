package ttb.assg.customer.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ttb.assg.customer.model.dto.CustomerAddressDTO;
import ttb.assg.customer.model.entity.CustomerAddress;

@Mapper
public interface CustomerAddressMapper {
    CustomerAddressMapper INSTANCE = Mappers.getMapper(CustomerAddressMapper.class);
    @Mapping(target = "createBy", expression = "java(staffId)")
    @Mapping(target = "updateBy", expression = "java(staffId)")
    CustomerAddress toCustomerAddressForCreate(CustomerAddressDTO customerAddressDTO, String staffId);

    @Mapping(target = "updateBy", expression = "java(staffId)")
    CustomerAddress toCustomerAddressForUpdate(CustomerAddressDTO customerAddressDTO, String staffId);

    @Mapping(target = "customerNo", source = "customerInfo.customerNo")
    CustomerAddressDTO toCustomerAddressDTO(CustomerAddress customerAddress);
}
