package model;

import java.util.List;

public interface BookLocalLegacy {
	List<Book> findAllBooks();

	Book findBookById(Long id);

	Book createBook(Book book);
}