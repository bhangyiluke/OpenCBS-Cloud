package com.opencbs.core.repositories.implementations;

import jakarta.persistence.EntityManager;
// import java.math.BigInteger;

public abstract class BaseEventGroupKeyRepository {

    private final EntityManager entityManager;

    private final String sequenceName;

    public BaseEventGroupKeyRepository(EntityManager entityManager, String sequenceName) {
        this.entityManager = entityManager;
        this.sequenceName = sequenceName;
    }

    public Long getNextEventGroupKey() {
        return ((Number)this.entityManager.createNativeQuery(
                String.format("select nextval('%s')", sequenceName)
        )
                .getSingleResult())
                .longValue();
    }
}
