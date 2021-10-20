package shine.aba.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @ManyToMany(mappedBy = "contacts")
    @JsonIgnoreProperties("contacts")
    private Set<Book> books;

    @Builder
    @SuppressWarnings("unused")
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String toString() {
        return String.format("Contact id=%d, name=%s, phone=%s, number of books=%d", id, name, phone, null == books ? 0 : books.size());
    }
}
