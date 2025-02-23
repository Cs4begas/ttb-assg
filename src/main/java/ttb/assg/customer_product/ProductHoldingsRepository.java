package ttb.assg.customer_product;

import org.springframework.data.jpa.repository.JpaRepository;
import ttb.assg.customer_product.model.entity.ProductHoldings;

public interface ProductHoldingsRepository extends JpaRepository<ProductHoldings, Integer> {

}
