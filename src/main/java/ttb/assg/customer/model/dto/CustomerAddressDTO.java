package ttb.assg.customer.model.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ttb.assg.customer.constant.AddressType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CustomerAddressDTO {

    private String customerNo;
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
    private String updateBy;
    private LocalDateTime updateDate;
    private LocalDateTime createDate;
    private String createBy;
}
