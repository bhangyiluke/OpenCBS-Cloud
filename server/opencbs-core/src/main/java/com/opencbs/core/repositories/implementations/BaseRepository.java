package com.opencbs.core.repositories.implementations;

import com.opencbs.core.domain.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import jakarta.persistence.EntityManager;

public abstract class BaseRepository<Tentity extends BaseEntity> {

    private EntityManager entityManager;
    protected Class<Tentity> clazz;

    public BaseRepository(EntityManager entityManager, Class<Tentity> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    protected Criteria createCriteria(String alias) throws HibernateException{
        return ((Criteria) getSession()).createCriteria(alias);
    }

    protected Criteria createCriteria(Class clazz, String alias) {
        return ((Criteria) getSession()).createCriteria(alias);
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
