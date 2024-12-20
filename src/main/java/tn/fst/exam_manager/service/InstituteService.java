package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.Institute;
import tn.fst.exam_manager.repository.InstituteRepository;
import tn.fst.exam_manager.service.dto.InstituteDTO;
import tn.fst.exam_manager.service.mapper.InstituteMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.Institute}.
 */
@Service
@Transactional
public class InstituteService {

    private static final Logger LOG = LoggerFactory.getLogger(InstituteService.class);

    private final InstituteRepository instituteRepository;

    private final InstituteMapper instituteMapper;

    public InstituteService(InstituteRepository instituteRepository, InstituteMapper instituteMapper) {
        this.instituteRepository = instituteRepository;
        this.instituteMapper = instituteMapper;
    }

    /**
     * Save a institute.
     *
     * @param instituteDTO the entity to save.
     * @return the persisted entity.
     */
    public InstituteDTO save(InstituteDTO instituteDTO) {
        LOG.debug("Request to save Institute : {}", instituteDTO);
        Institute institute = instituteMapper.toEntity(instituteDTO);
        institute = instituteRepository.save(institute);
        return instituteMapper.toDto(institute);
    }

    /**
     * Update a institute.
     *
     * @param instituteDTO the entity to save.
     * @return the persisted entity.
     */
    public InstituteDTO update(InstituteDTO instituteDTO) {
        LOG.debug("Request to update Institute : {}", instituteDTO);
        Institute institute = instituteMapper.toEntity(instituteDTO);
        institute = instituteRepository.save(institute);
        return instituteMapper.toDto(institute);
    }

    /**
     * Partially update a institute.
     *
     * @param instituteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InstituteDTO> partialUpdate(InstituteDTO instituteDTO) {
        LOG.debug("Request to partially update Institute : {}", instituteDTO);

        return instituteRepository
            .findById(instituteDTO.getId())
            .map(existingInstitute -> {
                instituteMapper.partialUpdate(existingInstitute, instituteDTO);

                return existingInstitute;
            })
            .map(instituteRepository::save)
            .map(instituteMapper::toDto);
    }

    /**
     * Get all the institutes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InstituteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Institutes");
        return instituteRepository.findAll(pageable).map(instituteMapper::toDto);
    }

    /**
     * Get one institute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InstituteDTO> findOne(Long id) {
        LOG.debug("Request to get Institute : {}", id);
        return instituteRepository.findById(id).map(instituteMapper::toDto);
    }

    /**
     * Delete the institute by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Institute : {}", id);
        instituteRepository.deleteById(id);
    }
}
