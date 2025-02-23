package ttb.assg.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttb.assg.customer.model.entity.CustomerInfo;


public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Integer> {

    CustomerInfo findByCustomerNo(String customerNo);
}
