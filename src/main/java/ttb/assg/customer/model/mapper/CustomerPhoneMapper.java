package ttb.assg.customer.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ttb.assg.customer.model.dto.CustomerPhoneDTO;
import ttb.assg.customer.model.entity.CustomerPhone;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerPhoneMapper {
    CustomerPhoneMapper INSTANCE = Mappers.getMapper(CustomerPhoneMapper.class);

    @Mapping(target = "createBy", expression = "java(staffId)")
    @Mapping(target = "updateBy", expression = "java(staffId)")
    CustomerPhone toCustomerPhoneForCreate(CustomerPhoneDTO customerPhoneDTO, String staffId);

    @Mapping(target = "updateBy", expression = "java(staffId)")
    CustomerPhone toCustomerPhoneForUpdate(CustomerPhoneDTO customerPhoneDTO, String staffId);

    @Mapping(target = "customerNo", source = "customerInfo.customerNo")
    CustomerPhoneDTO toCustomerPhoneDTO(CustomerPhone customerPhone);
}
