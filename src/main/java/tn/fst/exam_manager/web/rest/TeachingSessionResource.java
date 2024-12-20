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
import tn.fst.exam_manager.repository.TeachingSessionRepository;
import tn.fst.exam_manager.service.TeachingSessionService;
import tn.fst.exam_manager.service.dto.TeachingSessionDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.TeachingSession}.
 */
@RestController
@RequestMapping("/api/teaching-sessions")
public class TeachingSessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TeachingSessionResource.class);

    private static final String ENTITY_NAME = "teachingSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeachingSessionService teachingSessionService;

    private final TeachingSessionRepository teachingSessionRepository;

    public TeachingSessionResource(TeachingSessionService teachingSessionService, TeachingSessionRepository teachingSessionRepository) {
        this.teachingSessionService = teachingSessionService;
        this.teachingSessionRepository = teachingSessionRepository;
    }

    /**
     * {@code POST  /teaching-sessions} : Create a new teachingSession.
     *
     * @param teachingSessionDTO the teachingSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teachingSessionDTO, or with status {@code 400 (Bad Request)} if the teachingSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TeachingSessionDTO> createTeachingSession(@Valid @RequestBody TeachingSessionDTO teachingSessionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TeachingSession : {}", teachingSessionDTO);
        if (teachingSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new teachingSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        teachingSessionDTO = teachingSessionService.save(teachingSessionDTO);
        return ResponseEntity.created(new URI("/api/teaching-sessions/" + teachingSessionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, teachingSessionDTO.getId().toString()))
            .body(teachingSessionDTO);
    }

    /**
     * {@code PUT  /teaching-sessions/:id} : Updates an existing teachingSession.
     *
     * @param id the id of the teachingSessionDTO to save.
     * @param teachingSessionDTO the teachingSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teachingSessionDTO,
     * or with status {@code 400 (Bad Request)} if the teachingSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teachingSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeachingSessionDTO> updateTeachingSession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TeachingSessionDTO teachingSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TeachingSession : {}, {}", id, teachingSessionDTO);
        if (teachingSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teachingSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teachingSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        teachingSessionDTO = teachingSessionService.update(teachingSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teachingSessionDTO.getId().toString()))
            .body(teachingSessionDTO);
    }

    /**
     * {@code PATCH  /teaching-sessions/:id} : Partial updates given fields of an existing teachingSession, field will ignore if it is null
     *
     * @param id the id of the teachingSessionDTO to save.
     * @param teachingSessionDTO the teachingSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teachingSessionDTO,
     * or with status {@code 400 (Bad Request)} if the teachingSessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the teachingSessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the teachingSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TeachingSessionDTO> partialUpdateTeachingSession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TeachingSessionDTO teachingSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TeachingSession partially : {}, {}", id, teachingSessionDTO);
        if (teachingSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teachingSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teachingSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeachingSessionDTO> result = teachingSessionService.partialUpdate(teachingSessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teachingSessionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /teaching-sessions} : get all the teachingSessions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teachingSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TeachingSessionDTO>> getAllTeachingSessions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of TeachingSessions");
        Page<TeachingSessionDTO> page = teachingSessionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /teaching-sessions/:id} : get the "id" teachingSession.
     *
     * @param id the id of the teachingSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teachingSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeachingSessionDTO> getTeachingSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TeachingSession : {}", id);
        Optional<TeachingSessionDTO> teachingSessionDTO = teachingSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teachingSessionDTO);
    }

    /**
     * {@code DELETE  /teaching-sessions/:id} : delete the "id" teachingSession.
     *
     * @param id the id of the teachingSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeachingSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TeachingSession : {}", id);
        teachingSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
