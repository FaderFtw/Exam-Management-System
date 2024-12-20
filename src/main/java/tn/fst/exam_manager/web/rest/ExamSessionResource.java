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
import tn.fst.exam_manager.repository.ExamSessionRepository;
import tn.fst.exam_manager.service.ExamSessionService;
import tn.fst.exam_manager.service.dto.ExamSessionDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.ExamSession}.
 */
@RestController
@RequestMapping("/api/exam-sessions")
public class ExamSessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExamSessionResource.class);

    private static final String ENTITY_NAME = "examSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamSessionService examSessionService;

    private final ExamSessionRepository examSessionRepository;

    public ExamSessionResource(ExamSessionService examSessionService, ExamSessionRepository examSessionRepository) {
        this.examSessionService = examSessionService;
        this.examSessionRepository = examSessionRepository;
    }

    /**
     * {@code POST  /exam-sessions} : Create a new examSession.
     *
     * @param examSessionDTO the examSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examSessionDTO, or with status {@code 400 (Bad Request)} if the examSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExamSessionDTO> createExamSession(@Valid @RequestBody ExamSessionDTO examSessionDTO) throws URISyntaxException {
        LOG.debug("REST request to save ExamSession : {}", examSessionDTO);
        if (examSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new examSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        examSessionDTO = examSessionService.save(examSessionDTO);
        return ResponseEntity.created(new URI("/api/exam-sessions/" + examSessionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, examSessionDTO.getId().toString()))
            .body(examSessionDTO);
    }

    /**
     * {@code PUT  /exam-sessions/:id} : Updates an existing examSession.
     *
     * @param id the id of the examSessionDTO to save.
     * @param examSessionDTO the examSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examSessionDTO,
     * or with status {@code 400 (Bad Request)} if the examSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExamSessionDTO> updateExamSession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamSessionDTO examSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExamSession : {}, {}", id, examSessionDTO);
        if (examSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        examSessionDTO = examSessionService.update(examSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examSessionDTO.getId().toString()))
            .body(examSessionDTO);
    }

    /**
     * {@code PATCH  /exam-sessions/:id} : Partial updates given fields of an existing examSession, field will ignore if it is null
     *
     * @param id the id of the examSessionDTO to save.
     * @param examSessionDTO the examSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examSessionDTO,
     * or with status {@code 400 (Bad Request)} if the examSessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examSessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamSessionDTO> partialUpdateExamSession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamSessionDTO examSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExamSession partially : {}, {}", id, examSessionDTO);
        if (examSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamSessionDTO> result = examSessionService.partialUpdate(examSessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examSessionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-sessions} : get all the examSessions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExamSessionDTO>> getAllExamSessions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ExamSessions");
        Page<ExamSessionDTO> page;
        if (eagerload) {
            page = examSessionService.findAllWithEagerRelationships(pageable);
        } else {
            page = examSessionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exam-sessions/:id} : get the "id" examSession.
     *
     * @param id the id of the examSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExamSessionDTO> getExamSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExamSession : {}", id);
        Optional<ExamSessionDTO> examSessionDTO = examSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examSessionDTO);
    }

    /**
     * {@code DELETE  /exam-sessions/:id} : delete the "id" examSession.
     *
     * @param id the id of the examSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExamSession : {}", id);
        examSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
