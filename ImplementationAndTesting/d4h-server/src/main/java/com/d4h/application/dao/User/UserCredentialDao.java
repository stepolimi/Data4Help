package com.d4h.application.dao.User;

import com.d4h.application.model.user.UserCredential;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class UserCredentialDao {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addUserCredential(UserCredential userCredential) throws Exception {
        entityManager.persist(userCredential);
    }

    public void deleteUserCredential(UserCredential userCredential) throws Exception {
        entityManager.remove(userCredential);
    }

    public List<UserCredential> getUserCredentials() throws Exception {
        Query query = entityManager.createQuery("SELECT u from UserCredential as u");
        return query.getResultList();
    }

    public UserCredential getUserCredential(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT u from UserCredential u where u.id like :dataId").setParameter("dataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (UserCredential) query.getSingleResult();
    }
}
