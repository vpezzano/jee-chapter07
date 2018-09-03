package model;

import java.util.List;

import javax.ejb.Local;

@Local
public interface BookLocal {
	Book findBookById(Long id);

	List<Book> findBookByTitle(String title);
	
	Book createBook(Book book);
}