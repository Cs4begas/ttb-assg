package ttb.assg.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttb.assg.customer.model.entity.CustomerPhone;

import java.util.List;

public interface CustomerPhoneRepository extends JpaRepository<CustomerPhone, Integer> {

}
