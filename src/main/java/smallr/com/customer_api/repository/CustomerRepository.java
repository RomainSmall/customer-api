package smallr.com.customer_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smallr.com.customer_api.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}