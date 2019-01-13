package com.d4h.application.dao.User;

import com.d4h.application.dao.DaoBase;
import com.d4h.application.model.user.UserData;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class UserDataDao extends DaoBase{
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager = DaoBase.getDaoBase().getEntityManager();

    public void addUserData(UserData userData) throws Exception {
        entityManager.persist(userData);
    }

    public void deleteUserData(UserData userData) throws Exception {
        entityManager.remove(userData);
    }

    public List<UserData> getUserDatas() throws Exception {
        Query query = entityManager.createQuery("SELECT u from UserData as u");
        return query.getResultList();
    }

    public UserData getUserData(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT u from UserData u where u.id like :dataId").setParameter("dataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (UserData) query.getSingleResult();
    }
}
