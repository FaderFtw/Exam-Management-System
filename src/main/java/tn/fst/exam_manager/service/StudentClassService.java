package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.StudentClass;
import tn.fst.exam_manager.repository.StudentClassRepository;
import tn.fst.exam_manager.service.dto.StudentClassDTO;
import tn.fst.exam_manager.service.mapper.StudentClassMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.StudentClass}.
 */
@Service
@Transactional
public class StudentClassService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentClassService.class);

    private final StudentClassRepository studentClassRepository;

    private final StudentClassMapper studentClassMapper;

    public StudentClassService(StudentClassRepository studentClassRepository, StudentClassMapper studentClassMapper) {
        this.studentClassRepository = studentClassRepository;
        this.studentClassMapper = studentClassMapper;
    }

    /**
     * Save a studentClass.
     *
     * @param studentClassDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentClassDTO save(StudentClassDTO studentClassDTO) {
        LOG.debug("Request to save StudentClass : {}", studentClassDTO);
        StudentClass studentClass = studentClassMapper.toEntity(studentClassDTO);
        studentClass = studentClassRepository.save(studentClass);
        return studentClassMapper.toDto(studentClass);
    }

    /**
     * Update a studentClass.
     *
     * @param studentClassDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentClassDTO update(StudentClassDTO studentClassDTO) {
        LOG.debug("Request to update StudentClass : {}", studentClassDTO);
        StudentClass studentClass = studentClassMapper.toEntity(studentClassDTO);
        studentClass = studentClassRepository.save(studentClass);
        return studentClassMapper.toDto(studentClass);
    }

    /**
     * Partially update a studentClass.
     *
     * @param studentClassDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentClassDTO> partialUpdate(StudentClassDTO studentClassDTO) {
        LOG.debug("Request to partially update StudentClass : {}", studentClassDTO);

        return studentClassRepository
            .findById(studentClassDTO.getId())
            .map(existingStudentClass -> {
                studentClassMapper.partialUpdate(existingStudentClass, studentClassDTO);

                return existingStudentClass;
            })
            .map(studentClassRepository::save)
            .map(studentClassMapper::toDto);
    }

    /**
     * Get all the studentClasses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentClassDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all StudentClasses");
        return studentClassRepository.findAll(pageable).map(studentClassMapper::toDto);
    }

    /**
     * Get one studentClass by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentClassDTO> findOne(Long id) {
        LOG.debug("Request to get StudentClass : {}", id);
        return studentClassRepository.findById(id).map(studentClassMapper::toDto);
    }

    /**
     * Delete the studentClass by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete StudentClass : {}", id);
        studentClassRepository.deleteById(id);
    }
}
