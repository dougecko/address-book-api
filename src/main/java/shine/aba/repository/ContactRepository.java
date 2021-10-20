package shine.aba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shine.aba.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
