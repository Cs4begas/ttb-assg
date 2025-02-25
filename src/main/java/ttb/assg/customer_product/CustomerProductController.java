package ttb.assg.customer_product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttb.assg.customer.constant.CustomerConstants;
import ttb.assg.customer_product.model.entity.dto.ProductHoldingsDTO;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CustomerProductController {

    private final CustomerProductService customerProductService;

    @PostMapping("/api/v1/customers/{customerNo}/product-holdings")
    public ResponseEntity<ProductHoldingsDTO> createProductHolding(
            @PathVariable String customerNo,
            @RequestBody @Valid ProductHoldingsDTO productHoldingsDTO,
            @RequestHeader(value = CustomerConstants.STAFF_ID, required = true) String staffId
    ) {
        ProductHoldingsDTO createdProductHolding = customerProductService.createProductHolding(customerNo, productHoldingsDTO, staffId);
        return ResponseEntity.ok(createdProductHolding);
    }

    @GetMapping("/api/v1/customers/{customerNo}/product-holdings")
    public ResponseEntity<List<ProductHoldingsDTO>> getProductHoldingsByCustomerNo(@PathVariable String customerNo) {
        List<ProductHoldingsDTO> productHoldings = customerProductService.getProductHoldingsByCustomerNo(customerNo);
        return ResponseEntity.ok(productHoldings);
    }

    @GetMapping("/api/v1/customers/product-holdings")
    public ResponseEntity<List<ProductHoldingsDTO>> getProductHoldings(
            @RequestParam(value = "customerNo", required = false) String customerNo,
            @RequestParam(value = "productType", required = false) String productType,
            @RequestParam(value = "accountNo", required = false) String accountNo
    ) {
        List<ProductHoldingsDTO> productHoldings = customerProductService.getProductHoldings(customerNo, productType, accountNo);
        return ResponseEntity.ok(productHoldings);
    }

}
