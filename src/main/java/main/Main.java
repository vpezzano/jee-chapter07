package main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import ejb.BookEJBNoView;
import model.Book;
import model.BookLocal;
import model.BookLocalLegacy;
import model.BookRemote;
import model.CacheLocal;
import model.OrderLocal;

public class Main {
	/*
	 * The main is a now a client running in the same JVM as the container. With
	 * EJB3.2, transactions can be used by Managed Beans, and not only by EJB.
	 * Moreover, passivation (status storing) for stateful session beans has been
	 * opted out. More info on Session Beans can be found here:
	 * "https://docs.oracle.com/cd/E11035_01/workshop102/ejb/session/conGettingStartedWithSessionBeans.html"
	 */
	public static void main(String[] args) throws NamingException, InterruptedException, ExecutionException {
		// Initialize embedded container
		Map<String, Object> properties = new HashMap<>();
		properties.put(EJBContainer.MODULES, new File("target/classes"));
		EJBContainer ec = EJBContainer.createEJBContainer(properties);
		Context ctx = ec.getContext();

		// The interface BookLocal is to be used for clients running in the same VM as
		// the container. In this case, arguments to methods can be passed by reference
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		BookLocal bookLocal = (BookLocal) ctx.lookup("java:global/classes/BookEJB!model.BookLocal");
		Book book = new Book("Java 8", 50f, "Java 8 main features, edition " + formatter.format(new Date()), 300, true);
		bookLocal.createBook(book);
		
		System.out.println("Found: " + bookLocal.findBookById(book.getId()));
		System.out.println("Context info: " + bookLocal.getContextInfo());
		
		// The interface BookRemote should be used for clients running in a different VM
		BookRemote bookRemote = (BookRemote) ctx.lookup("java:global/classes/BookEJB!model.BookRemote");
		System.out.println("Remote message: " + bookRemote.getRemoteMessage());

		// In case we are dealing with legacy interfaces which we can't change,
		// no annotations will be present in the interfaces
		BookLocalLegacy bookLocalLegacy = (BookLocalLegacy) ctx
				.lookup("java:global/classes/BookEJBLegacy!model.BookLocalLegacy");
		System.out.println("Using legacy interfaces, found: " + bookLocalLegacy.findBookById(book.getId()));

		// Here we are using a Session Bean with no-interface view
		BookEJBNoView bookEJBNoView = (BookEJBNoView) ctx
				.lookup("java:global/classes/BookEJBNoView!ejb.BookEJBNoView");
		System.out.println("Using no-interface session bean, found: " + bookEJBNoView.findBookById(book.getId()));
		
		List<Book> booksByTitle = bookLocal.findBookByTitle("H2G2");
		CacheLocal cacheLocal = (CacheLocal) ctx
				.lookup("java:global/classes/CacheEJB!model.CacheLocal");
		if (!booksByTitle.isEmpty()) {
			System.out.println("Got from cache: " + cacheLocal.getFromCache(booksByTitle.get(0).getId()));
		}
		
		System.out.println("Country code for BE: " + cacheLocal.getCountryCode("BE"));
		
		OrderLocal orderLocal = (OrderLocal) ctx.lookup("java:global/classes/OrderEJB!model.OrderLocal");
		orderLocal.sendEmailOrderComplete(null, null);
		System.out.println("Invocation of sendEmailOrderComplete terminated.");
		orderLocal.printOrder(null);
		System.out.println("Invocation of printOrder terminated.");
		
		Future<Integer> status = orderLocal.sendOrderToWorkflow(null);
		System.out.println("Invocation of sendOrderToWorkflow terminated.");
		while (!status.isDone()) {
			System.out.println("Waiting for result of sendOrderToWorkflow...");
			Thread.sleep(1000);
		}
		
		System.out.println("Status of sendOrderToWorkflow: " + status.get());
		
		System.exit(0);
	}
}
