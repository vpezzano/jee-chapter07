package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Book;

/*
 * With this kind of session bean (no-interface view), all methods are locally invokable.
 * So, it is equivalent to using @Local, but while @Local exposes only the methods
 * specified in the interface, in this case all the methods can be called.
*/
@Stateless
public class BookEJBNoView {
	@PersistenceContext(unitName = "chapter07PU")
	private EntityManager em;

	public Book findBookById(Long id) {
		return em.find(Book.class, id);
	}

	public Book createBook(Book book) {
		em.persist(book);
		return book;
	}

	public List<Book> findAllBooks() {
		TypedQuery<Book> query = em.createNamedQuery(Book.FIND_ALL, Book.class);
		return query.getResultList();
	}

	public String getRemoteMessage() {
		System.out.println("Invoking the method getRemoteMessage().");
		return "Hello World!";
	}
}
