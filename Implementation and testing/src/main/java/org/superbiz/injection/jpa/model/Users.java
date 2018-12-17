package org.superbiz.injection.jpa.model;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.xml.registry.infomodel.User;
import java.util.List;

@Stateful
public class Users {

    @PersistenceContext(unitName = "user-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addUser(User user) throws Exception {
        entityManager.persist(user);
    }

    public void deleteUser(User user) throws Exception {
        entityManager.remove(user);
    }

    public List<User> getUsers() throws Exception {
        Query query = entityManager.createQuery("SELECT m from User as m");
        return query.getResultList();
    }
}