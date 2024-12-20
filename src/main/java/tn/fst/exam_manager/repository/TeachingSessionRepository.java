package tn.fst.exam_manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.fst.exam_manager.domain.TeachingSession;

/**
 * Spring Data JPA repository for the TeachingSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeachingSessionRepository extends JpaRepository<TeachingSession, Long> {}
