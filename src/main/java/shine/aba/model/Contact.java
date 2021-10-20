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
//    @Builder.Default
    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String phone;

    @Builder
    @SuppressWarnings("unused")
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
