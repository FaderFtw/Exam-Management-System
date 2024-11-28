package tn.fst.exam_manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.fst.exam_manager.domain.SessionType;

/**
 * Spring Data JPA repository for the SessionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionTypeRepository extends JpaRepository<SessionType, Long> {}
