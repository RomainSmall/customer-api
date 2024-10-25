package smallr.com.customer_api.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smallr.com.customer_api.model.Customer;
import smallr.com.customer_api.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    // Create a new customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        logger.info("Creating a new customer with email: {}", customer.getEmail());
        Customer createdCustomer = customerService.createCustomer(customer);
        logger.info("Customer created successfully with ID: {}", createdCustomer.getId());
        return ResponseEntity.ok(createdCustomer);
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        logger.info("Fetching all customers");
        List<Customer> customers = customerService.getAllCustomers();
        logger.info("Total customers found: {}", customers.size());
        return ResponseEntity.ok(customers);
    }

    // Get a customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Customer>> getCustomer(@PathVariable Long id) {
        logger.info("Fetching customer with ID: {}", id);
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            logger.info("Customer found with ID: {}", id);
            return ResponseEntity.ok(customer);
        } else {
            logger.warn("Customer not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing customer
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        logger.info("Updating customer with ID: {}", id);
        Optional<Customer> existingCustomer = customerService.getCustomerById(id);

        if (existingCustomer.isPresent()) {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            logger.info("Customer updated successfully with ID: {}", id);
            return ResponseEntity.ok(updatedCustomer);
        } else {
            logger.warn("Customer not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a customer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCustomer(@PathVariable Long id) {
        logger.info("Deleting customer with ID: {}", id);
        Optional<Customer> customer = customerService.getCustomerById(id);

        if (customer.isPresent()) {
            customerService.deleteCustomer(id);
            logger.info("Customer deleted successfully with ID: {}", id);

            // Return a success message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Customer deleted successfully with ID: " + id);
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Customer not found with ID: {}", id);

            // Return an error message
            Map<String, String> response = new HashMap<>();
            response.put("error", "Customer not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
