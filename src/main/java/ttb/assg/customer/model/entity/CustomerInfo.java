package ttb.assg.customer.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerInfo implements Serializable {

    @Id
    @Column(name = "customer_no")
    private String customerNo;

    @Column(name = "id_type")
    private String idType;

    @Column(name = "id_no")
    private String idNo;

    @Column(name = "title_name_th")
    private String titleNameTh;

    @Column(name = "first_name_th")
    private String firstNameTh;

    @Column(name = "last_name_th")
    private String lastNameTh;

    @Column(name = "title_name_en")
    private String titleNameEn;

    @Column(name = "first_name_en")
    private String firstNameEn;

    @Column(name = "last_name_en")
    private String lastNameEn;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "occupation_code")
    private String occupationCode;

    @Column(name = "working_place")
    private String workingPlace;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date", insertable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_by")
    private String updateBy;

    @UpdateTimestamp
    @Column(name = "update_date", insertable = false)
    private LocalDateTime updateDate;

}