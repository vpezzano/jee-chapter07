package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import model.BMCCacheRemote;
import model.BookRemote;
import model.BookRemoteLegacy;
import model.CacheRemote;
import model.Item;
import model.ItemRemote;

public class RemoteMain {
	public static void main(String[] args) throws FileNotFoundException, IOException, NamingException {
		try (FileInputStream jndi = new FileInputStream("jndi.properties")) {
			long delay;
			try {
				delay = Long.parseLong(args[0]) * 1000;
			} catch (RuntimeException e) {
				delay = 0;
			}

			Properties props = new Properties();
			props.load(jndi);
			InitialContext ctx = new InitialContext(props);
			BookRemote bookRemote = (BookRemote) ctx.lookup("java:global/chapter07/BookEJB!model.BookRemote");
			System.out.println("Using portable JNDI name: " + bookRemote.getRemoteMessage());

			System.out.println("Using portable JNDI name: " + bookRemote.findAllBooks());

			// The following uses a non portable JNDI name,
			// Glassfish-specific. Favor the previous one
			BookRemote bookRemoteNonPortable = (BookRemote) ctx.lookup("model.BookRemote");
			System.out.println("Using non portable JNDI name: " + bookRemoteNonPortable.getRemoteMessage());

			// In case we are dealing with legacy interfaces which we can't change,
			// no annotations will be present in the interfaces
			BookRemoteLegacy bookRemoteLegacy = (BookRemoteLegacy) ctx
					.lookup("java:global/chapter07/BookEJBLegacy!model.BookRemoteLegacy");
			System.out.println("Using legacy interfaces: " + bookRemoteLegacy.getRemoteMessage());

			ItemRemote itemRemote = (ItemRemote) ctx.lookup("java:global/chapter07/ItemEJB!model.ItemRemote");
			Item item1 = new Item("Item1", "This is item1", 10f);
			Item item2 = new Item("Item2", "This is item2", 20f);

			// The EJB container guarantees that the following two
			// calls will use the same instance of the ItemEJB class
			itemRemote.addItem(item1);

			// If you change ItemEJB to stateless, and run 2 main classes, the first one
			// with a delay and the second one without, you are likely to get a wrong
			// result from the one with the delay.
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			itemRemote.addItem(item2);

			System.out.println(
					itemRemote.getNumberOfItems() + " items currently in the basket: " + itemRemote.getItems());
			System.out.println("Price: " + itemRemote.getTotal());

			itemRemote.checkout();
			
			CacheRemote cacheRemote = (CacheRemote) ctx.lookup("java:global/chapter07/CacheEJB!model.CacheRemote");
			System.out.println("Country code for BE: " + cacheRemote.getCountryCode("BE"));
			
			long key = System.currentTimeMillis();
			cacheRemote.addToCache(key, "Cache");
			System.out.println("After addToCache, cache for " + key + ": " + cacheRemote.getFromCache(key));
			System.out.println("After addToCache, cache image: " + cacheRemote.getCacheImage());
			
			BMCCacheRemote bmcCacheRemote = (BMCCacheRemote) ctx.lookup("java:global/chapter07/BMCCacheEJB!model.BMCCacheRemote");
			bmcCacheRemote.addToCache(System.currentTimeMillis(), "Cache");
			System.out.println("After addToCache, cache: " + bmcCacheRemote.getCache());
		}
	}
}
