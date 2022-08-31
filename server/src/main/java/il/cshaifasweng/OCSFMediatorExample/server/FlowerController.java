package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class FlowerController {
    private Session session;

    public FlowerController(Session session) {
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

    public void updateData(int id, Flower flower) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Flower> criteriaQuery = builder.createQuery(Flower.class);
        Root<Flower> rootEntry = criteriaQuery.from(Flower.class);
        CriteriaQuery<Flower> allCriteriaQuery = criteriaQuery.select(rootEntry);
        TypedQuery<Flower> allQuery = session.createQuery(allCriteriaQuery);

        allQuery.getResultList().get(id - 1).setName(flower.getName());
        allQuery.getResultList().get(id - 1).setType(flower.getType());
        allQuery.getResultList().get(id - 1).setDiscount(flower.getDiscount());
        allQuery.getResultList().get(id - 1).setImageurl(flower.getImageurl());
        allQuery.getResultList().get(id - 1).setPrice(flower.getPrice());
        allQuery.getResultList().get(id - 1).setColor(flower.getColor());
        allQuery.getResultList().get(id - 1).setSale(flower.getSale());
        allQuery.getResultList().get(id - 1).setDescription(flower.getDescription());
        allQuery.getResultList().get(id - 1).setSerialNumber(flower.getSerialNumber());
        allQuery.getResultList().get(id - 1).setStatus(flower.getStatus());

        session.update(allQuery.getResultList().get(id - 1));
        session.flush();
    }

    public void addFlower(Flower flower) throws Exception {
        session.save(flower);
        session.flush();
    }

    public void deleteFlower(Flower flower) throws Exception {
        session.delete(flower);
        session.flush();
    }
}
