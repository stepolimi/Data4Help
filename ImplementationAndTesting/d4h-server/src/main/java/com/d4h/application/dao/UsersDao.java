package com.d4h.application.dao;

import com.d4h.application.model.User;

import javax.ejb.Stateful;
import javax.persistence.*;
import java.util.List;

@Stateful
public class UsersDao {

    @PersistenceContext(unitName = "user-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addUser(User user) throws Exception {
        entityManager.getTransaction().begin();
        entityManager.persist(user);

        entityManager.getTransaction().commit();
    }

    public void deleteUser(User user) throws Exception {
        entityManager.remove(user);
    }

    public List<User> getUsers() throws Exception {
        Query query = entityManager.createQuery("SELECT u from User as u");
        return query.getResultList();
    }
}