package shine.aba.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Contact {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 255, message = "name must be between 1 and 255 characters long")
    private String name;

    @NotNull
    @Size(min = 1, max = 25, message = "phone must be between 1 and 255 characters long")
    private String phone;

    @Builder
    @SuppressWarnings("unused")
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
