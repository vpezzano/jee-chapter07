package ejb;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Book;
import model.BookLocalLegacy;
import model.BookRemoteLegacy;

/*
 * This is the way to go when we cannot change the interfaces,
 * adding annotations to them.
 */
@Stateless
@Remote(BookRemoteLegacy.class)
@Local(BookLocalLegacy.class)
@LocalBean
public class BookEJBLegacy implements BookLocalLegacy, BookRemoteLegacy {
	@PersistenceContext(unitName = "chapter07PU")
	private EntityManager em;

	@Override
	public Book findBookById(Long id) {
		return em.find(Book.class, id);
	}

	@Override
	public Book createBook(Book book) {
		em.persist(book);
		return book;
	}

	@Override
	public List<Book> findAllBooks() {
		TypedQuery<Book> query = em.createNamedQuery(Book.FIND_ALL, Book.class);
		return query.getResultList();
	}

	@Override
	public String getRemoteMessage() {
		System.out.println("Invoking the method getRemoteMessage().");
		return "Hello World!";
	}
}
