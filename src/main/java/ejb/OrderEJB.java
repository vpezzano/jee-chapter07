package ejb;

import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import model.Customer;
import model.Order;
import model.OrderLocal;

/**
 * 
 * @Asynchronous can also be applied at class level; in this case, it applies to all methods.
 */
@Stateless
public class OrderEJB implements OrderLocal {
	@Resource
	private SessionContext ctx;

	@Asynchronous
	@Override
	public void sendEmailOrderComplete(Order order, Customer customer) {
		// Very Long task
		for (int i = 0; i < 5; i++) {
			System.out.println("Sending email order complete...");
			
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Asynchronous
	@Override
	public void printOrder(Order order) {
		// Very Long task
		for (int i = 0; i < 5; i++) {
			System.out.println("Printing order...");
			
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Asynchronous
	@Override
	public Future<Integer> sendOrderToWorkflow(Order order) {
		Integer status = 0;
		// processing
		for (int i = 0; i < 5; i++) {
			System.out.println("Processing...");
			
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		status = 1;
		if (ctx.wasCancelCalled()) {
			return new AsyncResult<>(2);
		}
		// processing
		return new AsyncResult<>(status);
	}
}
