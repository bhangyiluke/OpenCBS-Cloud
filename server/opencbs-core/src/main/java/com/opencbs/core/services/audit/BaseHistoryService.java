package com.opencbs.core.services.audit;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.audit.AuditRevisionEntity;
import com.opencbs.core.dto.audit.ChangedDto;
import com.opencbs.core.dto.audit.HistoryDto;
import com.opencbs.core.helpers.DateHelper;

// import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.data.repository.history.RevisionRepository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class BaseHistoryService<E extends BaseEntity, ID, N extends Number & Comparable<N>> {

    private final RevisionRepository<E, ID, N> revisionRepository;

    @Autowired
    public BaseHistoryService(RevisionRepository<E, ID, N> revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    public List<HistoryDto> getAllRevisions(ID id) throws Exception {
        Revisions<N, E> revisions = revisionRepository.findRevisions(id);
        return this.getListOfChanges(revisions.getContent());
    }

    public HistoryDto getRevisionByDate(ID entityId, LocalDateTime dateTime) throws Exception {
        Revisions<N, E> revisions = this.revisionRepository.findRevisions(entityId);
        E prevObject = null;
        for (Revision<N, E> revision : revisions.getContent()) {
            Instant revisionInstant = revision.getRevisionInstant().orElse(Instant.now());
            LocalDateTime dateTimeRevision = DateHelper.dateToLocalDateTime(revisionInstant);
            if (DateHelper.equal(dateTimeRevision.toLocalDate(), dateTime.toLocalDate())) {
                return convertToHistoryDto(prevObject, revision);
            }
            prevObject = revision.getEntity();
        }
        return null;
    }

    private List<HistoryDto> getListOfChanges(List<Revision<N, E>> content) throws IllegalAccessException {
        List<HistoryDto> changes = new ArrayList<>();
        E prevObject = null;
        for (Revision<N, E> revision : content) {
            HistoryDto historyDto = convertToHistoryDto(prevObject, revision);
            changes.add(historyDto);
            prevObject = revision.getEntity();
        }

        return changes;
    }

    private HistoryDto convertToHistoryDto(E prevObject, Revision<N, E> revision) throws IllegalAccessException {
        Class<?> clazz = revision.getEntity().getClass();
        Long number = revision.getRevisionNumber().map(Number::longValue).orElse(0L);
        var revisionInstant = DateHelper.dateToLocalDateTime(revision.getRevisionInstant().orElse(Instant.now()));
        return HistoryDto.builder()
                .number(number)
                .date(revisionInstant.toLocalDate())
                .changed(buildListOfChange(revision.getEntity(), prevObject, clazz))
                .username(((AuditRevisionEntity) revision.getMetadata().getDelegate()).getUsername())
                .build();
    }

    private <TEntity extends BaseEntity> List<ChangedDto> buildListOfChange(TEntity entity, TEntity prevObject, Class<?> clazz) throws IllegalAccessException {
        if (clazz == BaseEntity.class) {
            return Collections.emptyList();
        }

        List<ChangedDto> changed = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (prevObject == null && entity == null) {
                continue;
            }
            field.setAccessible(true);
            Object prefValue = (prevObject == null) ? null : field.get(prevObject);
            Object currentValue = (entity == null) ? null : field.get(entity);
            if (!comparingValues(currentValue, prefValue)) {
                ChangedDto builder = ChangedDto.builder()
                        .fieldName(field.getName())
                        .prefValue((prefValue == null) ? "" : prefValue.toString())
                        .value(currentValue == null ? "" : buildToString(currentValue))
                        .build();
                changed.add(builder);
            }
        }
        changed.addAll(buildListOfChange(entity, prevObject, clazz.getSuperclass()));

        return changed;
    }

    private String buildToString(Object o) {
        if (o instanceof Iterable){
            StringBuilder sb = new StringBuilder("["+System.lineSeparator());
            ((Iterable)o).forEach(item->sb.append(item.toString()+";"+System.lineSeparator()));
            sb.append("]");
            return sb.toString();
        }

        return o.toString();
    }

    private Boolean comparingValues(Object first, Object second) {
        if (first == null && second == null) {
            return true;
        }
        if (first != null && first.equals(second)) {
            return true;
        }
        if (second != null && second.equals(first)) {
            return true;
        }

        return false;
    }

    public LocalDateTime getDateTimeLastRevision(ID entityId) throws Exception {
        Revision<N, E> lastRevision = revisionRepository.findLastChangeRevision(entityId)
                .orElseThrow(() -> new IllegalArgumentException("No revisions found for entity with id:: " + entityId));
        return DateHelper.dateToLocalDateTime(lastRevision.getRevisionInstant().orElse(Instant.now()));
    }
}
