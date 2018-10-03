package ejb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
import model.CountryCodeLocal;

/*
 * @Singleton instructs the container to produce a single instance of a stateless bean. If we change
 * the AccessTimeout value to 0, no parallel access will be allowed. If we change it to -1, a client
 * issuing a parallel request will be able to wait infinite time.
 * Singleton EJBs can be initialized at startup, be chained together, and have their concurrency
 * access customized.
 */
@Singleton
@Startup
@DependsOn("CountryCodeEJB")
// The specification of the LockType is redundant here, because LockType.WRITE
// is the default; we also don't need to specify the keyword synchronized on
// each method, because of the LockType.
@Lock(LockType.WRITE)
@AccessTimeout(value = 5, unit = TimeUnit.SECONDS)
public class CacheEJB implements CacheRemote, CacheLocal {
	@PersistenceContext(unitName = "chapter07PU")
	private EntityManager em;
	// Use a ConcurrentHashMap, to prevent problems with the methods
	// marked with @Lock(LockType.READ)
	private Map<Long, Object> cache = new ConcurrentHashMap<>();

	/*
	 * It's not required to have the bean here, we can use a local reference.
	 */
	@EJB
	private CountryCodeLocal countryCodeLocal;

	@Override
	public void addToCache(Long id, Object object) {
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

	/*
	 * No concurrent access is allowed here
	 */
	@AccessTimeout(0)
	@Override
	public void removeFromCache(Long id) {
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
		return countryCodeLocal.getByCountry(country);
	}

	@PostConstruct
	private void init() {
		TypedQuery<Book> query = em.createNamedQuery(Book.FIND_ALL, Book.class);
		for (Book book : query.getResultList()) {
			cache.put(book.getId(), book);
		}
		System.out.println("After init cache size: " + cache.size());
	}

	@Override
	public String getCacheImage() {
		return cache.toString();
	}
}
