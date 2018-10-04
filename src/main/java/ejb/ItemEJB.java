package ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;

import model.Item;
import model.ItemLocal;
import model.ItemRemote;

/*
 * A JTA transaction is controlled by the Java EE transaction manager. In a stateful session bean with 
 * a JTA transaction, the association between the bean instance and the transaction is retained across
 * multiple client calls. Even if each business method called by the client opens and closes the database
 * connection, the association is retained until the instance completes the transaction. In a stateless
 * session bean with bean-managed transactions, a business method must commit or roll back a transaction
 * before returning. However, a stateful session bean does not have this restriction.
 * If we use @StatefulTimeout, an idle EJB is retained for the specified period; this period represents
 * the duration the bean is permitted to remain idle (not receiving any client invocations) before being
 * removed by the container.
 */
@Stateful
@StatefulTimeout(value = 60, unit = TimeUnit.SECONDS)
@LocalBean
public class ItemEJB implements ItemRemote, ItemLocal {
	private List<Item> cartItems = new ArrayList<>();

	@Resource(name = "currencyEntry")
	private String currency;
	@Resource(name = "changeRateEntry")
	private Float changeRate;

	@Override
	public void addItem(Item item) {
		if (!cartItems.contains(item)) {
			cartItems.add(convertPrice(item));
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

	private Item convertPrice(Item item) {
		item.setPrice(item.getPrice() * changeRate);
		item.setCurrency(currency);
		return item;
	}

	/*
	 * After the checkout is invoked, the bean instance is permanently removed from
	 * memory.
	 */
	@Remove
	public void checkout() {
		// Do some business logic
		empty();
	}
}
