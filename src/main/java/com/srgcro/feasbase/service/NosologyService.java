package com.srgcro.feasbase.service;

import com.srgcro.feasbase.domain.Nosology;
import com.srgcro.feasbase.repository.NosologyRepository;
import com.srgcro.feasbase.service.dto.NosologyDTO;
import com.srgcro.feasbase.service.mapper.NosologyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Nosology}.
 */
@Service
@Transactional
public class NosologyService {

    private final Logger log = LoggerFactory.getLogger(NosologyService.class);

    private final NosologyRepository nosologyRepository;

    private final NosologyMapper nosologyMapper;

    public NosologyService(NosologyRepository nosologyRepository, NosologyMapper nosologyMapper) {
        this.nosologyRepository = nosologyRepository;
        this.nosologyMapper = nosologyMapper;
    }

    /**
     * Save a nosology.
     *
     * @param nosologyDTO the entity to save.
     * @return the persisted entity.
     */
    public NosologyDTO save(NosologyDTO nosologyDTO) {
        log.debug("Request to save Nosology : {}", nosologyDTO);
        Nosology nosology = nosologyMapper.toEntity(nosologyDTO);
        nosology = nosologyRepository.save(nosology);
        return nosologyMapper.toDto(nosology);
    }

    /**
     * Partially update a nosology.
     *
     * @param nosologyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NosologyDTO> partialUpdate(NosologyDTO nosologyDTO) {
        log.debug("Request to partially update Nosology : {}", nosologyDTO);

        return nosologyRepository
            .findById(nosologyDTO.getId())
            .map(
                existingNosology -> {
                    nosologyMapper.partialUpdate(existingNosology, nosologyDTO);

                    return existingNosology;
                }
            )
            .map(nosologyRepository::save)
            .map(nosologyMapper::toDto);
    }

    /**
     * Get all the nosologies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NosologyDTO> findAll() {
        log.debug("Request to get all Nosologies");
        return nosologyRepository.findAll().stream().map(nosologyMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one nosology by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NosologyDTO> findOne(Long id) {
        log.debug("Request to get Nosology : {}", id);
        return nosologyRepository.findById(id).map(nosologyMapper::toDto);
    }

    /**
     * Delete the nosology by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Nosology : {}", id);
        nosologyRepository.deleteById(id);
    }
}
