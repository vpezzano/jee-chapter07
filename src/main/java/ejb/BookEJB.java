package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Book;
import model.BookLocal;
import model.BookRemote;

/*
 * Session beans can be transactional. 
*/
@Stateless
public class BookEJB implements BookLocal, BookRemote {
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
		TypedQuery<Book> query = em.createQuery("SELECT b FROM model.Book b", Book.class);
		return query.getResultList();
	}

	@Override
	public String getRemoteMessage() {
		System.out.println("Invoking the method getRemoteMessage().");
		return "Hello World!";
	}
}
