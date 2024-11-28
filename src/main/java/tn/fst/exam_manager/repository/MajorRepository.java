package tn.fst.exam_manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.fst.exam_manager.domain.Major;

/**
 * Spring Data JPA repository for the Major entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {}
