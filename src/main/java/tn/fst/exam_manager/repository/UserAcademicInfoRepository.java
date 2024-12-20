package tn.fst.exam_manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.fst.exam_manager.domain.UserAcademicInfo;

/**
 * Spring Data JPA repository for the UserAcademicInfo entity.
 */
@Repository
public interface UserAcademicInfoRepository extends JpaRepository<UserAcademicInfo, Long> {
    default Optional<UserAcademicInfo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserAcademicInfo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserAcademicInfo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userAcademicInfo from UserAcademicInfo userAcademicInfo left join fetch userAcademicInfo.user left join fetch userAcademicInfo.department left join fetch userAcademicInfo.institute",
        countQuery = "select count(userAcademicInfo) from UserAcademicInfo userAcademicInfo"
    )
    Page<UserAcademicInfo> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select userAcademicInfo from UserAcademicInfo userAcademicInfo left join fetch userAcademicInfo.user left join fetch userAcademicInfo.department left join fetch userAcademicInfo.institute"
    )
    List<UserAcademicInfo> findAllWithToOneRelationships();

    @Query(
        "select userAcademicInfo from UserAcademicInfo userAcademicInfo left join fetch userAcademicInfo.user left join fetch userAcademicInfo.department left join fetch userAcademicInfo.institute where userAcademicInfo.id =:id"
    )
    Optional<UserAcademicInfo> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<UserAcademicInfo> findByUserId(Long id);

    Optional<UserAcademicInfo> findByUserLogin(String login);
}
