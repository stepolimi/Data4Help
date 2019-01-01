package com.d4h.application.dao.request;

import com.d4h.application.dao.DaoBase;
import com.d4h.application.model.request.*;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class RequestUserDao {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager = DaoBase.getDaoBase().getEntityManager();

    //requestUser
    public void addRequestUser(RequestUser requestUser) throws Exception {
        entityManager.persist(requestUser);
    }

    public void deleteRequestUser(RequestUser requestUser) throws Exception {
        entityManager.remove(requestUser);
    }

    public List<RequestUser> getRequestesUser() throws Exception {
        Query query = entityManager.createQuery("SELECT r from RequestUser as r");
        return query.getResultList();
    }

    public RequestUser getRequestUser(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT r from RequestUser r where r.id like :requestId").setParameter("requestId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (RequestUser) query.getSingleResult();
    }



    //requestGroup
    public void addRequestGroup(RequestGroup requestGroup) throws Exception {
        entityManager.persist(requestGroup);
    }

    public void deleteRequestGroup(RequestGroup requestGroup) throws Exception {
        entityManager.remove(requestGroup);
    }

    public List<RequestGroup> getRequestesGroup() throws Exception {
        Query query = entityManager.createQuery("SELECT r from RequestGroup as r");
        return query.getResultList();
    }

    public RequestGroup getRequestGroup(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT r from RequestGroup r where r.id like :requestId").setParameter("requestId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (RequestGroup) query.getSingleResult();
    }


    //requestAttributes
    public void addRequestAttributes(RequestAttributes requestAttributes) throws Exception {
        entityManager.persist(requestAttributes);
    }

    public void deleteRequestAttributes(RequestAttributes requestAttributes) throws Exception {
        entityManager.remove(requestAttributes);
    }

    public List<RequestAttributes> getRequestesAttributes() throws Exception {
        Query query = entityManager.createQuery("SELECT r from RequestAttributes as r");
        return query.getResultList();
    }

    public RequestAttributes getRequestAttributes(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT r from RequestAttributes r where r.id like :attributesId").setParameter("attributesId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (RequestAttributes) query.getSingleResult();
    }

    public RequestAttributes getAttributesByRequest(RequestGroup request) throws Exception{
        Query query = entityManager.createQuery("SELECT r from RequestAttributes r where r.requestGroup.id like :dataId").setParameter("dataId", request.getId());
        if(query.getResultList().isEmpty())
            return null;
        return (RequestAttributes) query.getSingleResult();
    }


    //addressRange
    public void addAddressRange(AddressRange addressRange) throws Exception {
        entityManager.persist(addressRange);
    }

    public void deleteAddressRange(AddressRange addressRange) throws Exception {
        entityManager.remove(addressRange);
    }

    public List<AddressRange> getAddressRange() throws Exception {
        Query query = entityManager.createQuery("SELECT r from AddressRange as r");
        return query.getResultList();
    }

    public AddressRange getAddressRange(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT r from AddressRange r where r.id like :addressId").setParameter("addressId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (AddressRange) query.getSingleResult();
    }
}
