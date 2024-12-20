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
import tn.fst.exam_manager.repository.MajorRepository;
import tn.fst.exam_manager.service.MajorService;
import tn.fst.exam_manager.service.dto.MajorDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.Major}.
 */
@RestController
@RequestMapping("/api/majors")
public class MajorResource {

    private static final Logger LOG = LoggerFactory.getLogger(MajorResource.class);

    private static final String ENTITY_NAME = "major";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MajorService majorService;

    private final MajorRepository majorRepository;

    public MajorResource(MajorService majorService, MajorRepository majorRepository) {
        this.majorService = majorService;
        this.majorRepository = majorRepository;
    }

    /**
     * {@code POST  /majors} : Create a new major.
     *
     * @param majorDTO the majorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new majorDTO, or with status {@code 400 (Bad Request)} if the major has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MajorDTO> createMajor(@Valid @RequestBody MajorDTO majorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Major : {}", majorDTO);
        if (majorDTO.getId() != null) {
            throw new BadRequestAlertException("A new major cannot already have an ID", ENTITY_NAME, "idexists");
        }
        majorDTO = majorService.save(majorDTO);
        return ResponseEntity.created(new URI("/api/majors/" + majorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, majorDTO.getId().toString()))
            .body(majorDTO);
    }

    /**
     * {@code PUT  /majors/:id} : Updates an existing major.
     *
     * @param id the id of the majorDTO to save.
     * @param majorDTO the majorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated majorDTO,
     * or with status {@code 400 (Bad Request)} if the majorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the majorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MajorDTO> updateMajor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MajorDTO majorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Major : {}, {}", id, majorDTO);
        if (majorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, majorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!majorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        majorDTO = majorService.update(majorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, majorDTO.getId().toString()))
            .body(majorDTO);
    }

    /**
     * {@code PATCH  /majors/:id} : Partial updates given fields of an existing major, field will ignore if it is null
     *
     * @param id the id of the majorDTO to save.
     * @param majorDTO the majorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated majorDTO,
     * or with status {@code 400 (Bad Request)} if the majorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the majorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the majorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MajorDTO> partialUpdateMajor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MajorDTO majorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Major partially : {}, {}", id, majorDTO);
        if (majorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, majorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!majorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MajorDTO> result = majorService.partialUpdate(majorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, majorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /majors} : get all the majors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of majors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MajorDTO>> getAllMajors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Majors");
        Page<MajorDTO> page = majorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /majors/:id} : get the "id" major.
     *
     * @param id the id of the majorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the majorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MajorDTO> getMajor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Major : {}", id);
        Optional<MajorDTO> majorDTO = majorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(majorDTO);
    }

    /**
     * {@code DELETE  /majors/:id} : delete the "id" major.
     *
     * @param id the id of the majorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Major : {}", id);
        majorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
