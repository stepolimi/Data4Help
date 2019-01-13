package com.d4h.application.dao.ThirdParty;

import com.d4h.application.dao.DaoBase;
import com.d4h.application.model.thirdParty.AcquiredUserData;
import com.d4h.application.model.thirdParty.ThirdParty;
import com.d4h.application.model.thirdParty.ThirdPartyCredential;
import com.d4h.application.model.thirdParty.ThirdPartyData;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class ThirdPartyDao {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager = DaoBase.getDaoBase().getEntityManager();

    public void updateDB(){
        entityManager.flush();
    }

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

    public ThirdParty getThirdPartyById(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT t from ThirdParty t where t.id like :thirdPartyId").setParameter("thirdPartyId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (ThirdParty) query.getSingleResult();
    }

    public ThirdParty getThirdPartyByMail(String email) throws Exception{
        Query query = entityManager.createQuery("SELECT t from ThirdParty t where t.credential.email like :dataId").setParameter("dataId", email);
        if(query.getResultList().isEmpty())
            return null;
        return (ThirdParty) query.getSingleResult();
    }


    //thirdPartyData
    public void addThirdPartyData(ThirdPartyData thirdPartyData) throws Exception {
        entityManager.persist(thirdPartyData);
    }

    public void deleteThirdPartyData(ThirdPartyData thirdPartyData) throws Exception {
        entityManager.remove(thirdPartyData);
    }

    public List<ThirdPartyData> getThirdPartiesData() throws Exception {
        Query query = entityManager.createQuery("SELECT t from ThirdPartyData as t");
        return query.getResultList();
    }

    public ThirdPartyData getThirdPartyData(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT t from ThirdPartyData t where t.id like :thirdPartyId").setParameter("thirdPartyId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (ThirdPartyData) query.getSingleResult();
    }


    //thirdPartyCredential

    public void addThirdPartyCredential(ThirdPartyCredential thirdPartyCredential) throws Exception {
        entityManager.persist(thirdPartyCredential);
    }

    public void deleteThirdPartyCredential(ThirdPartyCredential thirdPartyCredential) throws Exception {
        entityManager.remove(thirdPartyCredential);
    }

    public List<ThirdPartyCredential> getThirdPartiesCredentials() throws Exception {
        Query query = entityManager.createQuery("SELECT t from ThirdPartyCredential as t");
        return query.getResultList();
    }

    public ThirdPartyCredential getThirdPartyCredential(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT t from ThirdPartyCredential t where t.id like :thirdPartyId").setParameter("thirdPartyId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (ThirdPartyCredential) query.getSingleResult();
    }

    public ThirdPartyCredential getThirdPartyCredentialByMail(String mail) throws Exception{
        Query query = entityManager.createQuery("SELECT t from ThirdPartyCredential t where t.email like :thirdPartyId").setParameter("thirdPartyId", mail);
        if(query.getResultList().isEmpty())
            return null;
        return (ThirdPartyCredential) query.getSingleResult();
    }


    //acquiredUserData
    public void addAcquiredUserData(AcquiredUserData data) throws Exception {
        entityManager.persist(data);
    }

    public void deleteAcquiredUserData(AcquiredUserData acquiredUserData) throws Exception {
        entityManager.remove(acquiredUserData);
    }

    public List<AcquiredUserData> getAcquiredUsersData() throws Exception {
        Query query = entityManager.createQuery("SELECT t from AcquiredUserData as t");
        return query.getResultList();
    }

    public AcquiredUserData getAcquiredUserData(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT a from AcquiredUserData a where a.id like :acquiredDataId").setParameter("acquiredDataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (AcquiredUserData) query.getSingleResult();
    }
}
