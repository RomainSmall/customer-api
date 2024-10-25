package smallr.com.customer_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smallr.com.customer_api.model.Contact;

import java.util.List;

@Repository
public interface ContactRepository  extends JpaRepository<Contact, Long> {}
