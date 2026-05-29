package com.opencbs.core.repositories.implementations;

import com.opencbs.core.domain.BaseEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public abstract class BaseRepository<Tentity extends BaseEntity> {

    private EntityManager entityManager;
    protected Class<Tentity> clazz;

    public BaseRepository(EntityManager entityManager, Class<Tentity> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    /**
     * Returns the JPA CriteriaBuilder for building CriteriaQuery instances.
     */
    protected CriteriaBuilder criteriaBuilder() {
        return getEntityManager().getCriteriaBuilder();
    }

    /**
     * Create an empty CriteriaQuery for the repository entity type.
     */
    protected CriteriaQuery<Tentity> createCriteriaQuery() {
        CriteriaQuery<Tentity> cq = criteriaBuilder().createQuery(clazz);
        cq.from(clazz);
        return cq;
    }

    /**
     * Create a TypedQuery from a CriteriaQuery.
     */
    protected TypedQuery<Tentity> toTypedQuery(CriteriaQuery<Tentity> cq) {
        return getEntityManager().createQuery(cq);
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
