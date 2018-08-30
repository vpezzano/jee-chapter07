package model;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface BookRemote {
	List<Book> findAllBooks();

	String getRemoteMessage();
}