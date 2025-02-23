package ttb.assg.customer.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customer_info")
@Getter
@Setter
@NoArgsConstructor
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
    private BigDecimal salary;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date", insertable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_by")
    private String updateBy;

    @UpdateTimestamp
    @Column(name = "update_date", insertable = false)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "customerInfo")
    private List<CustomerAddress> customerAddresses;

    @OneToMany(mappedBy = "customerInfo")
    private List<CustomerPhone> customerPhones;

    public CustomerInfo(String customerNo, String idType, String idNo, String titleNameTh, String firstNameTh, String lastNameTh,
                        String titleNameEn, String firstNameEn, String lastNameEn, LocalDate birthDate, String occupationCode,
                        String workingPlace, BigDecimal salary, String createBy, LocalDateTime createDate, String updateBy,
                        LocalDateTime updateDate) {
        this.customerNo = customerNo;
        this.idType = idType;
        this.idNo = idNo;
        this.titleNameTh = titleNameTh;
        this.firstNameTh = firstNameTh;
        this.lastNameTh = lastNameTh;
        this.titleNameEn = titleNameEn;
        this.firstNameEn = firstNameEn;
        this.lastNameEn = lastNameEn;
        this.birthDate = birthDate;
        this.occupationCode = occupationCode;
        this.workingPlace = workingPlace;
        this.salary = salary;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
    }

}