package ru.practicum.main_service.event.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.event.dto.Criteria;
import ru.practicum.main_service.event.dto.CriteriaPublic;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.request.model.ParticipationRequestStatus;
import ru.practicum.main_service.request.model.Request;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main_service.event.model.State.PUBLISHED;

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

    public List<Event> findEventsPublic(CriteriaPublic criteriaPublic) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = builder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        Predicate publicationStatus = builder.equal(root.get("state"), PUBLISHED);
        predicates.add(publicationStatus);

        if (criteriaPublic.getText() != null) {
            String searchText = "%" + criteriaPublic.getText().toLowerCase() + "%";

            Predicate searchInAnnotation = builder.like(builder.lower(root.get("annotation")), searchText);
            Predicate searchInDescription = builder.like(builder.lower(root.get("description")), searchText);

            Predicate test = builder.or(searchInAnnotation, searchInDescription);
            predicates.add(test);
        }

        if (criteriaPublic.getCategories() != null) {
            Predicate categories = root.get("category").in(criteriaPublic.getCategories());
            predicates.add(categories);
        }

        if (criteriaPublic.getPaid() != null) {
            Predicate paid = builder.equal(root.get("paid"), criteriaPublic.getPaid());
            predicates.add(paid);
        }

        if (criteriaPublic.getRangeStart() != null) {
            Predicate rangeStart = builder.greaterThan(root.get("eventDate"), criteriaPublic.getRangeStart());
            predicates.add(rangeStart);
        }

        if (criteriaPublic.getRangeEnd() != null) {
            Predicate rangeEnd = builder.lessThan(root.get("eventDate"), criteriaPublic.getRangeEnd());
            predicates.add(rangeEnd);
        }

        if (criteriaPublic.getOnlyAvailable()) {
            Subquery<Long> sub = criteriaQuery.subquery(Long.class);
            Root<Request> subRoot = sub.from(Request.class);
            Join<Request, Event> subParticipation = subRoot.join("event");
            sub.select(builder.count(subRoot.get("event")));
            sub.where(builder.equal(root.get("id"), subParticipation.get("id")));
            sub.where(builder.equal(subRoot.get("status"), ParticipationRequestStatus.CONFIRMED));
            Predicate onlyAvailable = builder.greaterThan(root.get("participantLimit"), sub);

            predicates.add(onlyAvailable);
        }

        CriteriaQuery<Event> select = criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Event> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(criteriaPublic.getFrom());
        typedQuery.setMaxResults(criteriaPublic.getSize());

        return typedQuery.getResultList();
    }
}
