package model;

import java.util.List;

import javax.ejb.Local;

/*
 * For this interface, method parameters are passed by reference.
 */
@Local
public interface BookLocal {
	Book findBookById(Long id);

	List<Book> findBookByTitle(String title);
	
	Book createBook(Book book);
}