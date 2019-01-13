package com.d4h.application.dao.GroupOfUsers;

import com.d4h.application.dao.DaoBase;
import com.d4h.application.model.groupOfUsers.AnonymousUserData;
import com.d4h.application.model.groupOfUsers.GroupOfUsers;
import com.d4h.application.model.groupOfUsers.GroupUsersData;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class GroupOfUsersDao {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager = DaoBase.getDaoBase().getEntityManager();

    public void updateDB(){
        entityManager.flush();
    }

    //groupOfUsers
    public void addGroupOfUsers(GroupOfUsers groupOfUsers) throws Exception {
        entityManager.persist(groupOfUsers);
    }

    public void deleteGroupOfUsers(GroupOfUsers groupOfUsers) throws Exception {
        entityManager.remove(groupOfUsers);
    }

    public List<GroupOfUsers> getGroupOfUsers() throws Exception {
        Query query = entityManager.createQuery("SELECT r from GroupOfUsers as r");
        return query.getResultList();
    }

    public GroupOfUsers getGroupOfUsers(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT r from GroupOfUsers r where r.id like :groupId").setParameter("groupId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (GroupOfUsers) query.getSingleResult();
    }


    //groupUsersData
    public void addGroupUsersData(GroupUsersData groupUsersData) throws Exception {
        entityManager.persist(groupUsersData);
    }

    public void deleteGroupUsersData(GroupUsersData groupUsersData) throws Exception {
        entityManager.remove(groupUsersData);
    }

    public List<GroupUsersData> getGroupUsersData() throws Exception {
        Query query = entityManager.createQuery("SELECT r from GroupUsersData as r");
        return query.getResultList();
    }

    public GroupUsersData getGroupUsersData(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT r from GroupUsersData r where r.id like :groupId").setParameter("groupId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (GroupUsersData) query.getSingleResult();
    }



    //usersData
    public void addAnonymousUserData(AnonymousUserData anonymousUserDataDao) throws Exception {
        entityManager.persist(anonymousUserDataDao);
    }

    public void deleteAnonymousUserData(AnonymousUserData anonymousUserData) throws Exception {
        entityManager.remove(anonymousUserData);
    }

    public List<AnonymousUserData> getAnonymousUserData() throws Exception {
        Query query = entityManager.createQuery("SELECT r from AnonymousUserData as r");
        return query.getResultList();
    }

    public AnonymousUserData getAnonymousUserData(String id) throws Exception{
        Query query = entityManager.createQuery("SELECT r from AnonymousUserData r where r.id like :dataId").setParameter("dataId", id);
        if(query.getResultList().isEmpty())
            return null;
        return (AnonymousUserData) query.getSingleResult();
    }
}
