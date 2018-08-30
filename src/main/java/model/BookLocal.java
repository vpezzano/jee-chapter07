package model;

import javax.ejb.Local;

@Local
public interface BookLocal {
	Book findBookById(Long id);

	Book createBook(Book book);
}