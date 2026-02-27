package com.opencbs.core.repositories.implementations;

import com.opencbs.core.domain.taskmanager.TaskEventParticipantsEntity;
import com.opencbs.core.repositories.customs.TaskEventsParticipantsRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unused")
@Repository
public class TaskEventsParticipantsRepositoryRepositoryImpl extends BaseRepository<TaskEventParticipantsEntity> implements TaskEventsParticipantsRepositoryCustom {

    //@Autowired
    public TaskEventsParticipantsRepositoryRepositoryImpl(EntityManager entityManager) {
        super(entityManager, TaskEventParticipantsEntity.class);
    }

    @Override
    public List<TaskEventParticipantsEntity> findAllTaskEventParticipants(String searchString, Pageable pageable) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<TaskEventParticipantsEntity> cq = cb.createQuery(TaskEventParticipantsEntity.class);
        Root<TaskEventParticipantsEntity> root = cq.from(TaskEventParticipantsEntity.class);

        List<Predicate> preds = new ArrayList<>();
        if (searchString != null) {
            String pattern = "%" + searchString.toLowerCase() + "%";
            preds.add(cb.like(cb.lower(root.get("name")), pattern));
        }
        cq.select(root).where(preds.toArray(new Predicate[0]));

        TypedQuery<TaskEventParticipantsEntity> q = getEntityManager().createQuery(cq);
        if (pageable != null) {
            q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            q.setMaxResults(pageable.getPageSize());
        }

        return q.getResultList();
    }
}