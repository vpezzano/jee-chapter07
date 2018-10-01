package model;

import java.util.List;

import javax.ejb.Remote;

/*
 * For this interface, method parameters are passed by value and
 * need to be serializable.
 */
@Remote
public interface BookRemote {
	List<Book> findAllBooks();

	String getRemoteMessage();
}