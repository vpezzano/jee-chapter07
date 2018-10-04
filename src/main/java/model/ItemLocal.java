package model;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ItemLocal {
	void addItem(Item item);

	void removeItem(Item item);

	List<Item> getItems();
	
	Float getTotal();

	Integer getNumberOfItems();
	
	void empty();

	void checkout();
}