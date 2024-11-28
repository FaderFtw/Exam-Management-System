package tn.fst.exam_manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.fst.exam_manager.domain.ProfessorDetails;

/**
 * Spring Data JPA repository for the ProfessorDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessorDetailsRepository extends JpaRepository<ProfessorDetails, Long> {}
