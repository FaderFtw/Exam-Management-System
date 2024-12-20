package tn.fst.exam_manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.fst.exam_manager.domain.TeachingSessionAsserts.*;
import static tn.fst.exam_manager.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import tn.fst.exam_manager.domain.TeachingSession;
import tn.fst.exam_manager.repository.TeachingSessionRepository;
import tn.fst.exam_manager.service.dto.TeachingSessionDTO;
import tn.fst.exam_manager.service.mapper.TeachingSessionMapper;

/**
 * Integration tests for the {@link TeachingSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TeachingSessionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_HOUR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_HOUR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_HOUR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_HOUR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teaching-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TeachingSessionRepository teachingSessionRepository;

    @Autowired
    private TeachingSessionMapper teachingSessionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeachingSessionMockMvc;

    private TeachingSession teachingSession;

    private TeachingSession insertedTeachingSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeachingSession createEntity() {
        return new TeachingSession().name(DEFAULT_NAME).startHour(DEFAULT_START_HOUR).endHour(DEFAULT_END_HOUR).type(DEFAULT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeachingSession createUpdatedEntity() {
        return new TeachingSession().name(UPDATED_NAME).startHour(UPDATED_START_HOUR).endHour(UPDATED_END_HOUR).type(UPDATED_TYPE);
    }

    @BeforeEach
    public void initTest() {
        teachingSession = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTeachingSession != null) {
            teachingSessionRepository.delete(insertedTeachingSession);
            insertedTeachingSession = null;
        }
    }

    @Test
    @Transactional
    void createTeachingSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TeachingSession
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);
        var returnedTeachingSessionDTO = om.readValue(
            restTeachingSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachingSessionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TeachingSessionDTO.class
        );

        // Validate the TeachingSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTeachingSession = teachingSessionMapper.toEntity(returnedTeachingSessionDTO);
        assertTeachingSessionUpdatableFieldsEquals(returnedTeachingSession, getPersistedTeachingSession(returnedTeachingSession));

        insertedTeachingSession = returnedTeachingSession;
    }

    @Test
    @Transactional
    void createTeachingSessionWithExistingId() throws Exception {
        // Create the TeachingSession with an existing ID
        teachingSession.setId(1L);
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeachingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachingSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TeachingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teachingSession.setName(null);

        // Create the TeachingSession, which fails.
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        restTeachingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachingSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartHourIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teachingSession.setStartHour(null);

        // Create the TeachingSession, which fails.
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        restTeachingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachingSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndHourIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teachingSession.setEndHour(null);

        // Create the TeachingSession, which fails.
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        restTeachingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachingSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teachingSession.setType(null);

        // Create the TeachingSession, which fails.
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        restTeachingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachingSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTeachingSessions() throws Exception {
        // Initialize the database
        insertedTeachingSession = teachingSessionRepository.saveAndFlush(teachingSession);

        // Get all the teachingSessionList
        restTeachingSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teachingSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR.toString())))
            .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getTeachingSession() throws Exception {
        // Initialize the database
        insertedTeachingSession = teachingSessionRepository.saveAndFlush(teachingSession);

        // Get the teachingSession
        restTeachingSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, teachingSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teachingSession.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startHour").value(DEFAULT_START_HOUR.toString()))
            .andExpect(jsonPath("$.endHour").value(DEFAULT_END_HOUR.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingTeachingSession() throws Exception {
        // Get the teachingSession
        restTeachingSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTeachingSession() throws Exception {
        // Initialize the database
        insertedTeachingSession = teachingSessionRepository.saveAndFlush(teachingSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teachingSession
        TeachingSession updatedTeachingSession = teachingSessionRepository.findById(teachingSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTeachingSession are not directly saved in db
        em.detach(updatedTeachingSession);
        updatedTeachingSession.name(UPDATED_NAME).startHour(UPDATED_START_HOUR).endHour(UPDATED_END_HOUR).type(UPDATED_TYPE);
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(updatedTeachingSession);

        restTeachingSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teachingSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachingSessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TeachingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTeachingSessionToMatchAllProperties(updatedTeachingSession);
    }

    @Test
    @Transactional
    void putNonExistingTeachingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingSession.setId(longCount.incrementAndGet());

        // Create the TeachingSession
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeachingSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teachingSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachingSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeachingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingSession.setId(longCount.incrementAndGet());

        // Create the TeachingSession
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachingSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachingSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeachingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingSession.setId(longCount.incrementAndGet());

        // Create the TeachingSession
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachingSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachingSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeachingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeachingSessionWithPatch() throws Exception {
        // Initialize the database
        insertedTeachingSession = teachingSessionRepository.saveAndFlush(teachingSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teachingSession using partial update
        TeachingSession partialUpdatedTeachingSession = new TeachingSession();
        partialUpdatedTeachingSession.setId(teachingSession.getId());

        partialUpdatedTeachingSession.name(UPDATED_NAME).endHour(UPDATED_END_HOUR);

        restTeachingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeachingSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeachingSession))
            )
            .andExpect(status().isOk());

        // Validate the TeachingSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeachingSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTeachingSession, teachingSession),
            getPersistedTeachingSession(teachingSession)
        );
    }

    @Test
    @Transactional
    void fullUpdateTeachingSessionWithPatch() throws Exception {
        // Initialize the database
        insertedTeachingSession = teachingSessionRepository.saveAndFlush(teachingSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teachingSession using partial update
        TeachingSession partialUpdatedTeachingSession = new TeachingSession();
        partialUpdatedTeachingSession.setId(teachingSession.getId());

        partialUpdatedTeachingSession.name(UPDATED_NAME).startHour(UPDATED_START_HOUR).endHour(UPDATED_END_HOUR).type(UPDATED_TYPE);

        restTeachingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeachingSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeachingSession))
            )
            .andExpect(status().isOk());

        // Validate the TeachingSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeachingSessionUpdatableFieldsEquals(
            partialUpdatedTeachingSession,
            getPersistedTeachingSession(partialUpdatedTeachingSession)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTeachingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingSession.setId(longCount.incrementAndGet());

        // Create the TeachingSession
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeachingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teachingSessionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teachingSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeachingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingSession.setId(longCount.incrementAndGet());

        // Create the TeachingSession
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teachingSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeachingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingSession.setId(longCount.incrementAndGet());

        // Create the TeachingSession
        TeachingSessionDTO teachingSessionDTO = teachingSessionMapper.toDto(teachingSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachingSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(teachingSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeachingSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeachingSession() throws Exception {
        // Initialize the database
        insertedTeachingSession = teachingSessionRepository.saveAndFlush(teachingSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the teachingSession
        restTeachingSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, teachingSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return teachingSessionRepository.count();
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

    protected TeachingSession getPersistedTeachingSession(TeachingSession teachingSession) {
        return teachingSessionRepository.findById(teachingSession.getId()).orElseThrow();
    }

    protected void assertPersistedTeachingSessionToMatchAllProperties(TeachingSession expectedTeachingSession) {
        assertTeachingSessionAllPropertiesEquals(expectedTeachingSession, getPersistedTeachingSession(expectedTeachingSession));
    }

    protected void assertPersistedTeachingSessionToMatchUpdatableProperties(TeachingSession expectedTeachingSession) {
        assertTeachingSessionAllUpdatablePropertiesEquals(expectedTeachingSession, getPersistedTeachingSession(expectedTeachingSession));
    }
}
