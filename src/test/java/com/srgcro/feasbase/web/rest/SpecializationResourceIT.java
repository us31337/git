package com.srgcro.feasbase.web.rest;

import com.srgcro.feasbase.IntegrationTest;
import com.srgcro.feasbase.domain.Specialization;
import com.srgcro.feasbase.repository.SpecializationRepository;
import com.srgcro.feasbase.service.dto.SpecializationDTO;
import com.srgcro.feasbase.service.mapper.SpecializationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link SpecializationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecializationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specializations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private SpecializationMapper specializationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecializationMockMvc;

    private Specialization specialization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialization createEntity(EntityManager em) {
        Specialization specialization = new Specialization().name(DEFAULT_NAME);
        return specialization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialization createUpdatedEntity(EntityManager em) {
        Specialization specialization = new Specialization().name(UPDATED_NAME);
        return specialization;
    }

    @BeforeEach
    public void initTest() {
        specialization = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialization() throws Exception {
        int databaseSizeBeforeCreate = specializationRepository.findAll().size();
        // Create the Specialization
        SpecializationDTO specializationDTO = specializationMapper.toDto(specialization);
        restSpecializationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeCreate + 1);
        Specialization testSpecialization = specializationList.get(specializationList.size() - 1);
        assertThat(testSpecialization.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSpecializationWithExistingId() throws Exception {
        // Create the Specialization with an existing ID
        specialization.setId(1L);
        SpecializationDTO specializationDTO = specializationMapper.toDto(specialization);

        int databaseSizeBeforeCreate = specializationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecializationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specializationRepository.findAll().size();
        // set the field null
        specialization.setName(null);

        // Create the Specialization, which fails.
        SpecializationDTO specializationDTO = specializationMapper.toDto(specialization);

        restSpecializationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecializations() throws Exception {
        // Initialize the database
        specializationRepository.saveAndFlush(specialization);

        // Get all the specializationList
        restSpecializationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSpecialization() throws Exception {
        // Initialize the database
        specializationRepository.saveAndFlush(specialization);

        // Get the specialization
        restSpecializationMockMvc
            .perform(get(ENTITY_API_URL_ID, specialization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSpecialization() throws Exception {
        // Get the specialization
        restSpecializationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpecialization() throws Exception {
        // Initialize the database
        specializationRepository.saveAndFlush(specialization);

        int databaseSizeBeforeUpdate = specializationRepository.findAll().size();

        // Update the specialization
        Specialization updatedSpecialization = specializationRepository.findById(specialization.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialization are not directly saved in db
        em.detach(updatedSpecialization);
        updatedSpecialization.name(UPDATED_NAME);
        SpecializationDTO specializationDTO = specializationMapper.toDto(updatedSpecialization);

        restSpecializationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specializationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeUpdate);
        Specialization testSpecialization = specializationList.get(specializationList.size() - 1);
        assertThat(testSpecialization.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSpecialization() throws Exception {
        int databaseSizeBeforeUpdate = specializationRepository.findAll().size();
        specialization.setId(count.incrementAndGet());

        // Create the Specialization
        SpecializationDTO specializationDTO = specializationMapper.toDto(specialization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecializationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specializationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialization() throws Exception {
        int databaseSizeBeforeUpdate = specializationRepository.findAll().size();
        specialization.setId(count.incrementAndGet());

        // Create the Specialization
        SpecializationDTO specializationDTO = specializationMapper.toDto(specialization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecializationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialization() throws Exception {
        int databaseSizeBeforeUpdate = specializationRepository.findAll().size();
        specialization.setId(count.incrementAndGet());

        // Create the Specialization
        SpecializationDTO specializationDTO = specializationMapper.toDto(specialization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecializationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecializationWithPatch() throws Exception {
        // Initialize the database
        specializationRepository.saveAndFlush(specialization);

        int databaseSizeBeforeUpdate = specializationRepository.findAll().size();

        // Update the specialization using partial update
        Specialization partialUpdatedSpecialization = new Specialization();
        partialUpdatedSpecialization.setId(specialization.getId());

        partialUpdatedSpecialization.name(UPDATED_NAME);

        restSpecializationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialization))
            )
            .andExpect(status().isOk());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeUpdate);
        Specialization testSpecialization = specializationList.get(specializationList.size() - 1);
        assertThat(testSpecialization.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSpecializationWithPatch() throws Exception {
        // Initialize the database
        specializationRepository.saveAndFlush(specialization);

        int databaseSizeBeforeUpdate = specializationRepository.findAll().size();

        // Update the specialization using partial update
        Specialization partialUpdatedSpecialization = new Specialization();
        partialUpdatedSpecialization.setId(specialization.getId());

        partialUpdatedSpecialization.name(UPDATED_NAME);

        restSpecializationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialization))
            )
            .andExpect(status().isOk());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeUpdate);
        Specialization testSpecialization = specializationList.get(specializationList.size() - 1);
        assertThat(testSpecialization.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialization() throws Exception {
        int databaseSizeBeforeUpdate = specializationRepository.findAll().size();
        specialization.setId(count.incrementAndGet());

        // Create the Specialization
        SpecializationDTO specializationDTO = specializationMapper.toDto(specialization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecializationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specializationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialization() throws Exception {
        int databaseSizeBeforeUpdate = specializationRepository.findAll().size();
        specialization.setId(count.incrementAndGet());

        // Create the Specialization
        SpecializationDTO specializationDTO = specializationMapper.toDto(specialization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecializationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialization() throws Exception {
        int databaseSizeBeforeUpdate = specializationRepository.findAll().size();
        specialization.setId(count.incrementAndGet());

        // Create the Specialization
        SpecializationDTO specializationDTO = specializationMapper.toDto(specialization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecializationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specializationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialization in the database
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialization() throws Exception {
        // Initialize the database
        specializationRepository.saveAndFlush(specialization);

        int databaseSizeBeforeDelete = specializationRepository.findAll().size();

        // Delete the specialization
        restSpecializationMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specialization> specializationList = specializationRepository.findAll();
        assertThat(specializationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
