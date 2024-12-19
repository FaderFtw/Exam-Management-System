package tn.fst.exam_manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.fst.exam_manager.domain.ProfessorDetailsAsserts.*;
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
import tn.fst.exam_manager.domain.ProfessorDetails;
import tn.fst.exam_manager.domain.enumeration.Rank;
import tn.fst.exam_manager.repository.ProfessorDetailsRepository;
import tn.fst.exam_manager.repository.UserRepository;
import tn.fst.exam_manager.service.dto.ProfessorDetailsDTO;
import tn.fst.exam_manager.service.mapper.ProfessorDetailsMapper;

/**
 * Integration tests for the {@link ProfessorDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfessorDetailsResourceIT {

    private static final Rank DEFAULT_RANK = Rank.MAITRE_ASSISTANT;
    private static final Rank UPDATED_RANK = Rank.ASSISTANT;

    private static final String ENTITY_API_URL = "/api/professor-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessorDetailsRepository professorDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessorDetailsMapper professorDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessorDetailsMockMvc;

    private ProfessorDetails professorDetails;

    private ProfessorDetails insertedProfessorDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessorDetails createEntity() {
        return new ProfessorDetails().rank(DEFAULT_RANK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessorDetails createUpdatedEntity() {
        return new ProfessorDetails().rank(UPDATED_RANK);
    }

    @BeforeEach
    public void initTest() {
        professorDetails = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProfessorDetails != null) {
            professorDetailsRepository.delete(insertedProfessorDetails);
            insertedProfessorDetails = null;
        }
    }

    @Test
    @Transactional
    void createProfessorDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessorDetails
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(professorDetails);
        var returnedProfessorDetailsDTO = om.readValue(
            restProfessorDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professorDetailsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessorDetailsDTO.class
        );

        // Validate the ProfessorDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessorDetails = professorDetailsMapper.toEntity(returnedProfessorDetailsDTO);
        assertProfessorDetailsUpdatableFieldsEquals(returnedProfessorDetails, getPersistedProfessorDetails(returnedProfessorDetails));

        insertedProfessorDetails = returnedProfessorDetails;
    }

    @Test
    @Transactional
    void createProfessorDetailsWithExistingId() throws Exception {
        // Create the ProfessorDetails with an existing ID
        professorDetails.setId(1L);
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(professorDetails);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessorDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professorDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessorDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRankIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professorDetails.setRank(null);

        // Create the ProfessorDetails, which fails.
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(professorDetails);

        restProfessorDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professorDetailsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessorDetails() throws Exception {
        // Initialize the database
        insertedProfessorDetails = professorDetailsRepository.saveAndFlush(professorDetails);

        // Get all the professorDetailsList
        restProfessorDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professorDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.toString())));
    }

    @Test
    @Transactional
    void getProfessorDetails() throws Exception {
        // Initialize the database
        insertedProfessorDetails = professorDetailsRepository.saveAndFlush(professorDetails);

        // Get the professorDetails
        restProfessorDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, professorDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professorDetails.getId().intValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfessorDetails() throws Exception {
        // Get the professorDetails
        restProfessorDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessorDetails() throws Exception {
        // Initialize the database
        insertedProfessorDetails = professorDetailsRepository.saveAndFlush(professorDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professorDetails
        ProfessorDetails updatedProfessorDetails = professorDetailsRepository.findById(professorDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfessorDetails are not directly saved in db
        em.detach(updatedProfessorDetails);
        updatedProfessorDetails.rank(UPDATED_RANK);
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(updatedProfessorDetails);

        restProfessorDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professorDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professorDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessorDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessorDetailsToMatchAllProperties(updatedProfessorDetails);
    }

    @Test
    @Transactional
    void putNonExistingProfessorDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professorDetails.setId(longCount.incrementAndGet());

        // Create the ProfessorDetails
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(professorDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessorDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professorDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professorDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessorDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessorDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professorDetails.setId(longCount.incrementAndGet());

        // Create the ProfessorDetails
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(professorDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessorDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professorDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessorDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessorDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professorDetails.setId(longCount.incrementAndGet());

        // Create the ProfessorDetails
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(professorDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessorDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professorDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessorDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessorDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedProfessorDetails = professorDetailsRepository.saveAndFlush(professorDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professorDetails using partial update
        ProfessorDetails partialUpdatedProfessorDetails = new ProfessorDetails();
        partialUpdatedProfessorDetails.setId(professorDetails.getId());

        restProfessorDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessorDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessorDetails))
            )
            .andExpect(status().isOk());

        // Validate the ProfessorDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessorDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessorDetails, professorDetails),
            getPersistedProfessorDetails(professorDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessorDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedProfessorDetails = professorDetailsRepository.saveAndFlush(professorDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professorDetails using partial update
        ProfessorDetails partialUpdatedProfessorDetails = new ProfessorDetails();
        partialUpdatedProfessorDetails.setId(professorDetails.getId());

        partialUpdatedProfessorDetails.rank(UPDATED_RANK);

        restProfessorDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessorDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessorDetails))
            )
            .andExpect(status().isOk());

        // Validate the ProfessorDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessorDetailsUpdatableFieldsEquals(
            partialUpdatedProfessorDetails,
            getPersistedProfessorDetails(partialUpdatedProfessorDetails)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessorDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professorDetails.setId(longCount.incrementAndGet());

        // Create the ProfessorDetails
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(professorDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessorDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professorDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professorDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessorDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessorDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professorDetails.setId(longCount.incrementAndGet());

        // Create the ProfessorDetails
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(professorDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessorDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professorDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessorDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessorDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professorDetails.setId(longCount.incrementAndGet());

        // Create the ProfessorDetails
        ProfessorDetailsDTO professorDetailsDTO = professorDetailsMapper.toDto(professorDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessorDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professorDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessorDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessorDetails() throws Exception {
        // Initialize the database
        insertedProfessorDetails = professorDetailsRepository.saveAndFlush(professorDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professorDetails
        restProfessorDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, professorDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professorDetailsRepository.count();
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

    protected ProfessorDetails getPersistedProfessorDetails(ProfessorDetails professorDetails) {
        return professorDetailsRepository.findById(professorDetails.getId()).orElseThrow();
    }

    protected void assertPersistedProfessorDetailsToMatchAllProperties(ProfessorDetails expectedProfessorDetails) {
        assertProfessorDetailsAllPropertiesEquals(expectedProfessorDetails, getPersistedProfessorDetails(expectedProfessorDetails));
    }

    protected void assertPersistedProfessorDetailsToMatchUpdatableProperties(ProfessorDetails expectedProfessorDetails) {
        assertProfessorDetailsAllUpdatablePropertiesEquals(
            expectedProfessorDetails,
            getPersistedProfessorDetails(expectedProfessorDetails)
        );
    }
}
