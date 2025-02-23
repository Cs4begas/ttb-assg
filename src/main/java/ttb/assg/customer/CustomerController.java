package ttb.assg.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttb.assg.customer.constant.CustomerConstants;
import ttb.assg.customer.model.dto.CustomerDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestHeader(value = CustomerConstants.STAFF_ID, required = true) String staffId, @RequestBody @Valid CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO, staffId));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomer() {
        return ResponseEntity.ok(customerService.getCustomers());
    }

}
