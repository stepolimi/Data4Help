package com.d4h.application.dao.User;

import com.d4h.application.dao.DaoBase;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.user.*;

import javax.ejb.Stateful;
import javax.persistence.*;
import java.util.List;

@Stateful
public class UsersDao extends DaoBase {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager = DaoBase.getDaoBase().getEntityManager();

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

    public User getUserById(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT u from User u where u.id like :dataId").setParameter("dataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (User) query.getSingleResult();
    }

    public User getUserByCode(String code) throws Exception{
        Query query = entityManager.createQuery("SELECT u from User u where u.userData.fiscalCode like :dataId").setParameter("dataId", code);
        if(query.getResultList().isEmpty())
            return null;
        return (User) query.getSingleResult();
    }

    public User getUserByMail(String email) throws Exception{
        Query query = entityManager.createQuery("SELECT u from User u where u.credential.email like :dataId").setParameter("dataId", email);
        if(query.getResultList().isEmpty())
            return null;
        return (User) query.getSingleResult();
    }

    public User getUserByUserData(UserData data) throws Exception{
        Query query = entityManager.createQuery("SELECT u from User u where u.userData.id like :dataId").setParameter("dataId", data.getId());
        if(query.getResultList().isEmpty())
            return null;
        return (User) query.getSingleResult();
    }



    //user credentials
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


    //user data
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

    public UserData getUserDataByUser(User user) throws Exception{
        Query query = entityManager.createQuery("SELECT u from UserData u where u.user.id like :dataId").setParameter("dataId", user.getId());
        if(query.getResultList().isEmpty())
            return null;
        return (UserData) query.getSingleResult();
    }


    //wearable
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


    //HealthParam
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

    public HealthParameters getHealthParam(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT u from HealthParameters u where u.id like :dataId").setParameter("dataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (HealthParameters) query.getSingleResult();
    }

    public List<HealthParameters> getUserHealthParam(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT u from HealthParameters u where u.user.id like :userId").setParameter("userId", id);
        return query.getResultList();
    }


    //address
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