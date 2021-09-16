package com.srgcro.feasbase.web.rest;

import com.srgcro.feasbase.IntegrationTest;
import com.srgcro.feasbase.domain.Nosology;
import com.srgcro.feasbase.repository.NosologyRepository;
import com.srgcro.feasbase.service.dto.NosologyDTO;
import com.srgcro.feasbase.service.mapper.NosologyMapper;
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
 * Integration tests for the {@link NosologyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NosologyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nosologies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NosologyRepository nosologyRepository;

    @Autowired
    private NosologyMapper nosologyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNosologyMockMvc;

    private Nosology nosology;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nosology createEntity(EntityManager em) {
        Nosology nosology = new Nosology().name(DEFAULT_NAME);
        return nosology;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nosology createUpdatedEntity(EntityManager em) {
        Nosology nosology = new Nosology().name(UPDATED_NAME);
        return nosology;
    }

    @BeforeEach
    public void initTest() {
        nosology = createEntity(em);
    }

    @Test
    @Transactional
    void createNosology() throws Exception {
        int databaseSizeBeforeCreate = nosologyRepository.findAll().size();
        // Create the Nosology
        NosologyDTO nosologyDTO = nosologyMapper.toDto(nosology);
        restNosologyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nosologyDTO)))
            .andExpect(status().isCreated());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeCreate + 1);
        Nosology testNosology = nosologyList.get(nosologyList.size() - 1);
        assertThat(testNosology.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createNosologyWithExistingId() throws Exception {
        // Create the Nosology with an existing ID
        nosology.setId(1L);
        NosologyDTO nosologyDTO = nosologyMapper.toDto(nosology);

        int databaseSizeBeforeCreate = nosologyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNosologyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nosologyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nosologyRepository.findAll().size();
        // set the field null
        nosology.setName(null);

        // Create the Nosology, which fails.
        NosologyDTO nosologyDTO = nosologyMapper.toDto(nosology);

        restNosologyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nosologyDTO)))
            .andExpect(status().isBadRequest());

        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNosologies() throws Exception {
        // Initialize the database
        nosologyRepository.saveAndFlush(nosology);

        // Get all the nosologyList
        restNosologyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nosology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getNosology() throws Exception {
        // Initialize the database
        nosologyRepository.saveAndFlush(nosology);

        // Get the nosology
        restNosologyMockMvc
            .perform(get(ENTITY_API_URL_ID, nosology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nosology.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingNosology() throws Exception {
        // Get the nosology
        restNosologyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNosology() throws Exception {
        // Initialize the database
        nosologyRepository.saveAndFlush(nosology);

        int databaseSizeBeforeUpdate = nosologyRepository.findAll().size();

        // Update the nosology
        Nosology updatedNosology = nosologyRepository.findById(nosology.getId()).get();
        // Disconnect from session so that the updates on updatedNosology are not directly saved in db
        em.detach(updatedNosology);
        updatedNosology.name(UPDATED_NAME);
        NosologyDTO nosologyDTO = nosologyMapper.toDto(updatedNosology);

        restNosologyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nosologyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nosologyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeUpdate);
        Nosology testNosology = nosologyList.get(nosologyList.size() - 1);
        assertThat(testNosology.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingNosology() throws Exception {
        int databaseSizeBeforeUpdate = nosologyRepository.findAll().size();
        nosology.setId(count.incrementAndGet());

        // Create the Nosology
        NosologyDTO nosologyDTO = nosologyMapper.toDto(nosology);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNosologyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nosologyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nosologyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNosology() throws Exception {
        int databaseSizeBeforeUpdate = nosologyRepository.findAll().size();
        nosology.setId(count.incrementAndGet());

        // Create the Nosology
        NosologyDTO nosologyDTO = nosologyMapper.toDto(nosology);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNosologyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nosologyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNosology() throws Exception {
        int databaseSizeBeforeUpdate = nosologyRepository.findAll().size();
        nosology.setId(count.incrementAndGet());

        // Create the Nosology
        NosologyDTO nosologyDTO = nosologyMapper.toDto(nosology);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNosologyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nosologyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNosologyWithPatch() throws Exception {
        // Initialize the database
        nosologyRepository.saveAndFlush(nosology);

        int databaseSizeBeforeUpdate = nosologyRepository.findAll().size();

        // Update the nosology using partial update
        Nosology partialUpdatedNosology = new Nosology();
        partialUpdatedNosology.setId(nosology.getId());

        restNosologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNosology.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNosology))
            )
            .andExpect(status().isOk());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeUpdate);
        Nosology testNosology = nosologyList.get(nosologyList.size() - 1);
        assertThat(testNosology.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateNosologyWithPatch() throws Exception {
        // Initialize the database
        nosologyRepository.saveAndFlush(nosology);

        int databaseSizeBeforeUpdate = nosologyRepository.findAll().size();

        // Update the nosology using partial update
        Nosology partialUpdatedNosology = new Nosology();
        partialUpdatedNosology.setId(nosology.getId());

        partialUpdatedNosology.name(UPDATED_NAME);

        restNosologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNosology.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNosology))
            )
            .andExpect(status().isOk());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeUpdate);
        Nosology testNosology = nosologyList.get(nosologyList.size() - 1);
        assertThat(testNosology.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingNosology() throws Exception {
        int databaseSizeBeforeUpdate = nosologyRepository.findAll().size();
        nosology.setId(count.incrementAndGet());

        // Create the Nosology
        NosologyDTO nosologyDTO = nosologyMapper.toDto(nosology);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNosologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nosologyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nosologyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNosology() throws Exception {
        int databaseSizeBeforeUpdate = nosologyRepository.findAll().size();
        nosology.setId(count.incrementAndGet());

        // Create the Nosology
        NosologyDTO nosologyDTO = nosologyMapper.toDto(nosology);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNosologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nosologyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNosology() throws Exception {
        int databaseSizeBeforeUpdate = nosologyRepository.findAll().size();
        nosology.setId(count.incrementAndGet());

        // Create the Nosology
        NosologyDTO nosologyDTO = nosologyMapper.toDto(nosology);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNosologyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nosologyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nosology in the database
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNosology() throws Exception {
        // Initialize the database
        nosologyRepository.saveAndFlush(nosology);

        int databaseSizeBeforeDelete = nosologyRepository.findAll().size();

        // Delete the nosology
        restNosologyMockMvc
            .perform(delete(ENTITY_API_URL_ID, nosology.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nosology> nosologyList = nosologyRepository.findAll();
        assertThat(nosologyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
