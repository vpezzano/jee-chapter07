package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import model.BookRemote;
import model.BookRemoteLegacy;

public class RemoteMain {
	public static void main(String[] args) throws FileNotFoundException, IOException, NamingException {
		try (FileInputStream jndi = new FileInputStream("jndi.properties")) {
			Properties props = new Properties();
			props.load(jndi);
			InitialContext ctx = new InitialContext(props);
			BookRemote bookRemote = (BookRemote) ctx.lookup("java:global/chapter07/BookEJB!model.BookRemote");
			System.out.println("Using portable JNDI name: " + bookRemote.getRemoteMessage());

			// The following uses a non portable JNDI name,
			// Glassfish-specific. Favor the previous one
			BookRemote bookRemoteNonPortable = (BookRemote) ctx.lookup("model.BookRemote");
			System.out.println("Using non portable JNDI name: " + bookRemoteNonPortable.getRemoteMessage());

			// In case we are dealing with legacy interfaces which we can't change,
			// no annotations will be present in the interfaces
			BookRemoteLegacy bookRemoteLegacy = (BookRemoteLegacy) ctx
					.lookup("java:global/chapter07/BookEJBLegacy!model.BookRemoteLegacy");
			System.out.println("Using legacy interfaces: " + bookRemoteLegacy.getRemoteMessage());
		}
	}
}
