package ttb.assg.customer_product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ttb.assg.customer_product.model.entity.ProductHoldings;
import ttb.assg.customer_product.model.entity.dto.ProductHoldingsDTO;

@Mapper
public interface ProductHoldingsMapper {
    ProductHoldingsMapper INSTANCE = Mappers.getMapper(ProductHoldingsMapper.class);

    @Mapping(target = "customerInfo.customerNo", source = "customerNo")
    @Mapping(target = "id.productType", source = "productHoldingsDTO.productType")
    @Mapping(target = "id.accountNo", source = "productHoldingsDTO.accountNo")
    @Mapping(target = "createBy", expression = "java(staffId)")
    @Mapping(target = "customerInfo", ignore = true)
    ProductHoldings toProductHoldings(ProductHoldingsDTO productHoldingsDTO, String customerNo, String staffId);

    @Mapping(target = "productType", source = "id.productType")
    @Mapping(target = "accountNo", source = "id.accountNo")
    ProductHoldingsDTO toProductHoldingsDTO(ProductHoldings productHoldings);
}
