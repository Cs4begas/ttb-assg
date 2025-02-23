package ttb.assg.customer_product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttb.assg.customer_product.model.entity.dto.ProductHoldingsDTO;

@RestController
@RequestMapping("/api/v1/customers/{customerNo}/product-holdings")
@RequiredArgsConstructor
public class CustomerProductController {

    private final CustomerProductService customerProductService;

    @PostMapping
    public ResponseEntity<ProductHoldingsDTO> createProductHolding(
            @PathVariable String customerNo,
            @RequestBody @Valid ProductHoldingsDTO productHoldingsDTO,
            String staffId
    ) {
        ProductHoldingsDTO createdProductHolding = customerProductService.createProductHolding(customerNo, productHoldingsDTO, staffId);
        return ResponseEntity.ok(createdProductHolding);
    }
}
