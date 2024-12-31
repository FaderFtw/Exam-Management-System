package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.repository.ClassroomRepository;
import tn.fst.exam_manager.security.SecurityUtils;
import tn.fst.exam_manager.service.dto.ClassroomDTO;
import tn.fst.exam_manager.service.mapper.ClassroomMapper;

/**
 * Service Implementation for managing
 * {@link tn.fst.exam_manager.domain.Classroom}.
 */
@Service
@Transactional
public class ClassroomService {

    private static final Logger LOG = LoggerFactory.getLogger(ClassroomService.class);

    private final ClassroomRepository classroomRepository;

    private final ClassroomMapper classroomMapper;

    private final SecurityService securityService;

    public ClassroomService(ClassroomRepository classroomRepository, ClassroomMapper classroomMapper, SecurityService securityService) {
        this.classroomRepository = classroomRepository;
        this.classroomMapper = classroomMapper;
        this.securityService = securityService;
    }

    /**
     * Save a classroom.
     *
     * @param classroomDTO the entity to save.
     * @return the persisted entity.
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or @securityService.isAuthorizedToWriteClassroom(#classroomDTO.id)")
    public ClassroomDTO save(ClassroomDTO classroomDTO) {
        LOG.debug("Request to save Classroom : {}", classroomDTO);
        Classroom classroom = classroomMapper.toEntity(classroomDTO);
        classroom = classroomRepository.save(classroom);
        return classroomMapper.toDto(classroom);
    }

    /**
     * Update a classroom.
     *
     * @param classroomDTO the entity to save.
     * @return the persisted entity.
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or @securityService.isAuthorizedToWriteClassroom(#classroomDTO.id)")
    public ClassroomDTO update(ClassroomDTO classroomDTO) {
        LOG.debug("Request to update Classroom : {}", classroomDTO);
        Classroom classroom = classroomMapper.toEntity(classroomDTO);
        classroom = classroomRepository.save(classroom);
        return classroomMapper.toDto(classroom);
    }

    /**
     * Partially update a classroom.
     *
     * @param classroomDTO the entity to update partially.
     * @return the persisted entity.
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or @securityService.isAuthorizedToWriteClassroom(#classroomDTO.id)")
    public Optional<ClassroomDTO> partialUpdate(ClassroomDTO classroomDTO) {
        LOG.debug("Request to partially update Classroom : {}", classroomDTO);

        return classroomRepository
            .findById(classroomDTO.getId())
            .map(existingClassroom -> {
                classroomMapper.partialUpdate(existingClassroom, classroomDTO);

                return existingClassroom;
            })
            .map(classroomRepository::save)
            .map(classroomMapper::toDto);
    }

    /**
     * Get all the classrooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_INSTITUTE_ADMIN') or hasRole('ROLE_DEPARTMENT_ADMIN')")
    @Transactional(readOnly = true)
    public Page<ClassroomDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Classrooms");

        if (SecurityUtils.isCurrentUserSuperAdmin()) {
            return classroomRepository.findAll(pageable).map(classroomMapper::toDto);
        }

        if (SecurityUtils.isCurrentUserInstituteAdmin() || SecurityUtils.isCurrentUserDepartmentAdmin()) {
            Long userInsituteId = securityService.getCurrentUserInstituteId();
            return classroomRepository.findAllByDepartmentInstituteId(pageable, userInsituteId).map(classroomMapper::toDto);
        }

        return Page.empty();
    }

    /**
     * Get one classroom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or @securityService.isAuthorizedToReadClassroom(#id)")
    public Optional<ClassroomDTO> findOne(Long id) {
        LOG.debug("Request to get Classroom : {}", id);
        return classroomRepository.findById(id).map(classroomMapper::toDto);
    }

    /**
     * Delete the classroom by id.
     *
     * @param id the id of the entity.
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or @securityService.isAuthorizedToWriteClassroom(#id)")
    public void delete(Long id) {
        LOG.debug("Request to delete Classroom : {}", id);
        classroomRepository.deleteById(id);
    }
}
