package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class SimpleClient extends AbstractClient {
	private static final Logger LOGGER = Logger.getLogger(SimpleClient.class.getName());
	static Scanner sc = new Scanner(System.in);
	private static SimpleClient client = null;

	public SimpleClient(String host, int port) {
		super(host, port);
	}

	public static SimpleClient getClient() {
		if (client == null) {
//			System.out.println("please enter the IP address and then the port number: ");
//			client = new SimpleClient(sc.next(), sc.nextInt());
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

	@Override
	protected void connectionEstablished() {
		// TODO Auto-generated method stub
		super.connectionEstablished();
		LOGGER.info("Connected to server.");
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		ArrayList<Flower> arr = new ArrayList<>();
		ArrayList<Object> msgArray = new ArrayList<>();
		msgArray = (ArrayList<Object>) msg;

		// get catalog for everyone
		if(msgArray.get(0).equals("#getcatalog")) {
			CatalogBoundaryController.setFlowers((ArrayList<Flower>)(msgArray.get(1)));
			CatalogEmployeeController.setFlowers((ArrayList<Flower>)(msgArray.get(1)));
		}
		// sign up a new user
		if(msgArray.get(0).equals("#connectUserAfterRegistration")) {
			EntityHolder.setTable(0);
			User user = new User((String)msgArray.get(1), (String)msgArray.get(2),
								 (String)msgArray.get(3), (String)msgArray.get(4),
								 (String)msgArray.get(5), (String)msgArray.get(6),
								 (String)msgArray.get(7), (String)msgArray.get(8),
								 (String)msgArray.get(9), (String)msgArray.get(10),
								 (double)msgArray.get(11), (int)msgArray.get(12));
			int id = (int)msgArray.get(13);
			EntityHolder.setUser(user);
			EntityHolder.setID(id);
			RegistrationBoundaryController r = new RegistrationBoundaryController();
			r.nextStep();
		}
		// login for users, employees, store Managers, Chain manager
		if(msgArray.get(0).equals("#connectEntity")) {
			if((boolean)msgArray.get(1) == true) {
				if(msgArray.get(2).equals("User")) {
					EntityHolder.setTable(0);
					User user = new User((String)msgArray.get(3), (String)msgArray.get(4),
										 (String)msgArray.get(5), (String)msgArray.get(6),
										 (String)msgArray.get(7), (String)msgArray.get(8),
										 (String)msgArray.get(9), (String)msgArray.get(10),
										 (String)msgArray.get(11), (String)msgArray.get(12),
										 (double)msgArray.get(13), (int)msgArray.get(14));
					int id = (int)msgArray.get(15);
					EntityHolder.setUser(user);
					EntityHolder.setID(id);
					LoginBoundaryController loginController = new LoginBoundaryController();
					loginController.nextStep(2);
				}
				else if(msgArray.get(2).equals("Employee")) {
					EntityHolder.setTable(1);
					Employee employee = new Employee((String)msgArray.get(3), (String)msgArray.get(4),
													 (String)msgArray.get(5), (int)msgArray.get(6));
					int id = (int)msgArray.get(7);
					EntityHolder.setEmployee(employee);
					EntityHolder.setID(id);
					LoginBoundaryController loginController = new LoginBoundaryController();
					loginController.nextStep(3);
				}
				else if(msgArray.get(2).equals("Store Manager")) {
					EntityHolder.setTable(2);
					StoreManager storeManager = new StoreManager((String)msgArray.get(3), (String)msgArray.get(4),
																 (String)msgArray.get(5), (String)msgArray.get(6),
																 (int)msgArray.get(7));
					int id = (int)msgArray.get(8);
					EntityHolder.setStoreM(storeManager);
					EntityHolder.setID(id);
					LoginBoundaryController loginController = new LoginBoundaryController();
					loginController.nextStep(4);
				}
				else if(msgArray.get(2).equals("Chain Manager")) {
					EntityHolder.setTable(3);
					ChainManager chainManager = new ChainManager((String)msgArray.get(3),
																 (String)msgArray.get(4),
																 (String)msgArray.get(5));
					int id = (int)msgArray.get(6);
					EntityHolder.setChainM(chainManager);
					EntityHolder.setID(id);
					LoginBoundaryController loginController = new LoginBoundaryController();
					loginController.nextStep(5);
				}
			}
		}
		// get my orders
		if(msgArray.get(0).equals("#getmyorders")) {
			int myUserId = (int)msgArray.get(1);
			ArrayList<Order> list = (ArrayList<Order>)msgArray.get(2);
			int generalUserId = 0;
			ObservableList<OrderHolder> newList = FXCollections.observableArrayList();

			for(int i = 0; i < list.size(); i++) {
				generalUserId = list.get(i).getUserID();
				if(generalUserId == myUserId) {
					OrderHolder o = new OrderHolder(list.get(i).getOrderID(), list.get(i).getDateTime(),
													list.get(i).getFinalPrice(), list.get(i).getFlowers(),
													list.get(i).getStatus());
					newList.add(o);
				}
			}

			MyOrdersBoundaryController.setMyOrders(newList);
			MyProfileBoundaryController m = new MyProfileBoundaryController();
			m.nextStep();
		}
		// cancel an order
		if(msgArray.get(0).equals("#cancelOrder")) {
			double r = (double)msgArray.get(1);
			EntityHolder.getUser().setRefund(r);
			CancelOrdersBoundaryController c = new CancelOrdersBoundaryController();
			c.nextStep();
		}
		// file complaints
		if(msgArray.get(0).equals("#complaintAdded")) {
			FileComplaintBoundaryController f = new FileComplaintBoundaryController();
			f.nextStep();
		}
		// get refund history
		if(msgArray.get(0).equals("#getmyrefunds")) {
			int myUserId = (int)msgArray.get(1);
			ArrayList<Refund> list = (ArrayList<Refund>)msgArray.get(2);
			int generalUserId = 0;
			ObservableList<RefundHolder> newList = FXCollections.observableArrayList();

			for(int i = 0; i < list.size(); i++) {
				generalUserId = list.get(i).getUserId();
				if(generalUserId == myUserId) {
					RefundHolder r = new RefundHolder(list.get(i).getOrderId(), list.get(i).getRefund());
					newList.add(r);
				}
			}

			RefundHistoryBoundaryController.setMyRefunds(newList);
			MyProfileBoundaryController m = new MyProfileBoundaryController();
			m.nextStep1();
		}
		// before update flower - employee
		if(msgArray.get(0).equals("#beforeUpdate")) {
			Flower f = new Flower((String)msgArray.get(1), (String)msgArray.get(2), (String)msgArray.get(3),
								  (String)msgArray.get(4), (String)msgArray.get(5), (double)msgArray.get(6),
								  (int)msgArray.get(7), (boolean)msgArray.get(8), (double)msgArray.get(9),
								  (int)msgArray.get(10));
			int id = (int)msgArray.get(11);
			FlowerHolder.setFlower(f);
			FlowerHolder.setId(id);
			CatalogEmployeeController c = new CatalogEmployeeController();
			c.nextStep(1);
		}
		// order list - employee
		if(msgArray.get(0).equals("#orderListE")) {
			String location = (String)msgArray.get(1);
			int index = (int)msgArray.get(2);
			ArrayList<Order> list = (ArrayList<Order>)msgArray.get(3);
			ObservableList<OrderHolder> newList = FXCollections.observableArrayList();
			String generalStoreId = "";

			for(int i = 0; i < list.size(); i++) {
				generalStoreId = list.get(i).getStoreName();
				if(generalStoreId.equals(location) && list.get(i).getStatus() == 1) {
					// int orderID, String receiverName, String flowers, String dateTime, String address, int where
					OrderHolder o = new OrderHolder(list.get(i).getOrderID(), list.get(i).getReceiverName(),
													list.get(i).getFlowers(), list.get(i).getDateTime(),
													list.get(i).getAddress(), index,
													list.get(i).getName(), list.get(i).getEmail());
					newList.add(o);
				}
			}

			OrderListEmployeeController.setMyOrders(newList);
			CatalogEmployeeController c = new CatalogEmployeeController();
			c.nextStep(2);
		}
		// complaint list - employee
		if(msgArray.get(0).equals("#ComplaintListE")) {
			ArrayList<Complaint> list = (ArrayList<Complaint>)msgArray.get(1);
			ObservableList<ComplaintHolder> newList = FXCollections.observableArrayList();

			for(int i = 0; i < list.size(); i++) {
				if(list.get(i).getStatus() == 1) {
					ComplaintHolder c = new ComplaintHolder(list.get(i).getOrderId(),
															list.get(i).getUserId(),
															list.get(i).getContent(),
															list.get(i).getDateTime(),
															list.get(i).getId(), list.get(i).getPrice());
					newList.add(c);
				}
			}

			ComplaintListEmployeeController.setComplaints(newList);
			CatalogEmployeeController c = new CatalogEmployeeController();
			c.nextStep(3);
		}
		// income report store manager
		if(msgArray.get(0).equals("#incomeStore")) {
			IncomeReportsBoundaryController.setOrders((ArrayList<Order>)(msgArray.get(1)), (String)msgArray.get(2));
		}
		// orders report store manager
		if(msgArray.get(0).equals("#orderStore")) {
			OrdersReportsBoundaryController.setData((ArrayList<Order>)(msgArray.get(1)),
													(ArrayList<Flower>)(msgArray.get(2)),
													(String)msgArray.get(3));
		}
		// complaints report store manager
		if(msgArray.get(0).equals("#complaintsStore")) {
			ComplaintsReportsBoundaryController.setComplaints((ArrayList<Complaint>)(msgArray.get(1)), (String)msgArray.get(2));
		}
		// income report chain manager
		if(msgArray.get(0).equals("#incomeChain")) {
			IncomeReportsChainBoundaryController.setOrders((ArrayList<Order>)(msgArray.get(1)));
		}
		// orders report chain manager
		if(msgArray.get(0).equals("#orderChain")) {
			OrdersReportsChainBoundaryController.setData((ArrayList<Order>)(msgArray.get(1)), (ArrayList<Flower>)(msgArray.get(2)));
		}
		// complaints report chain manager
		if(msgArray.get(0).equals("#complaintsChain")) {
			ComplaintsReportsChainBoundaryController.setComplaints((ArrayList<Complaint>)(msgArray.get(1)));
		}
		// change authorization chain manager
		if(msgArray.get(0).equals("#getAllLists")) {
			ChangeAuthorizationChainBoundaryController.testAll((ArrayList<User>)(msgArray.get(1)),
															   (ArrayList<Employee>)(msgArray.get(2)),
															   (ArrayList<StoreManager>)(msgArray.get(3)));
		}
	}

	@Override
	protected void connectionClosed() {
		// TODO Auto-generated method stub
		super.connectionClosed();
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("Required arguments: <host> <port>");
		}
		else {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			SimpleClient Client = new SimpleClient(host, port);
			Client.openConnection();
		}
	}
}