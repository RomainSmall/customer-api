package smallr.com.customer_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import smallr.com.customer_api.model.types.ContactType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(unique = true)
    @Pattern(regexp = "^\\(\\d{3}\\) \\d{3}-\\d{4}$", message = "Invalid phone number format. Expected format: (###) ###-####")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @JsonIgnore
    @OneToOne()
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}




