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
import tn.fst.exam_manager.repository.ExamRepository;
import tn.fst.exam_manager.service.ExamService;
import tn.fst.exam_manager.service.dto.ExamDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.Exam}.
 */
@RestController
@RequestMapping("/api/exams")
public class ExamResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExamResource.class);

    private static final String ENTITY_NAME = "exam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamService examService;

    private final ExamRepository examRepository;

    public ExamResource(ExamService examService, ExamRepository examRepository) {
        this.examService = examService;
        this.examRepository = examRepository;
    }

    /**
     * {@code POST  /exams} : Create a new exam.
     *
     * @param examDTO the examDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examDTO, or with status {@code 400 (Bad Request)} if the exam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExamDTO> createExam(@Valid @RequestBody ExamDTO examDTO) throws URISyntaxException {
        LOG.debug("REST request to save Exam : {}", examDTO);
        if (examDTO.getId() != null) {
            throw new BadRequestAlertException("A new exam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        examDTO = examService.save(examDTO);
        return ResponseEntity.created(new URI("/api/exams/" + examDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, examDTO.getId().toString()))
            .body(examDTO);
    }

    /**
     * {@code PUT  /exams/:id} : Updates an existing exam.
     *
     * @param id the id of the examDTO to save.
     * @param examDTO the examDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examDTO,
     * or with status {@code 400 (Bad Request)} if the examDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExamDTO> updateExam(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamDTO examDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Exam : {}, {}", id, examDTO);
        if (examDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        examDTO = examService.update(examDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examDTO.getId().toString()))
            .body(examDTO);
    }

    /**
     * {@code PATCH  /exams/:id} : Partial updates given fields of an existing exam, field will ignore if it is null
     *
     * @param id the id of the examDTO to save.
     * @param examDTO the examDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examDTO,
     * or with status {@code 400 (Bad Request)} if the examDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamDTO> partialUpdateExam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamDTO examDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Exam partially : {}, {}", id, examDTO);
        if (examDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamDTO> result = examService.partialUpdate(examDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exams} : get all the exams.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exams in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExamDTO>> getAllExams(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Exams");
        Page<ExamDTO> page;
        if (eagerload) {
            page = examService.findAllWithEagerRelationships(pageable);
        } else {
            page = examService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exams/:id} : get the "id" exam.
     *
     * @param id the id of the examDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> getExam(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Exam : {}", id);
        Optional<ExamDTO> examDTO = examService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examDTO);
    }

    /**
     * {@code DELETE  /exams/:id} : delete the "id" exam.
     *
     * @param id the id of the examDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Exam : {}", id);
        examService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
