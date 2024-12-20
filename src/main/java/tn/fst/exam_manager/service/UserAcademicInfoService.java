package tn.fst.exam_manager.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.domain.UserAcademicInfo;
import tn.fst.exam_manager.repository.UserAcademicInfoRepository;
import tn.fst.exam_manager.service.dto.UserAcademicInfoDTO;
import tn.fst.exam_manager.service.mapper.UserAcademicInfoMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.UserAcademicInfo}.
 */
@Service
@Transactional
public class UserAcademicInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAcademicInfoService.class);

    private final UserAcademicInfoRepository userAcademicInfoRepository;

    private final UserAcademicInfoMapper userAcademicInfoMapper;

    public UserAcademicInfoService(UserAcademicInfoRepository userAcademicInfoRepository, UserAcademicInfoMapper userAcademicInfoMapper) {
        this.userAcademicInfoRepository = userAcademicInfoRepository;
        this.userAcademicInfoMapper = userAcademicInfoMapper;
    }

    /**
     * Save a userAcademicInfo.
     *
     * @param userAcademicInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public UserAcademicInfoDTO save(UserAcademicInfoDTO userAcademicInfoDTO) {
        LOG.debug("Request to save UserAcademicInfo : {}", userAcademicInfoDTO);
        UserAcademicInfo userAcademicInfo = userAcademicInfoMapper.toEntity(userAcademicInfoDTO);
        userAcademicInfo = userAcademicInfoRepository.save(userAcademicInfo);
        return userAcademicInfoMapper.toDto(userAcademicInfo);
    }

    /**
     * Update a userAcademicInfo.
     *
     * @param userAcademicInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public UserAcademicInfoDTO update(UserAcademicInfoDTO userAcademicInfoDTO) {
        LOG.debug("Request to update UserAcademicInfo : {}", userAcademicInfoDTO);
        UserAcademicInfo userAcademicInfo = userAcademicInfoMapper.toEntity(userAcademicInfoDTO);
        userAcademicInfo = userAcademicInfoRepository.save(userAcademicInfo);
        return userAcademicInfoMapper.toDto(userAcademicInfo);
    }

    /**
     * Partially update a userAcademicInfo.
     *
     * @param userAcademicInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserAcademicInfoDTO> partialUpdate(UserAcademicInfoDTO userAcademicInfoDTO) {
        LOG.debug("Request to partially update UserAcademicInfo : {}", userAcademicInfoDTO);

        return userAcademicInfoRepository
            .findById(userAcademicInfoDTO.getId())
            .map(existingUserAcademicInfo -> {
                userAcademicInfoMapper.partialUpdate(existingUserAcademicInfo, userAcademicInfoDTO);

                return existingUserAcademicInfo;
            })
            .map(userAcademicInfoRepository::save)
            .map(userAcademicInfoMapper::toDto);
    }

    /**
     * Get all the userAcademicInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserAcademicInfoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserAcademicInfos");
        return userAcademicInfoRepository.findAll(pageable).map(userAcademicInfoMapper::toDto);
    }

    /**
     * Get all the userAcademicInfos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserAcademicInfoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userAcademicInfoRepository.findAllWithEagerRelationships(pageable).map(userAcademicInfoMapper::toDto);
    }

    /**
     * Get one userAcademicInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserAcademicInfoDTO> findOne(Long id) {
        LOG.debug("Request to get UserAcademicInfo : {}", id);
        return userAcademicInfoRepository.findOneWithEagerRelationships(id).map(userAcademicInfoMapper::toDto);
    }

    /**
     * Delete the userAcademicInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserAcademicInfo : {}", id);
        userAcademicInfoRepository.deleteById(id);
    }
}
