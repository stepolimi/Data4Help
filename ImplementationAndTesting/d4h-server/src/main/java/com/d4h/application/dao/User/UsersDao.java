package com.d4h.application.dao.User;

import com.d4h.application.model.user.User;

import javax.ejb.Stateful;
import javax.persistence.*;
import java.util.List;

@Stateful
public class UsersDao {

    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addUser(User user) throws Exception {
        entityManager.persist(user);
    }

    public void deleteUser(User user) throws Exception {
        entityManager.remove(user);
    }

    public List<User> getUsers() throws Exception {
        Query query = entityManager.createQuery("SELECT u from User as u");
        return query.getResultList();
    }

    public User getUser(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT u from User u where u.id like :dataId").setParameter("dataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (User) query.getSingleResult();
    }
}