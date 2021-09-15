package com.srgcro.feasbase.service;

import com.srgcro.feasbase.domain.Specialization;
import com.srgcro.feasbase.repository.SpecializationRepository;
import com.srgcro.feasbase.service.dto.SpecializationDTO;
import com.srgcro.feasbase.service.mapper.SpecializationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Specialization}.
 */
@Service
@Transactional
public class SpecializationService {

    private final Logger log = LoggerFactory.getLogger(SpecializationService.class);

    private final SpecializationRepository specializationRepository;

    private final SpecializationMapper specializationMapper;

    public SpecializationService(SpecializationRepository specializationRepository, SpecializationMapper specializationMapper) {
        this.specializationRepository = specializationRepository;
        this.specializationMapper = specializationMapper;
    }

    /**
     * Save a specialization.
     *
     * @param specializationDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecializationDTO save(SpecializationDTO specializationDTO) {
        log.debug("Request to save Specialization : {}", specializationDTO);
        Specialization specialization = specializationMapper.toEntity(specializationDTO);
        specialization = specializationRepository.save(specialization);
        return specializationMapper.toDto(specialization);
    }

    /**
     * Partially update a specialization.
     *
     * @param specializationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SpecializationDTO> partialUpdate(SpecializationDTO specializationDTO) {
        log.debug("Request to partially update Specialization : {}", specializationDTO);

        return specializationRepository
            .findById(specializationDTO.getId())
            .map(
                existingSpecialization -> {
                    specializationMapper.partialUpdate(existingSpecialization, specializationDTO);

                    return existingSpecialization;
                }
            )
            .map(specializationRepository::save)
            .map(specializationMapper::toDto);
    }

    /**
     * Get all the specializations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SpecializationDTO> findAll() {
        log.debug("Request to get all Specializations");
        return specializationRepository
            .findAll()
            .stream()
            .map(specializationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one specialization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpecializationDTO> findOne(Long id) {
        log.debug("Request to get Specialization : {}", id);
        return specializationRepository.findById(id).map(specializationMapper::toDto);
    }

    /**
     * Delete the specialization by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Specialization : {}", id);
        specializationRepository.deleteById(id);
    }
}
