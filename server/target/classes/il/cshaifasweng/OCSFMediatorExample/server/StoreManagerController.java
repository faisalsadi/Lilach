package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.StoreManager;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class StoreManagerController {
    private Session session;

    public StoreManagerController(Session session) {
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

    public void updateData(int id, StoreManager storeManager) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<StoreManager> criteriaQuery = builder.createQuery(StoreManager.class);
        Root<StoreManager> rootEntry = criteriaQuery.from(StoreManager.class);
        CriteriaQuery<StoreManager> allCriteriaQuery = criteriaQuery.select(rootEntry);
        TypedQuery<StoreManager> allQuery = session.createQuery(allCriteriaQuery);

        allQuery.getResultList().get(id - 1).setEmail(storeManager.getEmail());
        allQuery.getResultList().get(id - 1).setPassword(storeManager.getPassword());
        allQuery.getResultList().get(id - 1).setStoreName(storeManager.getStoreName());
        allQuery.getResultList().get(id - 1).setUserName(storeManager.getUserName());
        allQuery.getResultList().get(id - 1).setStatus(storeManager.getStatus());

        session.update(allQuery.getResultList().get(id - 1));
        session.flush();
    }
}
