package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import model.Book;
import model.BookLocal;
import model.BookLocalLegacy;
import model.BookRemote;

public class Main {
	/*
	 * The main is a now a client running in the same JVM as the container. With
	 * EJB3.2, transactions can be used by Managed Beans, and not only by EJB.
	 * Moreover, passivation (status storing) for stateful session beans has been
	 * opted out. More info on Session Beans can be found here:
	 * "https://docs.oracle.com/cd/E11035_01/workshop102/ejb/session/conGettingStartedWithSessionBeans.html"
	 */
	public static void main(String[] args) throws NamingException {
		// Initialize embedded container
		Map<String, Object> properties = new HashMap<>();
		properties.put(EJBContainer.MODULES, new File("target/classes"));
		EJBContainer ec = EJBContainer.createEJBContainer(properties);
		Context ctx = ec.getContext();

		// The interface BookLocal is to be used for clients running in the same VM as
		// the container. In this case, arguments to methods can be passed by reference
		BookLocal bookLocal = (BookLocal) ctx.lookup("java:global/classes/BookEJB!model.BookLocal");
		Book book = new Book("Java 8", 50f, "Java 8 main features", "1234-ABCD", 300, true);
		bookLocal.createBook(book);
		System.out.println("Found: " + bookLocal.findBookById(book.getId()));

		// The interface BookRemote should be used for clients running in a different VM
		BookRemote bookRemote = (BookRemote) ctx.lookup("java:global/classes/BookEJB!model.BookRemote");
		System.out.println("Remote message: " + bookRemote.getRemoteMessage());

		// In case we are dealing with legacy interfaces which we can't change,
		// no annotations will be present in the interfaces
		BookLocalLegacy bookLocalLegacy = (BookLocalLegacy) ctx
				.lookup("java:global/classes/BookEJBLegacy!model.BookLocalLegacy");
		System.out.println("Using legacy interfaces, found: " + bookLocalLegacy.findBookById(book.getId()));
		
		System.exit(0);
	}
}
