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
import tn.fst.exam_manager.repository.StudentClassRepository;
import tn.fst.exam_manager.service.StudentClassService;
import tn.fst.exam_manager.service.dto.StudentClassDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.StudentClass}.
 */
@RestController
@RequestMapping("/api/student-classes")
public class StudentClassResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentClassResource.class);

    private static final String ENTITY_NAME = "studentClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentClassService studentClassService;

    private final StudentClassRepository studentClassRepository;

    public StudentClassResource(StudentClassService studentClassService, StudentClassRepository studentClassRepository) {
        this.studentClassService = studentClassService;
        this.studentClassRepository = studentClassRepository;
    }

    /**
     * {@code POST  /student-classes} : Create a new studentClass.
     *
     * @param studentClassDTO the studentClassDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentClassDTO, or with status {@code 400 (Bad Request)} if the studentClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudentClassDTO> createStudentClass(@Valid @RequestBody StudentClassDTO studentClassDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StudentClass : {}", studentClassDTO);
        if (studentClassDTO.getId() != null) {
            throw new BadRequestAlertException("A new studentClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studentClassDTO = studentClassService.save(studentClassDTO);
        return ResponseEntity.created(new URI("/api/student-classes/" + studentClassDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studentClassDTO.getId().toString()))
            .body(studentClassDTO);
    }

    /**
     * {@code PUT  /student-classes/:id} : Updates an existing studentClass.
     *
     * @param id the id of the studentClassDTO to save.
     * @param studentClassDTO the studentClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentClassDTO,
     * or with status {@code 400 (Bad Request)} if the studentClassDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentClassDTO> updateStudentClass(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentClassDTO studentClassDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StudentClass : {}, {}", id, studentClassDTO);
        if (studentClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studentClassDTO = studentClassService.update(studentClassDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentClassDTO.getId().toString()))
            .body(studentClassDTO);
    }

    /**
     * {@code PATCH  /student-classes/:id} : Partial updates given fields of an existing studentClass, field will ignore if it is null
     *
     * @param id the id of the studentClassDTO to save.
     * @param studentClassDTO the studentClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentClassDTO,
     * or with status {@code 400 (Bad Request)} if the studentClassDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentClassDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentClassDTO> partialUpdateStudentClass(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentClassDTO studentClassDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StudentClass partially : {}, {}", id, studentClassDTO);
        if (studentClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentClassDTO> result = studentClassService.partialUpdate(studentClassDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentClassDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /student-classes} : get all the studentClasses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentClasses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StudentClassDTO>> getAllStudentClasses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of StudentClasses");
        Page<StudentClassDTO> page = studentClassService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-classes/:id} : get the "id" studentClass.
     *
     * @param id the id of the studentClassDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentClassDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentClassDTO> getStudentClass(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StudentClass : {}", id);
        Optional<StudentClassDTO> studentClassDTO = studentClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentClassDTO);
    }

    /**
     * {@code DELETE  /student-classes/:id} : delete the "id" studentClass.
     *
     * @param id the id of the studentClassDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentClass(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StudentClass : {}", id);
        studentClassService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
