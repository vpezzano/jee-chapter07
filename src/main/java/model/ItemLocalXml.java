package model;

import java.util.List;

public interface ItemLocalXml {
	void addItem(Item item);

	void removeItem(Item item);

	List<Item> getItems();
	
	Float getTotal();

	Integer getNumberOfItems();
	
	void empty();

	void checkout();
}