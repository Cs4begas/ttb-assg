package ttb.assg.customer.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ttb.assg.customer.model.dto.CustomerPhoneDTO;
import ttb.assg.customer.model.dto.update.CustomerPhoneUpdateDTO;
import ttb.assg.customer.model.entity.CustomerPhone;

@Mapper
public interface CustomerPhoneMapper {
    CustomerPhoneMapper INSTANCE = Mappers.getMapper(CustomerPhoneMapper.class);

    @Mapping(target = "createBy", expression = "java(staffId)")
    @Mapping(target = "updateBy", expression = "java(staffId)")
    CustomerPhone toCustomerPhoneForCreate(CustomerPhoneDTO customerPhoneDTO, String staffId);

    @Mapping(target = "customerInfo", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateBy", expression = "java(staffId)")
    CustomerPhone toCustomerPhoneForUpdate(CustomerPhoneUpdateDTO customerPhoneUpdateDTO, @MappingTarget  CustomerPhone customerPhone, String staffId);

    @Mapping(target = "customerNo", source = "customerInfo.customerNo")
    CustomerPhoneDTO toCustomerPhoneDTO(CustomerPhone customerPhone);
}
