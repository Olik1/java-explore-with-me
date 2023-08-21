package ru.practicum.main_service.event.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.event.dto.Criteria;
import ru.practicum.main_service.event.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
@Repository
@AllArgsConstructor
public class CustomBuiltEventRepository {
    private final EntityManager entityManager;

    public List<Event> getEvents(Criteria criteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = builder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getUsers() != null) {
            Predicate users = root.get("initiator").in(criteria.getUsers());
            predicates.add(users);
        }

        if (criteria.getStates() != null) {
            Predicate states = root.get("state").in(criteria.getStates());
            predicates.add(states);
        }

        if (criteria.getCategories() != null) {
            Predicate categories = root.get("category").in(criteria.getCategories());
            predicates.add(categories);
        }

        if (criteria.getRangeStart() != null) {
            Predicate rangeStart = builder.greaterThan(root.get("eventDate"), criteria.getRangeStart());
            predicates.add(rangeStart);
        }

        if (criteria.getRangeEnd() != null) {
            Predicate rangeEnd = builder.lessThan(root.get("eventDate"), criteria.getRangeEnd());
            predicates.add(rangeEnd);
        }

        CriteriaQuery<Event> select = criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Event> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(criteria.getFrom());
        typedQuery.setMaxResults(criteria.getSize());

        return typedQuery.getResultList();
    }
}
