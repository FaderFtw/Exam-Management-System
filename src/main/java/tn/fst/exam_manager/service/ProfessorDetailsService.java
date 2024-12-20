package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.ProfessorDetails;
import tn.fst.exam_manager.repository.ProfessorDetailsRepository;
import tn.fst.exam_manager.service.dto.ProfessorDetailsDTO;
import tn.fst.exam_manager.service.mapper.ProfessorDetailsMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.ProfessorDetails}.
 */
@Service
@Transactional
public class ProfessorDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessorDetailsService.class);

    private final ProfessorDetailsRepository professorDetailsRepository;

    private final ProfessorDetailsMapper professorDetailsMapper;

    public ProfessorDetailsService(ProfessorDetailsRepository professorDetailsRepository, ProfessorDetailsMapper professorDetailsMapper) {
        this.professorDetailsRepository = professorDetailsRepository;
        this.professorDetailsMapper = professorDetailsMapper;
    }

    /**
     * Save a professorDetails.
     *
     * @param professorDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessorDetailsDTO save(ProfessorDetailsDTO professorDetailsDTO) {
        LOG.debug("Request to save ProfessorDetails : {}", professorDetailsDTO);
        ProfessorDetails professorDetails = professorDetailsMapper.toEntity(professorDetailsDTO);
        professorDetails = professorDetailsRepository.save(professorDetails);
        return professorDetailsMapper.toDto(professorDetails);
    }

    /**
     * Update a professorDetails.
     *
     * @param professorDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessorDetailsDTO update(ProfessorDetailsDTO professorDetailsDTO) {
        LOG.debug("Request to update ProfessorDetails : {}", professorDetailsDTO);
        ProfessorDetails professorDetails = professorDetailsMapper.toEntity(professorDetailsDTO);
        professorDetails = professorDetailsRepository.save(professorDetails);
        return professorDetailsMapper.toDto(professorDetails);
    }

    /**
     * Partially update a professorDetails.
     *
     * @param professorDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessorDetailsDTO> partialUpdate(ProfessorDetailsDTO professorDetailsDTO) {
        LOG.debug("Request to partially update ProfessorDetails : {}", professorDetailsDTO);

        return professorDetailsRepository
            .findById(professorDetailsDTO.getId())
            .map(existingProfessorDetails -> {
                professorDetailsMapper.partialUpdate(existingProfessorDetails, professorDetailsDTO);

                return existingProfessorDetails;
            })
            .map(professorDetailsRepository::save)
            .map(professorDetailsMapper::toDto);
    }

    /**
     * Get all the professorDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessorDetailsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessorDetails");
        return professorDetailsRepository.findAll(pageable).map(professorDetailsMapper::toDto);
    }

    /**
     * Get one professorDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessorDetailsDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessorDetails : {}", id);
        return professorDetailsRepository.findById(id).map(professorDetailsMapper::toDto);
    }

    /**
     * Delete the professorDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessorDetails : {}", id);
        professorDetailsRepository.deleteById(id);
    }
}
