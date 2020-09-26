package project.bookcrossing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.bookcrossing.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	Book findByTitle(String beginning);
}
