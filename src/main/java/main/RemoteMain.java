package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import model.BookRemote;

public class RemoteMain {
	public static void main(String[] args) throws FileNotFoundException, IOException, NamingException {
		try(FileInputStream jndi = new FileInputStream("jndi.properties")) {
			Properties props = new Properties();
			props.load(jndi);
			InitialContext ctx = new InitialContext(props);
			BookRemote bookRemote = (BookRemote) ctx.lookup("model.BookRemote");
			System.out.println(bookRemote.getRemoteMessage());
		}
	}
}
