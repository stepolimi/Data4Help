package com.d4h.application.dao.User;

import com.d4h.application.dao.DaoBase;
import com.d4h.application.model.groupOfUsers.AnonymousUserData;
import com.d4h.application.model.groupOfUsers.GroupOfUsers;
import com.d4h.application.model.groupOfUsers.GroupUsersData;
import com.d4h.application.model.request.AddressRange;
import com.d4h.application.model.request.RequestAttributes;
import com.d4h.application.model.request.RequestGroup;
import com.d4h.application.model.request.RequestUser;
import com.d4h.application.model.thirdParty.AcquiredUserData;
import com.d4h.application.model.thirdParty.ThirdParty;
import com.d4h.application.model.thirdParty.ThirdPartyCredential;
import com.d4h.application.model.thirdParty.ThirdPartyData;
import com.d4h.application.model.user.*;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class UsersDao extends DaoBase {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager = DaoBase.getDaoBase().getEntityManager();

    public void updateDB(){
        entityManager.flush();
    }

    public void addUser(User user) throws Exception {
        entityManager.persist(user);
    }

    public void deleteUser(User user) throws Exception {
        entityManager.remove(user);
    }

    public void deleteUsers() throws Exception{
        Query query = entityManager.createQuery("DELETE from User");
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

    public UserCredential getUserCredentialByMail(String mail) throws Exception{
        Query query = entityManager.createQuery("SELECT u from UserCredential u where u.email like :dataId").setParameter("dataId", mail);
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