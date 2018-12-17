package org.superbiz.injection.jpa.model;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class Credentials {

    @PersistenceContext(unitName = "credential-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void addCredential(Credential credential) throws Exception {
        entityManager.persist(credential);
    }

    public void deleteCredential(Credential credential) throws Exception {
        entityManager.remove(credential);
    }

    public List<Credential> getCredential() throws Exception {
        Query query = entityManager.createQuery("SELECT cr from Credential as cr");
        return query.getResultList();
    }
}
