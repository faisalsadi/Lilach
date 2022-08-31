package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Hello world! */
public class App {
	private static SimpleServer server;
    private static Session session;
    private static SessionFactory sessionFactory;
    static Scanner sc = new Scanner(System.in);
    private static int flowerSerialNumber = 1002345;
    private static Catalog catalog = new Catalog();

    public static SessionFactory getSession() {
        return sessionFactory;
    }

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Catalog.class);
        configuration.addAnnotatedClass(ChainManager.class);
        configuration.addAnnotatedClass(Complaint.class);
        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Flower.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(Refund.class);
        configuration.addAnnotatedClass(StoreManager.class);
        configuration.addAnnotatedClass(User.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void main(String[] args) throws IOException {
        // System.out.println("please enter the port number: ");
        //server = new SimpleServer(sc.nextInt());
        server = new SimpleServer(3000);
        server.listen();

        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            List<Flower> flowersList = new ArrayList<Flower>();
            ConstantStrings1 c = new ConstantStrings1();

            // flowers
            for(int i = 0; i < c.flowersNames.length; i++) {
                Flower flowerItem = new Flower(c.flowersNames[i], c.flowersDescriptions[i],
                                               c.flowersTypes[i], c.flowersImages[i],
                                               c.flowersColors[i], c.flowersPrices[i],
                                               getFlowerSerialNumber());
                setFlowerSerialNumber(flowerSerialNumber + 1);
                flowerItem.setCatalog(catalog);
                flowersList.add(flowerItem);
                session.save(flowerItem);
            }
            // users
            for(int i = 0; i < c.userNames.length; i++) {
                User userItem = new User(c.userNames[i], c.userIdentificationNumbers[i],
                                         c.userEmails[i], c.userPhoneNumbers[i],
                                         c.userCreditNumbers[i], c.userMonthAndYears[i],
                                         c.userCVVNumbers[i], c.userPasswords[i],
                                         c.userAccounts[i], c.userStores[i],
                                         c.userRefund[i], c.userStatus[i]);
                session.save(userItem);
            }
            // employees
            for(int i = 0; i < c.employeesNames.length; i++) {
                Employee employeeItem = new Employee(c.employeesNames[i], c.employeesEmails[i],
                                                     c.employeesPasswords[i], c.employeeStatus[i]);
                session.save(employeeItem);
            }
            // store managers
            for(int i = 0; i < c.storeMNames.length; i++) {
                StoreManager storeMItem = new StoreManager(c.storeMNames[i], c.storeMEmails[i],
                                                           c.storeMPasswords[i], c.storeMUniqueStore[i],
                                                           c.storeMStatus[i]);
                session.save(storeMItem);
            }
            // a single chain manager
            ChainManager chainMItem = new ChainManager("Ramesses The Second", "riri@gmail.com", "ahsng55555");
            session.save(chainMItem);

            catalog.setFlowers(flowersList);
            session.save(catalog);

            session.flush();
            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static int getFlowerSerialNumber() {
        return flowerSerialNumber;
    }

    public static void setFlowerSerialNumber(int newFlowerSerialNumber) {
        flowerSerialNumber = newFlowerSerialNumber;
    }

    public static Catalog getCatalog() {
        return catalog;
    }
}

