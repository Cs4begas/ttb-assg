package ttb.assg.customer_product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ttb.assg.common.NotFoundException;
import ttb.assg.common.ValidationException;
import ttb.assg.customer.model.entity.CustomerInfo;
import ttb.assg.customer.repository.CustomerInfoRepository;
import ttb.assg.customer_product.mapper.ProductHoldingsMapper;
import ttb.assg.customer_product.model.entity.ProductHoldings;
import ttb.assg.customer_product.model.entity.dto.ProductHoldingsDTO;

import java.util.ArrayList;
import java.util.List;

import static ttb.assg.utils.CustomerUtils.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class CustomerProductService {

    private final ProductHoldingsRepository productHoldingsRepository;
    private final CustomerInfoRepository customerInfoRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public ProductHoldingsDTO createProductHolding(String customerNo, ProductHoldingsDTO productHoldingsDTO, String staffId) {
        CustomerInfo customerInfo = customerInfoRepository.findByCustomerNo(customerNo);
        if (customerInfo == null) {
            throw new NotFoundException("Customer not found");
        }
        ProductHoldings productHoldings = ProductHoldingsMapper.INSTANCE.toProductHoldings(productHoldingsDTO, customerNo, staffId);
        ProductHoldings savedProductHolding = productHoldingsRepository.saveAndFlush(productHoldings);
        entityManager.refresh(savedProductHolding);
        ProductHoldingsDTO savedProductHoldingDTO = ProductHoldingsMapper.INSTANCE.toProductHoldingsDTO(savedProductHolding);
        return savedProductHoldingDTO;
    }

    public List<ProductHoldingsDTO> getProductHoldingsByCustomerNo(String customerNo) {
        List<ProductHoldings> productHoldings = productHoldingsRepository.findById_CustomerNo(customerNo);
        if (productHoldings.isEmpty()) {
            throw new NotFoundException("Product holdings not found for customer: " + customerNo);
        }
        return productHoldings.stream()
                .map(ProductHoldingsMapper.INSTANCE::toProductHoldingsDTO)
                .toList();
    }

    public List<ProductHoldingsDTO> getProductHoldings(String customerNo, String productType, String accountNo) {
        List<ProductHoldingsDTO> productHoldingsDTOList = new ArrayList<>();
        List<ProductHoldings> producHoldingList =  getProductHoldingsByCriteria(customerNo, productType, accountNo);
        producHoldingList.forEach(productHoldings -> {
            ProductHoldingsDTO productHoldingsDTO = ProductHoldingsMapper.INSTANCE.toProductHoldingsDTO(productHoldings);
            productHoldingsDTOList.add(productHoldingsDTO);
        });

        return productHoldingsDTOList;
    }

    private List<ProductHoldings> getProductHoldingsByCriteria(String customerNo, String productType, String accountNo) {
        List<ProductHoldings> productHoldingsList;
        if (!isNullOrBlank(customerNo) && !isNullOrBlank(productType) && !isNullOrBlank(accountNo)) {
            productHoldingsList = productHoldingsRepository.findById_CustomerNoAndId_ProductTypeAndId_AccountNo(customerNo, productType, accountNo);
        } else if (!isNullOrBlank(customerNo) && !isNullOrBlank(productType)) {
            productHoldingsList = productHoldingsRepository.findById_CustomerNoAndId_ProductType(customerNo, productType);
        } else if (!isNullOrBlank(customerNo) && !isNullOrBlank(accountNo)) {
            productHoldingsList = productHoldingsRepository.findById_CustomerNoAndId_AccountNo(customerNo, accountNo);
        } else if (!isNullOrBlank(productType) && !isNullOrBlank(accountNo) ) {
            productHoldingsList = productHoldingsRepository.findById_ProductTypeAndId_AccountNo(productType, accountNo);
        } else if (!isNullOrBlank(productType)) {
            productHoldingsList = productHoldingsRepository.findById_ProductType(productType);
        } else if (!isNullOrBlank(accountNo)) {
            productHoldingsList = productHoldingsRepository.findById_AccountNo(accountNo);
        } else if (!isNullOrBlank(customerNo)) {
            productHoldingsList = productHoldingsRepository.findById_CustomerNo(customerNo);
        } else {
            productHoldingsList = productHoldingsRepository.findAll();
        }
        return productHoldingsList;
    }

}