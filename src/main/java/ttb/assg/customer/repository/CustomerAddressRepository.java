package ttb.assg.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttb.assg.customer.model.entity.CustomerAddress;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {
}
