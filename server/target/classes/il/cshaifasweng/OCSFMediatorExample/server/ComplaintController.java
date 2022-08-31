package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ComplaintController {
    private Session session;

    public ComplaintController(Session session) {
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

    public void addComplaint(Complaint complaint) throws Exception {
        session.save(complaint);
        session.flush();
    }

    public void updateData(int id, Complaint complaint) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Complaint> criteriaQuery = builder.createQuery(Complaint.class);
        Root<Complaint> rootEntry = criteriaQuery.from(Complaint.class);
        CriteriaQuery<Complaint> allCriteriaQuery = criteriaQuery.select(rootEntry);
        TypedQuery<Complaint> allQuery = session.createQuery(allCriteriaQuery);

        allQuery.getResultList().get(id - 1).setContent(complaint.getContent());
        allQuery.getResultList().get(id - 1).setDateTime(complaint.getDateTime());
        allQuery.getResultList().get(id - 1).setOrderId(complaint.getOrderId());
        allQuery.getResultList().get(id - 1).setStatus(complaint.getStatus());
        allQuery.getResultList().get(id - 1).setUserId(complaint.getUserId());
        allQuery.getResultList().get(id - 1).setPrice(complaint.getPrice());
        allQuery.getResultList().get(id - 1).setStoreName(complaint.getStoreName());

        session.update(allQuery.getResultList().get(id - 1));
        session.flush();
    }
}
