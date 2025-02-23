package ttb.assg.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttb.assg.customer.model.entity.CustomerAddress;

import java.util.List;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {

    CustomerAddress findByAddressSeqAndCustomerInfo_CustomerNo(Integer addressSeq, String customerNo);
}
