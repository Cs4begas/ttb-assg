package ttb.assg.customer.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ttb.assg.customer.constant.IdType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {
    private String customerNo;
    @Enumerated(EnumType.STRING)
    @NotNull
    private IdType idType;
    @NotBlank
    private String idNo;
    @NotBlank
    private String titleNameTh;
    @NotBlank
    private String firstNameTh;
    @NotBlank
    private String lastNameTh;
    @NotBlank
    private String titleNameEn;
    @NotBlank
    private String firstNameEn;
    @NotBlank
    private String lastNameEn;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate birthDate;
    @NotBlank
    private String occupationCode;
    @NotBlank
    private String workingPlace;
    @DecimalMin(value = "0.00", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal salary;

    private String updateBy;
    private LocalDateTime updateDate;
    private LocalDateTime createDate;
    private String createBy;

    @Valid
    @NotNull
    private List<CustomerAddressDTO> addresses;

    @Valid
    @NotNull
    private List<CustomerPhoneDTO> phones;
}
