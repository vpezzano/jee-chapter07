package ejb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
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
	private static final int TOO_MANY_BOOKS = 1000;

	@PersistenceContext(unitName = "chapter07PU")
	private EntityManager em;

	private SessionContext context;

	@Inject
	private NumberGenerator generator;

	/*
	 * The annotation can be also on the instance variable.
	 */
	@Resource
	private void setContext(SessionContext context) {
		this.context = context;
	}

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
		if (context.isCallerInRole("employee")) {
			throw new SecurityException("Only admins can create books");
		}

		book.setIsbn(generator.generateNumber());
		em.persist(book);

		if (inventoryLevel(book) == TOO_MANY_BOOKS) {
			context.setRollbackOnly();
		}

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

	@Override
	public String getContextInfo() {
		Map<String, Object> contextInfo = new HashMap<>();
		contextInfo.put("callerPrincipal", context.getCallerPrincipal());
		contextInfo.put("rollbackOnly", context.getRollbackOnly());
		contextInfo.put("timerService", context.getTimerService());
		return contextInfo.toString();
	}

	private int inventoryLevel(Book book) {
		return 0;
	}
}
