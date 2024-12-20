package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.Timetable;
import tn.fst.exam_manager.repository.TimetableRepository;
import tn.fst.exam_manager.service.dto.TimetableDTO;
import tn.fst.exam_manager.service.mapper.TimetableMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.Timetable}.
 */
@Service
@Transactional
public class TimetableService {

    private static final Logger LOG = LoggerFactory.getLogger(TimetableService.class);

    private final TimetableRepository timetableRepository;

    private final TimetableMapper timetableMapper;

    public TimetableService(TimetableRepository timetableRepository, TimetableMapper timetableMapper) {
        this.timetableRepository = timetableRepository;
        this.timetableMapper = timetableMapper;
    }

    /**
     * Save a timetable.
     *
     * @param timetableDTO the entity to save.
     * @return the persisted entity.
     */
    public TimetableDTO save(TimetableDTO timetableDTO) {
        LOG.debug("Request to save Timetable : {}", timetableDTO);
        Timetable timetable = timetableMapper.toEntity(timetableDTO);
        timetable = timetableRepository.save(timetable);
        return timetableMapper.toDto(timetable);
    }

    /**
     * Update a timetable.
     *
     * @param timetableDTO the entity to save.
     * @return the persisted entity.
     */
    public TimetableDTO update(TimetableDTO timetableDTO) {
        LOG.debug("Request to update Timetable : {}", timetableDTO);
        Timetable timetable = timetableMapper.toEntity(timetableDTO);
        timetable = timetableRepository.save(timetable);
        return timetableMapper.toDto(timetable);
    }

    /**
     * Partially update a timetable.
     *
     * @param timetableDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TimetableDTO> partialUpdate(TimetableDTO timetableDTO) {
        LOG.debug("Request to partially update Timetable : {}", timetableDTO);

        return timetableRepository
            .findById(timetableDTO.getId())
            .map(existingTimetable -> {
                timetableMapper.partialUpdate(existingTimetable, timetableDTO);

                return existingTimetable;
            })
            .map(timetableRepository::save)
            .map(timetableMapper::toDto);
    }

    /**
     * Get all the timetables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TimetableDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Timetables");
        return timetableRepository.findAll(pageable).map(timetableMapper::toDto);
    }

    /**
     * Get one timetable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TimetableDTO> findOne(Long id) {
        LOG.debug("Request to get Timetable : {}", id);
        return timetableRepository.findById(id).map(timetableMapper::toDto);
    }

    /**
     * Delete the timetable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Timetable : {}", id);
        timetableRepository.deleteById(id);
    }
}
