package com.opencbs.core.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportDataSource {

    @PersistenceContext
    private EntityManager entityManager;


    public List executeHQLScript(@NonNull String script){
        return entityManager.createQuery(script).getResultList();
    }

    public <T> List<T> executeNativeScript(@NonNull String sqlQuery, Class<T> clazz){
        // Use JPA EntityManager.createNativeQuery(...) instead of Session.createNativeQuery(...) (deprecated).
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rows = (List<Map<String, Object>>) entityManager.createNativeQuery(sqlQuery).getResultList();

        // List<Map<String, Object>> rows=
        // List<Map<String, Object>> rows = session.createSQLQuery(sqlQuery)
        //         .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

        return map(rows, clazz);
    }

    private <T> List<T> map(List<Map<String, Object>> rows, Class<T> clazz) {
        final ObjectMapper mapper = new ObjectMapper();

        List<T> result = new ArrayList<>();
        for(Map row:rows){
            result.add(mapper.convertValue(row, clazz));
        }

        return result;
    }

}
