package model;

import java.util.List;

import javax.ejb.Local;

@Local
public interface BookLocal {
	List<Book> findAllBooks();

	Book findBookById(Long id);

	Book createBook(Book book);
}