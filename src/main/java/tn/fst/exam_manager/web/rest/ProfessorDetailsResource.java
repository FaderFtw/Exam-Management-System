package tn.fst.exam_manager.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.fst.exam_manager.repository.ProfessorDetailsRepository;
import tn.fst.exam_manager.service.ProfessorDetailsService;
import tn.fst.exam_manager.service.dto.ProfessorDetailsDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.ProfessorDetails}.
 */
@RestController
@RequestMapping("/api/professor-details")
public class ProfessorDetailsResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessorDetailsResource.class);

    private static final String ENTITY_NAME = "professorDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessorDetailsService professorDetailsService;

    private final ProfessorDetailsRepository professorDetailsRepository;

    public ProfessorDetailsResource(
        ProfessorDetailsService professorDetailsService,
        ProfessorDetailsRepository professorDetailsRepository
    ) {
        this.professorDetailsService = professorDetailsService;
        this.professorDetailsRepository = professorDetailsRepository;
    }

    /**
     * {@code POST  /professor-details} : Create a new professorDetails.
     *
     * @param professorDetailsDTO the professorDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professorDetailsDTO, or with status {@code 400 (Bad Request)} if the professorDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessorDetailsDTO> createProfessorDetails(@Valid @RequestBody ProfessorDetailsDTO professorDetailsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfessorDetails : {}", professorDetailsDTO);
        if (professorDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new professorDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professorDetailsDTO = professorDetailsService.save(professorDetailsDTO);
        return ResponseEntity.created(new URI("/api/professor-details/" + professorDetailsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, professorDetailsDTO.getId().toString()))
            .body(professorDetailsDTO);
    }

    /**
     * {@code PUT  /professor-details/:id} : Updates an existing professorDetails.
     *
     * @param id the id of the professorDetailsDTO to save.
     * @param professorDetailsDTO the professorDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professorDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the professorDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professorDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDetailsDTO> updateProfessorDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessorDetailsDTO professorDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessorDetails : {}, {}", id, professorDetailsDTO);
        if (professorDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professorDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professorDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professorDetailsDTO = professorDetailsService.update(professorDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professorDetailsDTO.getId().toString()))
            .body(professorDetailsDTO);
    }

    /**
     * {@code PATCH  /professor-details/:id} : Partial updates given fields of an existing professorDetails, field will ignore if it is null
     *
     * @param id the id of the professorDetailsDTO to save.
     * @param professorDetailsDTO the professorDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professorDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the professorDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professorDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professorDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessorDetailsDTO> partialUpdateProfessorDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessorDetailsDTO professorDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessorDetails partially : {}, {}", id, professorDetailsDTO);
        if (professorDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professorDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professorDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessorDetailsDTO> result = professorDetailsService.partialUpdate(professorDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professorDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professor-details} : get all the professorDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professorDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessorDetailsDTO>> getAllProfessorDetails(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ProfessorDetails");
        Page<ProfessorDetailsDTO> page = professorDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professor-details/:id} : get the "id" professorDetails.
     *
     * @param id the id of the professorDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professorDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDetailsDTO> getProfessorDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessorDetails : {}", id);
        Optional<ProfessorDetailsDTO> professorDetailsDTO = professorDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professorDetailsDTO);
    }

    /**
     * {@code DELETE  /professor-details/:id} : delete the "id" professorDetails.
     *
     * @param id the id of the professorDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessorDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessorDetails : {}", id);
        professorDetailsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    @Operation(summary = "Import Professor Details from CSV")
    public ResponseEntity<Void> importProfessors(@RequestParam("file") MultipartFile file) {
        try {
            professorDetailsService.importProfessors(file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the exception here (using a logger)
            return ResponseEntity.badRequest().build();
        }
    }
}
