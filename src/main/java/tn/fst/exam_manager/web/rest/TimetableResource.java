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
import tn.fst.exam_manager.repository.TimetableRepository;
import tn.fst.exam_manager.service.TimetableService;
import tn.fst.exam_manager.service.dto.TimetableDTO;
import tn.fst.exam_manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.fst.exam_manager.domain.Timetable}.
 */
@RestController
@RequestMapping("/api/timetables")
public class TimetableResource {

    private static final Logger LOG = LoggerFactory.getLogger(TimetableResource.class);

    private static final String ENTITY_NAME = "timetable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimetableService timetableService;

    private final TimetableRepository timetableRepository;

    public TimetableResource(TimetableService timetableService, TimetableRepository timetableRepository) {
        this.timetableService = timetableService;
        this.timetableRepository = timetableRepository;
    }

    /**
     * {@code POST  /timetables} : Create a new timetable.
     *
     * @param timetableDTO the timetableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timetableDTO, or with status {@code 400 (Bad Request)} if the timetable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TimetableDTO> createTimetable(@Valid @RequestBody TimetableDTO timetableDTO) throws URISyntaxException {
        LOG.debug("REST request to save Timetable : {}", timetableDTO);
        if (timetableDTO.getId() != null) {
            throw new BadRequestAlertException("A new timetable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timetableDTO = timetableService.save(timetableDTO);
        return ResponseEntity.created(new URI("/api/timetables/" + timetableDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, timetableDTO.getId().toString()))
            .body(timetableDTO);
    }

    /**
     * {@code PUT  /timetables/:id} : Updates an existing timetable.
     *
     * @param id the id of the timetableDTO to save.
     * @param timetableDTO the timetableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timetableDTO,
     * or with status {@code 400 (Bad Request)} if the timetableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timetableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimetableDTO> updateTimetable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimetableDTO timetableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Timetable : {}, {}", id, timetableDTO);
        if (timetableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timetableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timetableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timetableDTO = timetableService.update(timetableDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timetableDTO.getId().toString()))
            .body(timetableDTO);
    }

    /**
     * {@code PATCH  /timetables/:id} : Partial updates given fields of an existing timetable, field will ignore if it is null
     *
     * @param id the id of the timetableDTO to save.
     * @param timetableDTO the timetableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timetableDTO,
     * or with status {@code 400 (Bad Request)} if the timetableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timetableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timetableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimetableDTO> partialUpdateTimetable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimetableDTO timetableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Timetable partially : {}, {}", id, timetableDTO);
        if (timetableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timetableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timetableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimetableDTO> result = timetableService.partialUpdate(timetableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timetableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /timetables} : get all the timetables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timetables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TimetableDTO>> getAllTimetables(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Timetables");
        Page<TimetableDTO> page = timetableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /timetables/:id} : get the "id" timetable.
     *
     * @param id the id of the timetableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timetableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimetableDTO> getTimetable(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Timetable : {}", id);
        Optional<TimetableDTO> timetableDTO = timetableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timetableDTO);
    }

    /**
     * {@code DELETE  /timetables/:id} : delete the "id" timetable.
     *
     * @param id the id of the timetableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimetable(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Timetable : {}", id);
        timetableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
