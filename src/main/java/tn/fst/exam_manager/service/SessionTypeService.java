package tn.fst.exam_manager.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.SessionType;
import tn.fst.exam_manager.repository.SessionTypeRepository;
import tn.fst.exam_manager.service.dto.SessionTypeDTO;
import tn.fst.exam_manager.service.mapper.SessionTypeMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.SessionType}.
 */
@Service
@Transactional
public class SessionTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionTypeService.class);

    private final SessionTypeRepository sessionTypeRepository;

    private final SessionTypeMapper sessionTypeMapper;

    public SessionTypeService(SessionTypeRepository sessionTypeRepository, SessionTypeMapper sessionTypeMapper) {
        this.sessionTypeRepository = sessionTypeRepository;
        this.sessionTypeMapper = sessionTypeMapper;
    }

    /**
     * Save a sessionType.
     *
     * @param sessionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public SessionTypeDTO save(SessionTypeDTO sessionTypeDTO) {
        LOG.debug("Request to save SessionType : {}", sessionTypeDTO);
        SessionType sessionType = sessionTypeMapper.toEntity(sessionTypeDTO);
        sessionType = sessionTypeRepository.save(sessionType);
        return sessionTypeMapper.toDto(sessionType);
    }

    /**
     * Update a sessionType.
     *
     * @param sessionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public SessionTypeDTO update(SessionTypeDTO sessionTypeDTO) {
        LOG.debug("Request to update SessionType : {}", sessionTypeDTO);
        SessionType sessionType = sessionTypeMapper.toEntity(sessionTypeDTO);
        sessionType = sessionTypeRepository.save(sessionType);
        return sessionTypeMapper.toDto(sessionType);
    }

    /**
     * Partially update a sessionType.
     *
     * @param sessionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SessionTypeDTO> partialUpdate(SessionTypeDTO sessionTypeDTO) {
        LOG.debug("Request to partially update SessionType : {}", sessionTypeDTO);

        return sessionTypeRepository
            .findById(sessionTypeDTO.getId())
            .map(existingSessionType -> {
                sessionTypeMapper.partialUpdate(existingSessionType, sessionTypeDTO);

                return existingSessionType;
            })
            .map(sessionTypeRepository::save)
            .map(sessionTypeMapper::toDto);
    }

    /**
     * Get all the sessionTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SessionTypeDTO> findAll() {
        LOG.debug("Request to get all SessionTypes");
        return sessionTypeRepository.findAll().stream().map(sessionTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the sessionTypes where ExamSession is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SessionTypeDTO> findAllWhereExamSessionIsNull() {
        LOG.debug("Request to get all sessionTypes where ExamSession is null");
        return StreamSupport.stream(sessionTypeRepository.findAll().spliterator(), false)
            .filter(sessionType -> sessionType.getExamSession() == null)
            .map(sessionTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one sessionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SessionTypeDTO> findOne(Long id) {
        LOG.debug("Request to get SessionType : {}", id);
        return sessionTypeRepository.findById(id).map(sessionTypeMapper::toDto);
    }

    /**
     * Delete the sessionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SessionType : {}", id);
        sessionTypeRepository.deleteById(id);
    }
}
