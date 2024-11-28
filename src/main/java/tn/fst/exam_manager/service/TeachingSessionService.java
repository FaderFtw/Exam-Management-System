package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.TeachingSession;
import tn.fst.exam_manager.repository.TeachingSessionRepository;
import tn.fst.exam_manager.service.dto.TeachingSessionDTO;
import tn.fst.exam_manager.service.mapper.TeachingSessionMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.TeachingSession}.
 */
@Service
@Transactional
public class TeachingSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(TeachingSessionService.class);

    private final TeachingSessionRepository teachingSessionRepository;

    private final TeachingSessionMapper teachingSessionMapper;

    public TeachingSessionService(TeachingSessionRepository teachingSessionRepository, TeachingSessionMapper teachingSessionMapper) {
        this.teachingSessionRepository = teachingSessionRepository;
        this.teachingSessionMapper = teachingSessionMapper;
    }

    /**
     * Save a teachingSession.
     *
     * @param teachingSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public TeachingSessionDTO save(TeachingSessionDTO teachingSessionDTO) {
        LOG.debug("Request to save TeachingSession : {}", teachingSessionDTO);
        TeachingSession teachingSession = teachingSessionMapper.toEntity(teachingSessionDTO);
        teachingSession = teachingSessionRepository.save(teachingSession);
        return teachingSessionMapper.toDto(teachingSession);
    }

    /**
     * Update a teachingSession.
     *
     * @param teachingSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public TeachingSessionDTO update(TeachingSessionDTO teachingSessionDTO) {
        LOG.debug("Request to update TeachingSession : {}", teachingSessionDTO);
        TeachingSession teachingSession = teachingSessionMapper.toEntity(teachingSessionDTO);
        teachingSession = teachingSessionRepository.save(teachingSession);
        return teachingSessionMapper.toDto(teachingSession);
    }

    /**
     * Partially update a teachingSession.
     *
     * @param teachingSessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TeachingSessionDTO> partialUpdate(TeachingSessionDTO teachingSessionDTO) {
        LOG.debug("Request to partially update TeachingSession : {}", teachingSessionDTO);

        return teachingSessionRepository
            .findById(teachingSessionDTO.getId())
            .map(existingTeachingSession -> {
                teachingSessionMapper.partialUpdate(existingTeachingSession, teachingSessionDTO);

                return existingTeachingSession;
            })
            .map(teachingSessionRepository::save)
            .map(teachingSessionMapper::toDto);
    }

    /**
     * Get all the teachingSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TeachingSessionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TeachingSessions");
        return teachingSessionRepository.findAll(pageable).map(teachingSessionMapper::toDto);
    }

    /**
     * Get one teachingSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TeachingSessionDTO> findOne(Long id) {
        LOG.debug("Request to get TeachingSession : {}", id);
        return teachingSessionRepository.findById(id).map(teachingSessionMapper::toDto);
    }

    /**
     * Delete the teachingSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TeachingSession : {}", id);
        teachingSessionRepository.deleteById(id);
    }
}
