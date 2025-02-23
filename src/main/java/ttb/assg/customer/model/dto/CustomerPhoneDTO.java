package ttb.assg.customer.model.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ttb.assg.customer.constant.PhoneType;

@Getter
@Setter
@NoArgsConstructor
public class CustomerPhoneDTO {

    private String customerNo;
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;
    private String phoneNo;
}
