package ttb.assg.customer_product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ttb.assg.customer_product.model.entity.ProductHoldings;

import java.util.List;

public interface ProductHoldingsRepository extends JpaRepository<ProductHoldings, Integer> {

    @Query("SELECT ph FROM ProductHoldings ph WHERE ph.id =:id")
    ProductHoldings findById(@Param("id") ProductHoldings.ProductHoldingsId id);

    List<ProductHoldings> findById_CustomerNo(String customerNo);

    List<ProductHoldings> findById_ProductType(String productType);

    List<ProductHoldings> findById_AccountNo(String accountNo);

    List<ProductHoldings> findById_CustomerNoAndId_ProductType(String customerNo, String productType);

    List<ProductHoldings> findById_CustomerNoAndId_AccountNo(String customerNo, String accountNo);

    List<ProductHoldings> findById_CustomerNoAndId_ProductTypeAndId_AccountNo(String customerNo, String productType, String accountNo);
    
    List<ProductHoldings> findById_ProductTypeAndId_AccountNo(String productType, String accountNo);
}

