package model;

import java.util.concurrent.Future;

import javax.ejb.Local;

@Local
public interface OrderLocal {

	void sendEmailOrderComplete(Order order, Customer customer);

	void printOrder(Order order);

	Future<Integer> sendOrderToWorkflow(Order order);
}
