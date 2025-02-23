package ttb.assg.customer.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_seq")
    private Integer addressSeq;

    @Column(name = "address_type")
    private String addressType;

    @Column(name = "address_no")
    private String addressNo;

    @Column(name = "moo")
    private String moo;

    @Column(name = "building_village")
    private String buildingVillage;

    @Column(name = "floor")
    private String floor;

    @Column(name = "room_no")
    private String roomNo;

    @Column(name = "soi")
    private String soi;

    @Column(name = "road")
    private String road;

    @Column(name = "sub_district")
    private String subDistrict;

    @Column(name = "district")
    private String district;

    @Column(name = "province_code")
    private String provinceCode;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date", insertable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_by")
    private String updateBy;

    @UpdateTimestamp
    @Column(name = "update_date", insertable = false)
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "customer_no")
    private CustomerInfo customerInfo;
}