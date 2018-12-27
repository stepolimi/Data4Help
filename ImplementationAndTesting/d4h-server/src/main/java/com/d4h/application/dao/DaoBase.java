package com.d4h.application.dao;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateful
public class DaoBase {
    @PersistenceContext(unitName = "client-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    private static DaoBase daoBase;

    private DaoBase(){}

    public static DaoBase getDaoBase() {
        if(daoBase == null)
            daoBase= new DaoBase();
        return daoBase;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
