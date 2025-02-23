package ttb.assg.customer.model.dto.update;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ttb.assg.customer.constant.PhoneType;

@Getter
@Setter
@NoArgsConstructor
public class CustomerPhoneUpdateDTO {
    @NotNull
    @Min(1)
    private Integer phoneSeq;
    private String customerNo;
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;
    private String phoneNo;
}
