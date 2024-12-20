package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.repository.ClassroomRepository;
import tn.fst.exam_manager.service.dto.ClassroomDTO;
import tn.fst.exam_manager.service.mapper.ClassroomMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.Classroom}.
 */
@Service
@Transactional
public class ClassroomService {

    private static final Logger LOG = LoggerFactory.getLogger(ClassroomService.class);

    private final ClassroomRepository classroomRepository;

    private final ClassroomMapper classroomMapper;

    public ClassroomService(ClassroomRepository classroomRepository, ClassroomMapper classroomMapper) {
        this.classroomRepository = classroomRepository;
        this.classroomMapper = classroomMapper;
    }

    /**
     * Save a classroom.
     *
     * @param classroomDTO the entity to save.
     * @return the persisted entity.
     */
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
    @Transactional(readOnly = true)
    public Page<ClassroomDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Classrooms");
        return classroomRepository.findAll(pageable).map(classroomMapper::toDto);
    }

    /**
     * Get one classroom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClassroomDTO> findOne(Long id) {
        LOG.debug("Request to get Classroom : {}", id);
        return classroomRepository.findById(id).map(classroomMapper::toDto);
    }

    /**
     * Delete the classroom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Classroom : {}", id);
        classroomRepository.deleteById(id);
    }
}
