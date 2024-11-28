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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.fst.exam_manager.repository.SessionTypeRepository;
import tn.fst.exam_manager.service.SessionTypeService;
import tn.fst.exam_manager.service.dto.SessionTypeDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.SessionType}.
 */
@RestController
@RequestMapping("/api/session-types")
public class SessionTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(SessionTypeResource.class);

    private static final String ENTITY_NAME = "sessionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionTypeService sessionTypeService;

    private final SessionTypeRepository sessionTypeRepository;

    public SessionTypeResource(SessionTypeService sessionTypeService, SessionTypeRepository sessionTypeRepository) {
        this.sessionTypeService = sessionTypeService;
        this.sessionTypeRepository = sessionTypeRepository;
    }

    /**
     * {@code POST  /session-types} : Create a new sessionType.
     *
     * @param sessionTypeDTO the sessionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionTypeDTO, or with status {@code 400 (Bad Request)} if the sessionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SessionTypeDTO> createSessionType(@Valid @RequestBody SessionTypeDTO sessionTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save SessionType : {}", sessionTypeDTO);
        if (sessionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new sessionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sessionTypeDTO = sessionTypeService.save(sessionTypeDTO);
        return ResponseEntity.created(new URI("/api/session-types/" + sessionTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sessionTypeDTO.getId().toString()))
            .body(sessionTypeDTO);
    }

    /**
     * {@code PUT  /session-types/:id} : Updates an existing sessionType.
     *
     * @param id the id of the sessionTypeDTO to save.
     * @param sessionTypeDTO the sessionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the sessionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SessionTypeDTO> updateSessionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SessionTypeDTO sessionTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SessionType : {}, {}", id, sessionTypeDTO);
        if (sessionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sessionTypeDTO = sessionTypeService.update(sessionTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionTypeDTO.getId().toString()))
            .body(sessionTypeDTO);
    }

    /**
     * {@code PATCH  /session-types/:id} : Partial updates given fields of an existing sessionType, field will ignore if it is null
     *
     * @param id the id of the sessionTypeDTO to save.
     * @param sessionTypeDTO the sessionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the sessionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionTypeDTO> partialUpdateSessionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SessionTypeDTO sessionTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SessionType partially : {}, {}", id, sessionTypeDTO);
        if (sessionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionTypeDTO> result = sessionTypeService.partialUpdate(sessionTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /session-types} : get all the sessionTypes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionTypes in body.
     */
    @GetMapping("")
    public List<SessionTypeDTO> getAllSessionTypes(@RequestParam(name = "filter", required = false) String filter) {
        if ("examsession-is-null".equals(filter)) {
            LOG.debug("REST request to get all SessionTypes where examSession is null");
            return sessionTypeService.findAllWhereExamSessionIsNull();
        }
        LOG.debug("REST request to get all SessionTypes");
        return sessionTypeService.findAll();
    }

    /**
     * {@code GET  /session-types/:id} : get the "id" sessionType.
     *
     * @param id the id of the sessionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessionTypeDTO> getSessionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SessionType : {}", id);
        Optional<SessionTypeDTO> sessionTypeDTO = sessionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionTypeDTO);
    }

    /**
     * {@code DELETE  /session-types/:id} : delete the "id" sessionType.
     *
     * @param id the id of the sessionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSessionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SessionType : {}", id);
        sessionTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
