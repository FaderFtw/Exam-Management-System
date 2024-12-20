package tn.fst.exam_manager.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.fst.exam_manager.repository.UserAcademicInfoRepository;
import tn.fst.exam_manager.service.UserAcademicInfoService;
import tn.fst.exam_manager.service.dto.UserAcademicInfoDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.UserAcademicInfo}.
 */
@RestController
@RequestMapping("/api/user-academic-infos")
public class UserAcademicInfoResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserAcademicInfoResource.class);

    private static final String ENTITY_NAME = "userAcademicInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAcademicInfoService userAcademicInfoService;

    private final UserAcademicInfoRepository userAcademicInfoRepository;

    public UserAcademicInfoResource(
        UserAcademicInfoService userAcademicInfoService,
        UserAcademicInfoRepository userAcademicInfoRepository
    ) {
        this.userAcademicInfoService = userAcademicInfoService;
        this.userAcademicInfoRepository = userAcademicInfoRepository;
    }

    /**
     * {@code POST  /user-academic-infos} : Create a new userAcademicInfo.
     *
     * @param userAcademicInfoDTO the userAcademicInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAcademicInfoDTO, or with status {@code 400 (Bad Request)} if the userAcademicInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserAcademicInfoDTO> createUserAcademicInfo(@Valid @RequestBody UserAcademicInfoDTO userAcademicInfoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UserAcademicInfo : {}", userAcademicInfoDTO);
        if (userAcademicInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAcademicInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userAcademicInfoDTO = userAcademicInfoService.save(userAcademicInfoDTO);
        return ResponseEntity.created(new URI("/api/user-academic-infos/" + userAcademicInfoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userAcademicInfoDTO.getId().toString()))
            .body(userAcademicInfoDTO);
    }

    /**
     * {@code PUT  /user-academic-infos/:id} : Updates an existing userAcademicInfo.
     *
     * @param id the id of the userAcademicInfoDTO to save.
     * @param userAcademicInfoDTO the userAcademicInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAcademicInfoDTO,
     * or with status {@code 400 (Bad Request)} if the userAcademicInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAcademicInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAcademicInfoDTO> updateUserAcademicInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAcademicInfoDTO userAcademicInfoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserAcademicInfo : {}, {}", id, userAcademicInfoDTO);
        if (userAcademicInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAcademicInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAcademicInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userAcademicInfoDTO = userAcademicInfoService.update(userAcademicInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAcademicInfoDTO.getId().toString()))
            .body(userAcademicInfoDTO);
    }

    /**
     * {@code PATCH  /user-academic-infos/:id} : Partial updates given fields of an existing userAcademicInfo, field will ignore if it is null
     *
     * @param id the id of the userAcademicInfoDTO to save.
     * @param userAcademicInfoDTO the userAcademicInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAcademicInfoDTO,
     * or with status {@code 400 (Bad Request)} if the userAcademicInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAcademicInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAcademicInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAcademicInfoDTO> partialUpdateUserAcademicInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAcademicInfoDTO userAcademicInfoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserAcademicInfo partially : {}, {}", id, userAcademicInfoDTO);
        if (userAcademicInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAcademicInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAcademicInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAcademicInfoDTO> result = userAcademicInfoService.partialUpdate(userAcademicInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAcademicInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-academic-infos} : get all the userAcademicInfos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAcademicInfos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserAcademicInfoDTO>> getAllUserAcademicInfos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of UserAcademicInfos");
        Page<UserAcademicInfoDTO> page;
        if (eagerload) {
            page = userAcademicInfoService.findAllWithEagerRelationships(pageable);
        } else {
            page = userAcademicInfoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-academic-infos/:id} : get the "id" userAcademicInfo.
     *
     * @param id the id of the userAcademicInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAcademicInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAcademicInfoDTO> getUserAcademicInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserAcademicInfo : {}", id);
        Optional<UserAcademicInfoDTO> userAcademicInfoDTO = userAcademicInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAcademicInfoDTO);
    }

    /**
     * {@code DELETE  /user-academic-infos/:id} : delete the "id" userAcademicInfo.
     *
     * @param id the id of the userAcademicInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAcademicInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserAcademicInfo : {}", id);
        userAcademicInfoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
