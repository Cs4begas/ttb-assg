package ttb.assg.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ttb.assg.customer.model.entity.CustomerInfo;

import java.util.List;


public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Integer> {

    CustomerInfo findByCustomerNo(String customerNo);

    @Query("SELECT ci FROM CustomerInfo ci " +
            "LEFT JOIN FETCH ci.customerAddresses ca " +
            "LEFT JOIN FETCH ci.customerPhones cp")
    List<CustomerInfo> findAllWithAddressesAndPhones();
}
