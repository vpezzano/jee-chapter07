package ejb;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Book;
import model.CacheLocal;
import model.CacheRemote;

/*
 * @Singleton instructs the container to produce a single instance of a stateless bean. If we change
 * the AccessTimeout value to 0, no parallel access will be allowed. If we change it to -1, a client
 * issuing a parallel request will be able to wait infinite time.
 */
@Singleton
@Startup
@DependsOn("CountryCodeEJB")
@Lock(LockType.WRITE) // This is the default
@AccessTimeout(value = 5, unit = TimeUnit.SECONDS)
public class CacheEJB implements CacheRemote, CacheLocal {
	@PersistenceContext(unitName = "chapter07PU")
	private EntityManager em;
	private Map<Long, Object> cache = new HashMap<>();

	@EJB
	private CountryCodeEJB countryCodeEJB;

	@Override
	public synchronized void addToCache(Long id, Object object) {
		if (!cache.containsKey(id))
			cache.put(id, object);

		// This is used to text ConcurrentAccessTimeoutException. If we run 2 clients
		// simultaneously, the second one will not be able to acquire the lock within
		// 5 seconds, because here we are introducing a delay of 20 seconds
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void removeFromCache(Long id) {
		if (cache.containsKey(id))
			cache.remove(id);
	}

	@Override
	@Lock(LockType.READ)
	public Object getFromCache(Long id) {
		if (cache.containsKey(id))
			return cache.get(id);
		else
			return null;
	}

	@Override
	@Lock(LockType.READ)
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
