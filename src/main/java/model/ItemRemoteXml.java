package model;

import java.util.List;

public interface ItemRemoteXml {
	void addItem(Item item);

	List<Item> getItems();
	
	Float getTotal();

	Integer getNumberOfItems();
	
	void empty();

	void checkout();
}