package model;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface ItemRemote {
	void addItem(Item item);

	void removeItem(Item item);

	List<Item> getItems();
	
	Float getTotal();

	Integer getNumberOfItems();
	
	void empty();

	void checkout();
}