package tn.fst.exam_manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.fst.exam_manager.domain.ExamSession;

/**
 * Spring Data JPA repository for the ExamSession entity.
 *
 * When extending this class, extend ExamSessionRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ExamSessionRepository extends ExamSessionRepositoryWithBagRelationships, JpaRepository<ExamSession, Long> {
    default Optional<ExamSession> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<ExamSession> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<ExamSession> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
