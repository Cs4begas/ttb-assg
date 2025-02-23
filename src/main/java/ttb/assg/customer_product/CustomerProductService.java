package ttb.assg.customer_product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ttb.assg.common.NotFoundException;
import ttb.assg.customer.model.entity.CustomerInfo;
import ttb.assg.customer.repository.CustomerInfoRepository;
import ttb.assg.customer_product.mapper.ProductHoldingsMapper;
import ttb.assg.customer_product.model.entity.ProductHoldings;
import ttb.assg.customer_product.model.entity.dto.ProductHoldingsDTO;

@Service
@RequiredArgsConstructor
public class CustomerProductService {

    private final ProductHoldingsRepository productHoldingsRepository;
    private final CustomerInfoRepository customerInfoRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ProductHoldingsDTO createProductHolding(String customerNo, ProductHoldingsDTO productHoldingsDTO, String staffId) {
        CustomerInfo customerInfo = customerInfoRepository.findByCustomerNo(customerNo);
        if(customerInfo == null) {
            throw new NotFoundException("Customer not found");
        }
        ProductHoldings productHoldings = ProductHoldingsMapper.INSTANCE.toProductHoldings(productHoldingsDTO, customerNo, staffId);

        ProductHoldings savedProductHoldings = productHoldingsRepository.save(productHoldings);
        entityManager.flush(); // Flush changes to the database
        entityManager.refresh(savedProductHoldings);
        return ProductHoldingsMapper.INSTANCE.toProductHoldingsDTO(savedProductHoldings);
    }
}