package ttb.assg.customer_product.model.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ttb.assg.customer.constant.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductHoldingsDTO {
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    @NotBlank
    private String accountNo;
    @NotBlank
    private String accountName;
    @NotNull
    private LocalDate accountOpenDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateDate;

    public ProductHoldingsDTO(ProductType productType, String accountNo, String accountName, LocalDate accountOpenDate) {
        this.productType = productType;
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.accountOpenDate = accountOpenDate;
    }

}
