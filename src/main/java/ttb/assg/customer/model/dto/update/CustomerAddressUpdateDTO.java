package ttb.assg.customer.model.dto.update;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ttb.assg.customer.constant.AddressType;

@Getter
@Setter
@NoArgsConstructor
public class CustomerAddressUpdateDTO {
    @NotNull
    @Min(value = 1)
    private Integer addressSeq;
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    @NotBlank
    private String addressNo;
    private String moo;
    private String buildingVillage;
    private String floor;
    private String roomNo;
    private String soi;
    private String road;
    @NotBlank
    private String subDistrict;
    @NotBlank
    private String district;
    @NotBlank
    private String provinceCode;
    @NotBlank
    private String postalCode;
}
