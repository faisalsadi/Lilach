package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserController {
    private Session session;

    public UserController(Session session) {
        this.session = session;
    }

    public void addUser(User user) throws Exception {
        session.save(user);
        session.flush();
    }

    public <T> List<T> getAllData(Class<T> c) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(c);
        Root<T> rootEntry = criteriaQuery.from(c);
        CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);
        TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
        return allQuery.getResultList();
    }

    public void updateData(int id, User user) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> rootEntry = criteriaQuery.from(User.class);
        CriteriaQuery<User> allCriteriaQuery = criteriaQuery.select(rootEntry);
        TypedQuery<User> allQuery = session.createQuery(allCriteriaQuery);

        allQuery.getResultList().get(id - 1).setAccount(user.getAccount());
        allQuery.getResultList().get(id - 1).setCredit(user.getCredit());
        allQuery.getResultList().get(id - 1).setCvv(user.getCvv());
        allQuery.getResultList().get(id - 1).setEmail(user.getEmail());
        allQuery.getResultList().get(id - 1).setId(user.getId());
        allQuery.getResultList().get(id - 1).setMonthAndYear(user.getMonthAndYear());
        allQuery.getResultList().get(id - 1).setPassword(user.getPassword());
        allQuery.getResultList().get(id - 1).setPhone(user.getPhone());
        allQuery.getResultList().get(id - 1).setRefund(user.getRefund());
        allQuery.getResultList().get(id - 1).setStoreOrNull(user.getStoreOrNull());
        allQuery.getResultList().get(id - 1).setUserName(user.getUserName());

        session.update(allQuery.getResultList().get(id - 1));
        session.flush();
    }
}
