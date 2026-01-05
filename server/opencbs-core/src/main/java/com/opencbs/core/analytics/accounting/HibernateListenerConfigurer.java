package com.opencbs.core.analytics.accounting;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

// @Component
public class HibernateListenerConfigurer {

    // @PersistenceUnit
    // private EntityManagerFactory entityManagerFactory;

    // //@Autowired
    // private EventListener listener;

    // @PostConstruct
    // protected void init() {
    //     if(entityManagerFactory instanceof EntityManagerFactory) {
    //         final var hibernateEntityManagerFactory =  entityManagerFactory;
    //         final SessionFactoryImpl sessionFactory = (SessionFactoryImpl) hibernateEntityManagerFactory.getSessionFactory();
    //         final EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
    //         registry.getEventListenerGroup(EventType.PERSIST).appendListener(listener);
    //     }
    // }
}
