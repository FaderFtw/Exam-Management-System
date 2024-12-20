package tn.fst.exam_manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.fst.exam_manager.domain.MajorAsserts.*;
import static tn.fst.exam_manager.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.IntegrationTest;
import tn.fst.exam_manager.domain.Major;
import tn.fst.exam_manager.repository.MajorRepository;
import tn.fst.exam_manager.service.dto.MajorDTO;
import tn.fst.exam_manager.service.mapper.MajorMapper;

/**
 * Integration tests for the {@link MajorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MajorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/majors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMajorMockMvc;

    private Major major;

    private Major insertedMajor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Major createEntity() {
        return new Major().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Major createUpdatedEntity() {
        return new Major().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        major = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMajor != null) {
            majorRepository.delete(insertedMajor);
            insertedMajor = null;
        }
    }

    @Test
    @Transactional
    void createMajor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Major
        MajorDTO majorDTO = majorMapper.toDto(major);
        var returnedMajorDTO = om.readValue(
            restMajorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(majorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MajorDTO.class
        );

        // Validate the Major in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMajor = majorMapper.toEntity(returnedMajorDTO);
        assertMajorUpdatableFieldsEquals(returnedMajor, getPersistedMajor(returnedMajor));

        insertedMajor = returnedMajor;
    }

    @Test
    @Transactional
    void createMajorWithExistingId() throws Exception {
        // Create the Major with an existing ID
        major.setId(1L);
        MajorDTO majorDTO = majorMapper.toDto(major);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMajorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(majorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Major in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        major.setName(null);

        // Create the Major, which fails.
        MajorDTO majorDTO = majorMapper.toDto(major);

        restMajorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(majorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMajors() throws Exception {
        // Initialize the database
        insertedMajor = majorRepository.saveAndFlush(major);

        // Get all the majorList
        restMajorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(major.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getMajor() throws Exception {
        // Initialize the database
        insertedMajor = majorRepository.saveAndFlush(major);

        // Get the major
        restMajorMockMvc
            .perform(get(ENTITY_API_URL_ID, major.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(major.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMajor() throws Exception {
        // Get the major
        restMajorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMajor() throws Exception {
        // Initialize the database
        insertedMajor = majorRepository.saveAndFlush(major);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the major
        Major updatedMajor = majorRepository.findById(major.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMajor are not directly saved in db
        em.detach(updatedMajor);
        updatedMajor.name(UPDATED_NAME);
        MajorDTO majorDTO = majorMapper.toDto(updatedMajor);

        restMajorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, majorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(majorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Major in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMajorToMatchAllProperties(updatedMajor);
    }

    @Test
    @Transactional
    void putNonExistingMajor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        major.setId(longCount.incrementAndGet());

        // Create the Major
        MajorDTO majorDTO = majorMapper.toDto(major);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMajorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, majorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(majorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Major in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMajor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        major.setId(longCount.incrementAndGet());

        // Create the Major
        MajorDTO majorDTO = majorMapper.toDto(major);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMajorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(majorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Major in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMajor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        major.setId(longCount.incrementAndGet());

        // Create the Major
        MajorDTO majorDTO = majorMapper.toDto(major);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMajorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(majorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Major in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMajorWithPatch() throws Exception {
        // Initialize the database
        insertedMajor = majorRepository.saveAndFlush(major);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the major using partial update
        Major partialUpdatedMajor = new Major();
        partialUpdatedMajor.setId(major.getId());

        partialUpdatedMajor.name(UPDATED_NAME);

        restMajorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMajor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMajor))
            )
            .andExpect(status().isOk());

        // Validate the Major in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMajorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMajor, major), getPersistedMajor(major));
    }

    @Test
    @Transactional
    void fullUpdateMajorWithPatch() throws Exception {
        // Initialize the database
        insertedMajor = majorRepository.saveAndFlush(major);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the major using partial update
        Major partialUpdatedMajor = new Major();
        partialUpdatedMajor.setId(major.getId());

        partialUpdatedMajor.name(UPDATED_NAME);

        restMajorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMajor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMajor))
            )
            .andExpect(status().isOk());

        // Validate the Major in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMajorUpdatableFieldsEquals(partialUpdatedMajor, getPersistedMajor(partialUpdatedMajor));
    }

    @Test
    @Transactional
    void patchNonExistingMajor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        major.setId(longCount.incrementAndGet());

        // Create the Major
        MajorDTO majorDTO = majorMapper.toDto(major);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMajorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, majorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(majorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Major in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMajor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        major.setId(longCount.incrementAndGet());

        // Create the Major
        MajorDTO majorDTO = majorMapper.toDto(major);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMajorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(majorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Major in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMajor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        major.setId(longCount.incrementAndGet());

        // Create the Major
        MajorDTO majorDTO = majorMapper.toDto(major);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMajorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(majorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Major in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMajor() throws Exception {
        // Initialize the database
        insertedMajor = majorRepository.saveAndFlush(major);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the major
        restMajorMockMvc
            .perform(delete(ENTITY_API_URL_ID, major.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return majorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Major getPersistedMajor(Major major) {
        return majorRepository.findById(major.getId()).orElseThrow();
    }

    protected void assertPersistedMajorToMatchAllProperties(Major expectedMajor) {
        assertMajorAllPropertiesEquals(expectedMajor, getPersistedMajor(expectedMajor));
    }

    protected void assertPersistedMajorToMatchUpdatableProperties(Major expectedMajor) {
        assertMajorAllUpdatablePropertiesEquals(expectedMajor, getPersistedMajor(expectedMajor));
    }
}
