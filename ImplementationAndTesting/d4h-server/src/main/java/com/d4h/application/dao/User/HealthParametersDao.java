package com.d4h.application.dao.User;

import com.d4h.application.model.user.HealthParameters;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class HealthParametersDao {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addHealthParameters(HealthParameters healthParameters) throws Exception {
        entityManager.persist(healthParameters);
    }

    public void deleteHealthParameters(HealthParameters healthParameters) throws Exception {
        entityManager.remove(healthParameters);
    }

    public List<HealthParameters> getHealthParameters() throws Exception {
        Query query = entityManager.createQuery("SELECT u from HealthParameters as u");
        return query.getResultList();
    }

    public HealthParameters getHealthParameters(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT u from HealthParameters u where u.id like :dataId").setParameter("dataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (HealthParameters) query.getSingleResult();
    }
}
