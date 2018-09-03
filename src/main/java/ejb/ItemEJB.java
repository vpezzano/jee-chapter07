package ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;

import model.Item;
import model.ItemRemote;

@Stateful
@StatefulTimeout(value = 20, unit = TimeUnit.SECONDS)
public class ItemEJB implements ItemRemote {
	private List<Item> cartItems = new ArrayList<>();

	@Override
	public void addItem(Item item) {
		if (!cartItems.contains(item))
			cartItems.add(item);
	}

	@Override
	public void removeItem(Item item) {
		if (cartItems.contains(item))
			cartItems.remove(item);
	}

	@Override
	public List<Item> getItems() {
		return cartItems;
	}

	@Override
	public Float getTotal() {
		if (cartItems == null || cartItems.isEmpty())
			return 0f;
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

	@Remove
	public void checkout() {
		// Do some business logic
		empty();
	}
}
