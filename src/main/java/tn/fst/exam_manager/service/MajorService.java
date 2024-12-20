package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.Major;
import tn.fst.exam_manager.repository.MajorRepository;
import tn.fst.exam_manager.service.dto.MajorDTO;
import tn.fst.exam_manager.service.mapper.MajorMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.Major}.
 */
@Service
@Transactional
public class MajorService {

    private static final Logger LOG = LoggerFactory.getLogger(MajorService.class);

    private final MajorRepository majorRepository;

    private final MajorMapper majorMapper;

    public MajorService(MajorRepository majorRepository, MajorMapper majorMapper) {
        this.majorRepository = majorRepository;
        this.majorMapper = majorMapper;
    }

    /**
     * Save a major.
     *
     * @param majorDTO the entity to save.
     * @return the persisted entity.
     */
    public MajorDTO save(MajorDTO majorDTO) {
        LOG.debug("Request to save Major : {}", majorDTO);
        Major major = majorMapper.toEntity(majorDTO);
        major = majorRepository.save(major);
        return majorMapper.toDto(major);
    }

    /**
     * Update a major.
     *
     * @param majorDTO the entity to save.
     * @return the persisted entity.
     */
    public MajorDTO update(MajorDTO majorDTO) {
        LOG.debug("Request to update Major : {}", majorDTO);
        Major major = majorMapper.toEntity(majorDTO);
        major = majorRepository.save(major);
        return majorMapper.toDto(major);
    }

    /**
     * Partially update a major.
     *
     * @param majorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MajorDTO> partialUpdate(MajorDTO majorDTO) {
        LOG.debug("Request to partially update Major : {}", majorDTO);

        return majorRepository
            .findById(majorDTO.getId())
            .map(existingMajor -> {
                majorMapper.partialUpdate(existingMajor, majorDTO);

                return existingMajor;
            })
            .map(majorRepository::save)
            .map(majorMapper::toDto);
    }

    /**
     * Get all the majors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MajorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Majors");
        return majorRepository.findAll(pageable).map(majorMapper::toDto);
    }

    /**
     * Get one major by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MajorDTO> findOne(Long id) {
        LOG.debug("Request to get Major : {}", id);
        return majorRepository.findById(id).map(majorMapper::toDto);
    }

    /**
     * Delete the major by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Major : {}", id);
        majorRepository.deleteById(id);
    }
}
