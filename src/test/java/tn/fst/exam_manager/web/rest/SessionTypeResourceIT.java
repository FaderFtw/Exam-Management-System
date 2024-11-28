package tn.fst.exam_manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.fst.exam_manager.domain.SessionTypeAsserts.*;
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
import tn.fst.exam_manager.domain.SessionType;
import tn.fst.exam_manager.repository.SessionTypeRepository;
import tn.fst.exam_manager.service.dto.SessionTypeDTO;
import tn.fst.exam_manager.service.mapper.SessionTypeMapper;

/**
 * Integration tests for the {@link SessionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/session-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SessionTypeRepository sessionTypeRepository;

    @Autowired
    private SessionTypeMapper sessionTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionTypeMockMvc;

    private SessionType sessionType;

    private SessionType insertedSessionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionType createEntity() {
        return new SessionType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionType createUpdatedEntity() {
        return new SessionType().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        sessionType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSessionType != null) {
            sessionTypeRepository.delete(insertedSessionType);
            insertedSessionType = null;
        }
    }

    @Test
    @Transactional
    void createSessionType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SessionType
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(sessionType);
        var returnedSessionTypeDTO = om.readValue(
            restSessionTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SessionTypeDTO.class
        );

        // Validate the SessionType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSessionType = sessionTypeMapper.toEntity(returnedSessionTypeDTO);
        assertSessionTypeUpdatableFieldsEquals(returnedSessionType, getPersistedSessionType(returnedSessionType));

        insertedSessionType = returnedSessionType;
    }

    @Test
    @Transactional
    void createSessionTypeWithExistingId() throws Exception {
        // Create the SessionType with an existing ID
        sessionType.setId(1L);
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(sessionType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SessionType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sessionType.setName(null);

        // Create the SessionType, which fails.
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(sessionType);

        restSessionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSessionTypes() throws Exception {
        // Initialize the database
        insertedSessionType = sessionTypeRepository.saveAndFlush(sessionType);

        // Get all the sessionTypeList
        restSessionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSessionType() throws Exception {
        // Initialize the database
        insertedSessionType = sessionTypeRepository.saveAndFlush(sessionType);

        // Get the sessionType
        restSessionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSessionType() throws Exception {
        // Get the sessionType
        restSessionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSessionType() throws Exception {
        // Initialize the database
        insertedSessionType = sessionTypeRepository.saveAndFlush(sessionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sessionType
        SessionType updatedSessionType = sessionTypeRepository.findById(sessionType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSessionType are not directly saved in db
        em.detach(updatedSessionType);
        updatedSessionType.name(UPDATED_NAME);
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(updatedSessionType);

        restSessionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sessionTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SessionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSessionTypeToMatchAllProperties(updatedSessionType);
    }

    @Test
    @Transactional
    void putNonExistingSessionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionType.setId(longCount.incrementAndGet());

        // Create the SessionType
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(sessionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sessionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionType.setId(longCount.incrementAndGet());

        // Create the SessionType
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(sessionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sessionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionType.setId(longCount.incrementAndGet());

        // Create the SessionType
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(sessionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedSessionType = sessionTypeRepository.saveAndFlush(sessionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sessionType using partial update
        SessionType partialUpdatedSessionType = new SessionType();
        partialUpdatedSessionType.setId(sessionType.getId());

        restSessionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSessionType))
            )
            .andExpect(status().isOk());

        // Validate the SessionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSessionTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSessionType, sessionType),
            getPersistedSessionType(sessionType)
        );
    }

    @Test
    @Transactional
    void fullUpdateSessionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedSessionType = sessionTypeRepository.saveAndFlush(sessionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sessionType using partial update
        SessionType partialUpdatedSessionType = new SessionType();
        partialUpdatedSessionType.setId(sessionType.getId());

        partialUpdatedSessionType.name(UPDATED_NAME);

        restSessionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSessionType))
            )
            .andExpect(status().isOk());

        // Validate the SessionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSessionTypeUpdatableFieldsEquals(partialUpdatedSessionType, getPersistedSessionType(partialUpdatedSessionType));
    }

    @Test
    @Transactional
    void patchNonExistingSessionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionType.setId(longCount.incrementAndGet());

        // Create the SessionType
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(sessionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sessionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionType.setId(longCount.incrementAndGet());

        // Create the SessionType
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(sessionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sessionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionType.setId(longCount.incrementAndGet());

        // Create the SessionType
        SessionTypeDTO sessionTypeDTO = sessionTypeMapper.toDto(sessionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sessionTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionType() throws Exception {
        // Initialize the database
        insertedSessionType = sessionTypeRepository.saveAndFlush(sessionType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sessionType
        restSessionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sessionTypeRepository.count();
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

    protected SessionType getPersistedSessionType(SessionType sessionType) {
        return sessionTypeRepository.findById(sessionType.getId()).orElseThrow();
    }

    protected void assertPersistedSessionTypeToMatchAllProperties(SessionType expectedSessionType) {
        assertSessionTypeAllPropertiesEquals(expectedSessionType, getPersistedSessionType(expectedSessionType));
    }

    protected void assertPersistedSessionTypeToMatchUpdatableProperties(SessionType expectedSessionType) {
        assertSessionTypeAllUpdatablePropertiesEquals(expectedSessionType, getPersistedSessionType(expectedSessionType));
    }
}
