package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class OrderController {
    private Session session;

    public OrderController(Session session) {
        this.session = session;
    }

    public <T> List<T> getAllData(Class<T> c) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(c);
        Root<T> rootEntry = criteriaQuery.from(c);
        CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);
        TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
        return allQuery.getResultList();
    }

    public void addOrder(Order order) throws Exception {
        session.save(order);
        session.flush();
    }

    public void updateData(int id, Order order) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);
        Root<Order> rootEntry = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> allCriteriaQuery = criteriaQuery.select(rootEntry);
        TypedQuery<Order> allQuery = session.createQuery(allCriteriaQuery);
        // id - 1 ==> id
        allQuery.getResultList().get(id - 1).setAddress(order.getAddress());
        allQuery.getResultList().get(id - 1).setCard(order.getCard());
        allQuery.getResultList().get(id - 1).setCredit(order.getCredit());
        allQuery.getResultList().get(id - 1).setCvv(order.getCvv());
        allQuery.getResultList().get(id - 1).setDateTime(order.getDateTime());
        allQuery.getResultList().get(id - 1).setFinalPrice(order.getFinalPrice());
        allQuery.getResultList().get(id - 1).setFormOfSupplying(order.getFormOfSupplying());
        allQuery.getResultList().get(id - 1).setMonthAndYear(order.getMonthAndYear());
        allQuery.getResultList().get(id - 1).setOrderID(order.getOrderID());
        allQuery.getResultList().get(id - 1).setReceiverName(order.getReceiverName());
        allQuery.getResultList().get(id - 1).setReceiverEmail(order.getReceiverEmail());
        allQuery.getResultList().get(id - 1).setRefund(order.getRefund());
        allQuery.getResultList().get(id - 1).setStatus(order.getStatus());
        allQuery.getResultList().get(id - 1).setStoreName(order.getStoreName());
        allQuery.getResultList().get(id - 1).setUserID(order.getUserID());
        allQuery.getResultList().get(id - 1).setName(order.getName());
        allQuery.getResultList().get(id - 1).setEmail(order.getEmail());
        allQuery.getResultList().get(id - 1).setFlowers(order.getFlowers());

        session.update(allQuery.getResultList().get(id - 1));
        session.flush();
    }
}
