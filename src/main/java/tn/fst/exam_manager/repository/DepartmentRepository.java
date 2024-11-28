package tn.fst.exam_manager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.fst.exam_manager.domain.Department;

/**
 * Spring Data JPA repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select department from Department department where department.users.login = ?#{authentication.name}")
    List<Department> findByUsersIsCurrentUser();
}
