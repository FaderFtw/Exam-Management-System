package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.ExamSession;
import tn.fst.exam_manager.repository.ExamSessionRepository;
import tn.fst.exam_manager.service.dto.ExamSessionDTO;
import tn.fst.exam_manager.service.mapper.ExamSessionMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.ExamSession}.
 */
@Service
@Transactional
public class ExamSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(ExamSessionService.class);

    private final ExamSessionRepository examSessionRepository;

    private final ExamSessionMapper examSessionMapper;

    public ExamSessionService(ExamSessionRepository examSessionRepository, ExamSessionMapper examSessionMapper) {
        this.examSessionRepository = examSessionRepository;
        this.examSessionMapper = examSessionMapper;
    }

    /**
     * Save a examSession.
     *
     * @param examSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamSessionDTO save(ExamSessionDTO examSessionDTO) {
        LOG.debug("Request to save ExamSession : {}", examSessionDTO);
        ExamSession examSession = examSessionMapper.toEntity(examSessionDTO);
        examSession = examSessionRepository.save(examSession);
        return examSessionMapper.toDto(examSession);
    }

    /**
     * Update a examSession.
     *
     * @param examSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamSessionDTO update(ExamSessionDTO examSessionDTO) {
        LOG.debug("Request to update ExamSession : {}", examSessionDTO);
        ExamSession examSession = examSessionMapper.toEntity(examSessionDTO);
        examSession = examSessionRepository.save(examSession);
        return examSessionMapper.toDto(examSession);
    }

    /**
     * Partially update a examSession.
     *
     * @param examSessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExamSessionDTO> partialUpdate(ExamSessionDTO examSessionDTO) {
        LOG.debug("Request to partially update ExamSession : {}", examSessionDTO);

        return examSessionRepository
            .findById(examSessionDTO.getId())
            .map(existingExamSession -> {
                examSessionMapper.partialUpdate(existingExamSession, examSessionDTO);

                return existingExamSession;
            })
            .map(examSessionRepository::save)
            .map(examSessionMapper::toDto);
    }

    /**
     * Get all the examSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamSessionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ExamSessions");
        return examSessionRepository.findAll(pageable).map(examSessionMapper::toDto);
    }

    /**
     * Get all the examSessions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ExamSessionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return examSessionRepository.findAllWithEagerRelationships(pageable).map(examSessionMapper::toDto);
    }

    /**
     * Get one examSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExamSessionDTO> findOne(Long id) {
        LOG.debug("Request to get ExamSession : {}", id);
        return examSessionRepository.findOneWithEagerRelationships(id).map(examSessionMapper::toDto);
    }

    /**
     * Delete the examSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExamSession : {}", id);
        examSessionRepository.deleteById(id);
    }
}
