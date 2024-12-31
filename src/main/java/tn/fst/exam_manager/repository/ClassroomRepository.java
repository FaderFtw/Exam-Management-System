package tn.fst.exam_manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.service.dto.ClassroomDTO;

/**
 * Spring Data JPA repository for the Classroom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Page<Classroom> findAllByDepartmentInstituteId(Pageable pageable, Long instituteId);
}
