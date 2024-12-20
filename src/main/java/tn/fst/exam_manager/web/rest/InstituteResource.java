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
import tn.fst.exam_manager.repository.InstituteRepository;
import tn.fst.exam_manager.service.InstituteService;
import tn.fst.exam_manager.service.dto.InstituteDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.Institute}.
 */
@RestController
@RequestMapping("/api/institutes")
public class InstituteResource {

    private static final Logger LOG = LoggerFactory.getLogger(InstituteResource.class);

    private static final String ENTITY_NAME = "institute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstituteService instituteService;

    private final InstituteRepository instituteRepository;

    public InstituteResource(InstituteService instituteService, InstituteRepository instituteRepository) {
        this.instituteService = instituteService;
        this.instituteRepository = instituteRepository;
    }

    /**
     * {@code POST  /institutes} : Create a new institute.
     *
     * @param instituteDTO the instituteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instituteDTO, or with status {@code 400 (Bad Request)} if the institute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InstituteDTO> createInstitute(@Valid @RequestBody InstituteDTO instituteDTO) throws URISyntaxException {
        LOG.debug("REST request to save Institute : {}", instituteDTO);
        if (instituteDTO.getId() != null) {
            throw new BadRequestAlertException("A new institute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        instituteDTO = instituteService.save(instituteDTO);
        return ResponseEntity.created(new URI("/api/institutes/" + instituteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, instituteDTO.getId().toString()))
            .body(instituteDTO);
    }

    /**
     * {@code PUT  /institutes/:id} : Updates an existing institute.
     *
     * @param id the id of the instituteDTO to save.
     * @param instituteDTO the instituteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instituteDTO,
     * or with status {@code 400 (Bad Request)} if the instituteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instituteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InstituteDTO> updateInstitute(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InstituteDTO instituteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Institute : {}, {}", id, instituteDTO);
        if (instituteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instituteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instituteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        instituteDTO = instituteService.update(instituteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instituteDTO.getId().toString()))
            .body(instituteDTO);
    }

    /**
     * {@code PATCH  /institutes/:id} : Partial updates given fields of an existing institute, field will ignore if it is null
     *
     * @param id the id of the instituteDTO to save.
     * @param instituteDTO the instituteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instituteDTO,
     * or with status {@code 400 (Bad Request)} if the instituteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the instituteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the instituteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InstituteDTO> partialUpdateInstitute(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InstituteDTO instituteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Institute partially : {}, {}", id, instituteDTO);
        if (instituteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instituteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instituteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InstituteDTO> result = instituteService.partialUpdate(instituteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instituteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /institutes} : get all the institutes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of institutes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InstituteDTO>> getAllInstitutes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Institutes");
        Page<InstituteDTO> page = instituteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /institutes/:id} : get the "id" institute.
     *
     * @param id the id of the instituteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instituteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InstituteDTO> getInstitute(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Institute : {}", id);
        Optional<InstituteDTO> instituteDTO = instituteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instituteDTO);
    }

    /**
     * {@code DELETE  /institutes/:id} : delete the "id" institute.
     *
     * @param id the id of the instituteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstitute(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Institute : {}", id);
        instituteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
