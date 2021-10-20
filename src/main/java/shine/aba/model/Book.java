package shine.aba.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 255, message = "name must be between 1 and 255 characters long")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "book_contact",
            joinColumns = @JoinColumn(name = "contactId"),
            inverseJoinColumns = @JoinColumn(name = "bookId"),
            foreignKey = @ForeignKey(name = "book_contact_foreign_key"),
            inverseForeignKey = @ForeignKey(name = "contact_book_foreign_key"))
    private Set<Contact> contacts;

    @Builder
    @SuppressWarnings("unused")
    public Book(String name) {
        this.name = name;
    }
}
