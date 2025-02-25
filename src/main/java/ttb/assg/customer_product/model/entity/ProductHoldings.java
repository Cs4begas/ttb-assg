package ttb.assg.customer_product.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ttb.assg.customer.model.entity.CustomerInfo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_holdings")
@Getter
@Setter
@NoArgsConstructor
public class ProductHoldings implements Serializable {

    public ProductHoldings(ProductHoldingsId productHoldingsId,String accountName, LocalDate accountOpenDate, String createBy, String updateBy, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = productHoldingsId;
        this.accountName = accountName;
        this.accountOpenDate = accountOpenDate;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    @EmbeddedId
    private ProductHoldingsId id;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_open_date")
    private LocalDate accountOpenDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date", insertable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date", insertable = false)
    private LocalDateTime updateDate;

    @ManyToOne
    @MapsId("customerNo")
    @JoinColumn(name = "customer_no")
    private CustomerInfo customerInfo;


    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductHoldingsId implements Serializable {
        private String customerNo;
        private String productType;
        private String accountNo;
    }
}