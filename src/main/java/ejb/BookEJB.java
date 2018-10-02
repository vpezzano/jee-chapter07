package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Book;
import model.BookLocal;
import model.BookRemote;
import numbergeneration.NumberGenerator;

/*
 * Stateless session beans are transactional. Such a bean doesn't have associated client state,
 * but it may preserve its instance state.
*/
@Stateless
public class BookEJB implements BookLocal, BookRemote {
	@PersistenceContext(unitName = "chapter07PU")
	private EntityManager em;

	@Inject
	private NumberGenerator generator;
	
	@Override
	public Book findBookById(Long id) {
		return em.find(Book.class, id);
	}

	@Override
	public List<Book> findBookByTitle(String title) {
		TypedQuery<Book> query = em.createNamedQuery(Book.FIND_BY_TITLE, Book.class);
		query.setParameter("title", title);
		return query.getResultList();
	}
	
	@Override
	public Book createBook(Book book) {
		book.setIsbn(generator.generateNumber());
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
