package ttb.assg.customer.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_phone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerPhone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_seq")
    private Integer phoneSeq;

    @Column(name = "phone_type")
    private String phoneType;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date", insertable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date", insertable = false)
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "customer_no")
    private CustomerInfo customerInfo;

}