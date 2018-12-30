package com.d4h.application.dao.User;

import com.d4h.application.model.user.Wearable;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class WearableDao {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addWearable(Wearable wearable) throws Exception {
        entityManager.persist(wearable);
    }

    public void deleteWearable(Wearable wearable) throws Exception {
        entityManager.remove(wearable);
    }

    public List<Wearable> getWearables() throws Exception {
        Query query = entityManager.createQuery("SELECT u from Wearable as u");
        return query.getResultList();
    }

    public Wearable getWearable(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT u from Wearable u where u.id like :dataId").setParameter("dataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (Wearable) query.getSingleResult();
    }
}
