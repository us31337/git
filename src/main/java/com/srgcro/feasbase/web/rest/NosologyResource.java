package com.srgcro.feasbase.web.rest;

import com.srgcro.feasbase.repository.NosologyRepository;
import com.srgcro.feasbase.service.NosologyService;
import com.srgcro.feasbase.service.dto.NosologyDTO;
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
 * REST controller for managing {@link com.srgcro.feasbase.domain.Nosology}.
 */
@RestController
@RequestMapping("/api")
public class NosologyResource {

    private final Logger log = LoggerFactory.getLogger(NosologyResource.class);

    private static final String ENTITY_NAME = "nosology";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NosologyService nosologyService;

    private final NosologyRepository nosologyRepository;

    public NosologyResource(NosologyService nosologyService, NosologyRepository nosologyRepository) {
        this.nosologyService = nosologyService;
        this.nosologyRepository = nosologyRepository;
    }

    /**
     * {@code POST  /nosologies} : Create a new nosology.
     *
     * @param nosologyDTO the nosologyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nosologyDTO, or with status {@code 400 (Bad Request)} if the nosology has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nosologies")
    public ResponseEntity<NosologyDTO> createNosology(@Valid @RequestBody NosologyDTO nosologyDTO) throws URISyntaxException {
        log.debug("REST request to save Nosology : {}", nosologyDTO);
        if (nosologyDTO.getId() != null) {
            throw new BadRequestAlertException("A new nosology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NosologyDTO result = nosologyService.save(nosologyDTO);
        return ResponseEntity
            .created(new URI("/api/nosologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nosologies/:id} : Updates an existing nosology.
     *
     * @param id the id of the nosologyDTO to save.
     * @param nosologyDTO the nosologyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nosologyDTO,
     * or with status {@code 400 (Bad Request)} if the nosologyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nosologyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nosologies/{id}")
    public ResponseEntity<NosologyDTO> updateNosology(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NosologyDTO nosologyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Nosology : {}, {}", id, nosologyDTO);
        if (nosologyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nosologyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nosologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NosologyDTO result = nosologyService.save(nosologyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nosologyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nosologies/:id} : Partial updates given fields of an existing nosology, field will ignore if it is null
     *
     * @param id the id of the nosologyDTO to save.
     * @param nosologyDTO the nosologyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nosologyDTO,
     * or with status {@code 400 (Bad Request)} if the nosologyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nosologyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nosologyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nosologies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<NosologyDTO> partialUpdateNosology(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NosologyDTO nosologyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Nosology partially : {}, {}", id, nosologyDTO);
        if (nosologyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nosologyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nosologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NosologyDTO> result = nosologyService.partialUpdate(nosologyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nosologyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nosologies} : get all the nosologies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nosologies in body.
     */
    @GetMapping("/nosologies")
    public List<NosologyDTO> getAllNosologies() {
        log.debug("REST request to get all Nosologies");
        return nosologyService.findAll();
    }

    /**
     * {@code GET  /nosologies/:id} : get the "id" nosology.
     *
     * @param id the id of the nosologyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nosologyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nosologies/{id}")
    public ResponseEntity<NosologyDTO> getNosology(@PathVariable Long id) {
        log.debug("REST request to get Nosology : {}", id);
        Optional<NosologyDTO> nosologyDTO = nosologyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nosologyDTO);
    }

    /**
     * {@code DELETE  /nosologies/:id} : delete the "id" nosology.
     *
     * @param id the id of the nosologyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nosologies/{id}")
    public ResponseEntity<Void> deleteNosology(@PathVariable Long id) {
        log.debug("REST request to delete Nosology : {}", id);
        nosologyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
