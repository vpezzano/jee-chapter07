package ejb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Book;

/*
 * @Singleton instructs the container to produce a single instance of a stateless bean.
 */
@Singleton
@Startup
@DependsOn("CountryCodeEJB")
public class CacheEJB {
	@PersistenceContext(unitName = "chapter07PU")
	private EntityManager em;
	private Map<Long, Object> cache = new ConcurrentHashMap<>();

	@EJB
	private CountryCodeEJB countryCodeEJB;
	
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
	
	public String getCountryCode(String country) {
		return countryCodeEJB.getByCountry(country);
	}
	
	@PostConstruct
    private void init() {
		TypedQuery<Book> query = em.createNamedQuery(Book.FIND_ALL, Book.class);
		for (Book book : query.getResultList()) {
			cache.put(book.getId(), book);
		}
    }
}
