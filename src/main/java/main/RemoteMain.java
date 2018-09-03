package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import model.BookRemote;
import model.BookRemoteLegacy;
import model.Item;
import model.ItemRemote;

public class RemoteMain {
	public static void main(String[] args) throws FileNotFoundException, IOException, NamingException {
		try (FileInputStream jndi = new FileInputStream("jndi.properties")) {
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
			itemRemote.addItem(item1);
			
			Item item2 = new Item("Item2", "This is item2", 20f);
			itemRemote.addItem(item2);
			
			System.out.println(itemRemote.getNumberOfItems() + " items currently in the basket: " + itemRemote.getItems());
			System.out.println("Price: " + itemRemote.getTotal());
			
			itemRemote.checkout();
			
		}
	}
}
