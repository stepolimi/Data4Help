package com.d4h.application.dao.ThirdParty;

import com.d4h.application.model.thirdParty.ThirdParty;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class ThirdPartyDao {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addThirdParty(ThirdParty thirdParty) throws Exception {
        entityManager.persist(thirdParty);
    }

    public void deleteThirdParty(ThirdParty thirdParty) throws Exception {
        entityManager.remove(thirdParty);
    }

    public List<ThirdParty> getThirdParties() throws Exception {
        Query query = entityManager.createQuery("SELECT t from ThirdParty as t");
        return query.getResultList();
    }

    public ThirdParty getThirdParty(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT t from ThirdParty t where t.id like :thirdPartyId").setParameter("thirdPartyId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (ThirdParty) query.getSingleResult();
    }
}
