package tn.fst.exam_manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.fst.exam_manager.domain.Institute;

/**
 * Spring Data JPA repository for the Institute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstituteRepository extends JpaRepository<Institute, Long> {}
