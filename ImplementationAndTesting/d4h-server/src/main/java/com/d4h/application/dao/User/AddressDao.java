package com.d4h.application.dao.User;

import com.d4h.application.model.user.Address;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class AddressDao {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addAddress(Address address) throws Exception {
        entityManager.persist(address);
    }

    public void deleteAddress(Address address) throws Exception {
        entityManager.remove(address);
    }

    public List<Address> getAddress() throws Exception {
        Query query = entityManager.createQuery("SELECT u from Address as u");
        return query.getResultList();
    }

    public Address getAddress(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT u from Address u where u.id like :dataId").setParameter("dataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (Address) query.getSingleResult();
    }
}
