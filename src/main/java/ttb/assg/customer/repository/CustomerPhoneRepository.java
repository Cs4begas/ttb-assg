package ttb.assg.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttb.assg.customer.model.entity.CustomerPhone;

public interface CustomerPhoneRepository extends JpaRepository<CustomerPhone, Integer> {
    CustomerPhone findByPhoneSeqAndCustomerInfo_CustomerNo (int phoneSeq, String customerNo);
}
