package tn.fst.exam_manager.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import tn.fst.exam_manager.domain.ExamSession;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ExamSessionRepositoryWithBagRelationshipsImpl implements ExamSessionRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String EXAMSESSIONS_PARAMETER = "examSessions";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ExamSession> fetchBagRelationships(Optional<ExamSession> examSession) {
        return examSession.map(this::fetchDepartments);
    }

    @Override
    public Page<ExamSession> fetchBagRelationships(Page<ExamSession> examSessions) {
        return new PageImpl<>(
            fetchBagRelationships(examSessions.getContent()),
            examSessions.getPageable(),
            examSessions.getTotalElements()
        );
    }

    @Override
    public List<ExamSession> fetchBagRelationships(List<ExamSession> examSessions) {
        return Optional.of(examSessions).map(this::fetchDepartments).orElse(Collections.emptyList());
    }

    ExamSession fetchDepartments(ExamSession result) {
        return entityManager
            .createQuery(
                "select examSession from ExamSession examSession left join fetch examSession.departments where examSession.id = :id",
                ExamSession.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ExamSession> fetchDepartments(List<ExamSession> examSessions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, examSessions.size()).forEach(index -> order.put(examSessions.get(index).getId(), index));
        List<ExamSession> result = entityManager
            .createQuery(
                "select examSession from ExamSession examSession left join fetch examSession.departments where examSession in :examSessions",
                ExamSession.class
            )
            .setParameter(EXAMSESSIONS_PARAMETER, examSessions)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
