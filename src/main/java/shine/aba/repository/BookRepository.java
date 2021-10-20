package shine.aba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shine.aba.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
