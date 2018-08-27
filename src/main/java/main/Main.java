package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import model.Book;
import model.ItemLocal;
import model.ItemRemote;

public class Main {
	/*
	 * The main is a now a client running in the same JVM as the container. With
	 * EJB3.2, transactions can be used by Managed Beans, and not only by EJB.
	 * Moreover, passivation (status storing) for stateful session beans has been
	 * opted out. More info on Session Beans can be found here:
	 * "https://docs.oracle.com/cd/E11035_01/workshop102/ejb/session/conGettingStartedWithSessionBeans.html"
	 */
	public static void main(String[] args) throws NamingException {
		Map<String, Object> properties = new HashMap<>();
		properties.put(EJBContainer.MODULES, new File("target/classes"));
		EJBContainer ec = EJBContainer.createEJBContainer(properties);
		Context ctx = ec.getContext();
		// The interface ItemLocal is to be used for clients running inside the
		// container. In this case, arguments to methods can be passed by reference.
		ItemLocal itemLocal = (ItemLocal) ctx.lookup("java:global/classes/BookEJB!model.ItemLocal");
		Book book = new Book("Java 8", 50f, "Java 8 main features", "1234-ABCD", 300, true);
		itemLocal.createBook(book);
		System.out.println(itemLocal.findBookById(book.getId()));
		ItemRemote itemRemote = (ItemRemote) ctx.lookup("java:global/classes/BookEJB!model.ItemRemote");
		System.out.println(itemRemote.getRemoteMessage());
		System.exit(0);
	}
}
