package com.srgcro.feasbase.web.rest;

import com.srgcro.feasbase.repository.SpecializationRepository;
import com.srgcro.feasbase.service.SpecializationService;
import com.srgcro.feasbase.service.dto.SpecializationDTO;
import com.srgcro.feasbase.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.srgcro.feasbase.domain.Specialization}.
 */
@RestController
@RequestMapping("/api")
public class SpecializationResource {

    private final Logger log = LoggerFactory.getLogger(SpecializationResource.class);

    private static final String ENTITY_NAME = "specialization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecializationService specializationService;

    private final SpecializationRepository specializationRepository;

    public SpecializationResource(SpecializationService specializationService, SpecializationRepository specializationRepository) {
        this.specializationService = specializationService;
        this.specializationRepository = specializationRepository;
    }

    /**
     * {@code POST  /specializations} : Create a new specialization.
     *
     * @param specializationDTO the specializationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specializationDTO, or with status {@code 400 (Bad Request)} if the specialization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specializations")
    public ResponseEntity<SpecializationDTO> createSpecialization(@Valid @RequestBody SpecializationDTO specializationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Specialization : {}", specializationDTO);
        if (specializationDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecializationDTO result = specializationService.save(specializationDTO);
        return ResponseEntity
            .created(new URI("/api/specializations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specializations/:id} : Updates an existing specialization.
     *
     * @param id the id of the specializationDTO to save.
     * @param specializationDTO the specializationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specializationDTO,
     * or with status {@code 400 (Bad Request)} if the specializationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specializationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specializations/{id}")
    public ResponseEntity<SpecializationDTO> updateSpecialization(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecializationDTO specializationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Specialization : {}, {}", id, specializationDTO);
        if (specializationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specializationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specializationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecializationDTO result = specializationService.save(specializationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specializationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /specializations/:id} : Partial updates given fields of an existing specialization, field will ignore if it is null
     *
     * @param id the id of the specializationDTO to save.
     * @param specializationDTO the specializationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specializationDTO,
     * or with status {@code 400 (Bad Request)} if the specializationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specializationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specializationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/specializations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SpecializationDTO> partialUpdateSpecialization(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecializationDTO specializationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Specialization partially : {}, {}", id, specializationDTO);
        if (specializationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specializationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specializationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecializationDTO> result = specializationService.partialUpdate(specializationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specializationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /specializations} : get all the specializations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specializations in body.
     */
    @GetMapping("/specializations")
    public List<SpecializationDTO> getAllSpecializations() {
        log.debug("REST request to get all Specializations");
        return specializationService.findAll();
    }

    /**
     * {@code GET  /specializations/:id} : get the "id" specialization.
     *
     * @param id the id of the specializationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specializationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specializations/{id}")
    public ResponseEntity<SpecializationDTO> getSpecialization(@PathVariable Long id) {
        log.debug("REST request to get Specialization : {}", id);
        Optional<SpecializationDTO> specializationDTO = specializationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specializationDTO);
    }

    /**
     * {@code DELETE  /specializations/:id} : delete the "id" specialization.
     *
     * @param id the id of the specializationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specializations/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable Long id) {
        log.debug("REST request to delete Specialization : {}", id);
        specializationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
