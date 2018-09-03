package ejb;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Book;

import java.util.HashMap;

/*
 * @Singleton instructs the container to produce a single instance of a stateless bean.
 */
@Singleton
@Startup
public class CacheEJB {
	@PersistenceContext(unitName = "chapter07PU")
	private EntityManager em;
	private Map<Long, Object> cache = new HashMap<>();

	public void addToCache(Long id, Object object) {
		if (!cache.containsKey(id))
			cache.put(id, object);
	}

	public void removeFromCache(Long id) {
		if (cache.containsKey(id))
			cache.remove(id);
	}

	public Object getFromCache(Long id) {
		if (cache.containsKey(id))
			return cache.get(id);
		else
			return null;
	}
	
	@PostConstruct
    private void init() {
		TypedQuery<Book> query = em.createNamedQuery(Book.FIND_ALL, Book.class);
		for (Book book : query.getResultList()) {
			cache.put(book.getId(), book);
		}
    }
}
