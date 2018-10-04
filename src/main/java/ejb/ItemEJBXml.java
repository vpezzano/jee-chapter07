package ejb;

import java.util.ArrayList;
import java.util.List;

import model.Item;
import model.ItemLocalXml;
import model.ItemRemoteXml;

public class ItemEJBXml implements ItemRemoteXml,ItemLocalXml {
	private List<Item> cartItems = new ArrayList<>();

	@Override
	public void addItem(Item item) {
		if (!cartItems.contains(item)) {
			cartItems.add(item);
		}
	}

	@Override
	public void removeItem(Item item) {
		if (cartItems.contains(item)) {
			cartItems.remove(item);
		}
	}

	@Override
	public List<Item> getItems() {
		return cartItems;
	}

	@Override
	public Float getTotal() {
		if (cartItems == null || cartItems.isEmpty()) {
			return 0f;
		}
			
		Float total = 0f;
		for (Item cartItem : cartItems) {
			total += (cartItem.getPrice());
		}
		return total;
	}

	@Override
	public Integer getNumberOfItems() {
		if (cartItems == null || cartItems.isEmpty())
			return 0;
		return cartItems.size();
	}

	@Override
	public void empty() {
		cartItems.clear();
	}

	/*
	 * After the checkout is invoked, the bean instance is permanently removed from
	 * memory.
	 */
	public void checkout() {
		// Do some business logic
		empty();
	}
}
