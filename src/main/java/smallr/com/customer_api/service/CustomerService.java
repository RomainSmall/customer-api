package smallr.com.customer_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smallr.com.customer_api.exception.CustomerNotFoundException;
import smallr.com.customer_api.exception.ResourceNotFoundException;
import smallr.com.customer_api.model.Address;
import smallr.com.customer_api.model.Contact;
import smallr.com.customer_api.model.Customer;
import smallr.com.customer_api.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        return customerRepository.findById(id)
                .or(() -> {
                    log.warn("Customer with ID {} not found", id);
                    throw new CustomerNotFoundException(id);
                });
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll();
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        log.info("Creating new customer: {} {}", customer.getFirstName(), customer.getLastName());
        associateCustomerWithAddressAndContact(customer);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return savedCustomer;
    }

    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customerRepository.delete(customer);
        log.info("Customer with ID {} deleted successfully", id);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        log.info("Updating customer with ID: {}", id);

        // Fetch the existing customer
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        // Update customer details
        updateCustomerDetails(existingCustomer, customerDetails);

        // Save and return the updated customer
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        log.info("Customer with ID {} updated successfully", id);
        return updatedCustomer;
    }

    // Helper method to associate customer with address and contact
    private void associateCustomerWithAddressAndContact(Customer customer) {
        if (customer.getAddress() != null) {
            customer.getAddress().setCustomer(customer);
        }
        if (customer.getContact() != null) {
            customer.getContact().setCustomer(customer);
        }
    }

    // Helper method to update customer details including address and contact
    private void updateCustomerDetails(Customer existingCustomer, Customer customerDetails) {
        // Update basic information
        existingCustomer.setFirstName(customerDetails.getFirstName());
        existingCustomer.setLastName(customerDetails.getLastName());
        existingCustomer.setEmail(customerDetails.getEmail());

        // Update address, if provided
        if (customerDetails.getAddress() != null) {
            Address existingAddress = Optional.ofNullable(existingCustomer.getAddress())
                    .orElseGet(() -> {
                        Address newAddress = new Address();
                        existingCustomer.setAddress(newAddress);
                        return newAddress;
                    });
            updateAddressDetails(existingAddress, customerDetails.getAddress());
        }

        // Update contact, if provided
        if (customerDetails.getContact() != null) {
            Contact existingContact = Optional.ofNullable(existingCustomer.getContact())
                    .orElseGet(() -> {
                        Contact newContact = new Contact();
                        existingCustomer.setContact(newContact);
                        return newContact;
                    });
            updateContactDetails(existingContact, customerDetails.getContact());
        }
    }

    // Helper method to update address details
    private void updateAddressDetails(Address existingAddress, Address newAddress) {
        existingAddress.setStreet(newAddress.getStreet());
        existingAddress.setCity(newAddress.getCity());
        existingAddress.setState(newAddress.getState());
        existingAddress.setZip(newAddress.getZip());
        existingAddress.setAddressType(newAddress.getAddressType());
        log.info("Updated address details for customer");
    }

    // Helper method to update contact details
    private void updateContactDetails(Contact existingContact, Contact newContact) {
        existingContact.setPhoneNumber(newContact.getPhoneNumber());
        existingContact.setContactType(newContact.getContactType());
        log.info("Updated contact details for customer");
    }
}
