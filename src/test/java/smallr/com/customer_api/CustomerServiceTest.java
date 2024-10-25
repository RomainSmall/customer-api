package smallr.com.customer_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import smallr.com.customer_api.exception.CustomerNotFoundException;
import smallr.com.customer_api.exception.ResourceNotFoundException;
import smallr.com.customer_api.model.Address;
import smallr.com.customer_api.model.Contact;
import smallr.com.customer_api.model.Customer;
import smallr.com.customer_api.model.types.AddressType;
import smallr.com.customer_api.model.types.ContactType;
import smallr.com.customer_api.repository.CustomerRepository;
import smallr.com.customer_api.service.CustomerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a mock customer
        mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setFirstName("John");
        mockCustomer.setLastName("Doe");
        mockCustomer.setEmail("john.doe@example.com");

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Miami");
        address.setState("FL");
        address.setZip("33101");
        address.setAddressType(AddressType.valueOf("Home"));
        mockCustomer.setAddress(address);

        Contact contact = new Contact();
        contact.setPhoneNumber("(123) 456-7890");
        contact.setContactType(ContactType.valueOf("Mobile"));
        mockCustomer.setContact(contact);
    }

    @Test
    void getCustomerById_success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        Optional<Customer> foundCustomer = customerService.getCustomerById(1L);

        assertTrue(foundCustomer.isPresent());
        assertEquals("John", foundCustomer.get().getFirstName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getCustomerById_throwsCustomerNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getAllCustomers_success() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(mockCustomer));

        List<Customer> customers = customerService.getAllCustomers();  // Call the method only once

        assertFalse(customers.isEmpty());  // Check if the result is not empty
        assertEquals(1, customers.size());  // Check the size of the list
        verify(customerRepository, times(1)).findAll();  // Ensure the repository method was called exactly once
    }


    @Test
    void createCustomer_success() {
        when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);

        Customer savedCustomer = customerService.createCustomer(mockCustomer);

        assertNotNull(savedCustomer);
        assertEquals("John", savedCustomer.getFirstName());
        verify(customerRepository, times(1)).save(mockCustomer);
    }

    @Test
    void deleteCustomer_success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).delete(mockCustomer);
    }

    @Test
    void deleteCustomer_throwsCustomerNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1L));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).delete(any(Customer.class));
    }

    @Test
    void updateCustomer_success() {
        Customer updatedDetails = new Customer();
        updatedDetails.setFirstName("Jane");
        updatedDetails.setLastName("Smith");
        updatedDetails.setEmail("jane.smith@example.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);

        Customer updatedCustomer = customerService.updateCustomer(1L, updatedDetails);

        assertNotNull(updatedCustomer);
        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("Smith", updatedCustomer.getLastName());
        assertEquals("jane.smith@example.com", updatedCustomer.getEmail());

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(mockCustomer);
    }

    @Test
    void updateCustomer_throwsResourceNotFoundException() {
        Customer updatedDetails = new Customer();
        updatedDetails.setFirstName("Jane");
        updatedDetails.setLastName("Smith");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(1L, updatedDetails));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }
}
