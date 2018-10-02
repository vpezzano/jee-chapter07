package ejb;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.BMCCacheLocal;
import model.BMCCacheRemote;
import model.Book;

/*
 * CMC: Container-managed concurrency; it's the default, and uses LockType to control concurrent access.
 * BMC: Bean-managed concurrency; the synchronization responsibility is delegated to the bean.
 * @Singleton instructs the container to produce a single instance of a stateless bean. If we change
 * the AccessTimeout value to 0, no parallel access will be allowed. If we change it to -1, a client
 * issuing a parallel request will be able to wait infinite time.
 * In this example, we are using BMC, so we need to explicitly synchronize the methods. The annotation
 * @AccessTimeout is usable also for BMC.
 */
@Singleton
@Startup
@DependsOn("CountryCodeEJB")
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@AccessTimeout(value = 5, unit = TimeUnit.SECONDS)
public class BMCCacheEJB implements BMCCacheRemote, BMCCacheLocal {
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
	public synchronized Object getFromCache(Long id) {
		if (cache.containsKey(id))
			return cache.get(id);
		else
			return null;
	}

	@Override
	public synchronized String getCountryCode(String country) {
		return countryCodeEJB.getByCountry(country);
	}

	@PostConstruct
	private void init() {
		TypedQuery<Book> query = em.createNamedQuery(Book.FIND_ALL, Book.class);
		for (Book book : query.getResultList()) {
			cache.put(book.getId(), book);
		}
	}

	@Override
	public synchronized Map<Long, Object> getCache() {
		Map<Long, Object> cacheCopy = new HashMap<>();
		cacheCopy.putAll(cache);
		return cacheCopy;
	}
}
