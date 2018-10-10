package model;

import java.util.concurrent.Future;

import javax.ejb.Remote;

@Remote
public interface OrderRemote {

	void sendEmailOrderComplete(Order order, Customer customer);

	void printOrder(Order order);

	Future<Integer> sendOrderToWorkflow(Order order);
}
