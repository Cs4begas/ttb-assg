package ttb.assg.customer.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ttb.assg.customer.constant.PhoneType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerPhoneDTO {

    private Integer phoneSeq;
    private String customerNo;
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;
    private String phoneNo;
    private String updateBy;
    private LocalDateTime updateDate;
    private LocalDateTime createDate;
    private String createBy;
}
