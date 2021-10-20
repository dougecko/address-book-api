package shine.aba.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 255, message = "name must be between 1 and 255 characters long")
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "book_contact",
            joinColumns = @JoinColumn(name = "contactId"),
            inverseJoinColumns = @JoinColumn(name = "bookId"),
            foreignKey = @ForeignKey(name = "book_contact_foreign_key"),
            inverseForeignKey = @ForeignKey(name = "contact_book_foreign_key"))
    @JsonIgnoreProperties("books")
    private Set<Contact> contacts;

    @Builder
    @SuppressWarnings("unused")
    public Book(String name) {
        this.name = name;
    }

    public String toString() {
        return String.format("Book id=%d, name=%s, number of contacts=%d", id, name, null == contacts ? 0 : contacts.size());
    }
}
