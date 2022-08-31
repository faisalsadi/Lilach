package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Employee;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class EmployeeController {
    private Session session;

    public EmployeeController(Session session) {
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

    public void updateData(int id, Employee employee) throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
        Root<Employee> rootEntry = criteriaQuery.from(Employee.class);
        CriteriaQuery<Employee> allCriteriaQuery = criteriaQuery.select(rootEntry);
        TypedQuery<Employee> allQuery = session.createQuery(allCriteriaQuery);

        allQuery.getResultList().get(id - 1).setEmail(employee.getEmail());
        allQuery.getResultList().get(id - 1).setPassword(employee.getPassword());
        allQuery.getResultList().get(id - 1).setUserName(employee.getUserName());
        allQuery.getResultList().get(id - 1).setStatus(employee.getStatus());

        session.update(allQuery.getResultList().get(id - 1));
        session.flush();
    }
}
