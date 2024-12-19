package tn.fst.exam_manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.fst.exam_manager.domain.ExamSessionAsserts.*;
import static tn.fst.exam_manager.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.fst.exam_manager.IntegrationTest;
import tn.fst.exam_manager.domain.ExamSession;
import tn.fst.exam_manager.repository.ExamSessionRepository;
import tn.fst.exam_manager.service.ExamSessionService;
import tn.fst.exam_manager.service.dto.ExamSessionDTO;
import tn.fst.exam_manager.service.mapper.ExamSessionMapper;

/**
 * Integration tests for the {@link ExamSessionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExamSessionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SESSION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ALLOW_PARALLEL_STUDIES = false;
    private static final Boolean UPDATED_ALLOW_PARALLEL_STUDIES = true;

    private static final Boolean DEFAULT_ALLOW_OWN_CLASS_SUPERVISION = false;
    private static final Boolean UPDATED_ALLOW_OWN_CLASS_SUPERVISION = true;

    private static final Boolean DEFAULT_ALLOW_COMBINE_CLASSES = false;
    private static final Boolean UPDATED_ALLOW_COMBINE_CLASSES = true;

    private static final String ENTITY_API_URL = "/api/exam-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExamSessionRepository examSessionRepository;

    @Mock
    private ExamSessionRepository examSessionRepositoryMock;

    @Autowired
    private ExamSessionMapper examSessionMapper;

    @Mock
    private ExamSessionService examSessionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamSessionMockMvc;

    private ExamSession examSession;

    private ExamSession insertedExamSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamSession createEntity() {
        return new ExamSession()
            .name(DEFAULT_NAME)
            .sessionCode(DEFAULT_SESSION_CODE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .allowParallelStudies(DEFAULT_ALLOW_PARALLEL_STUDIES)
            .allowOwnClassSupervision(DEFAULT_ALLOW_OWN_CLASS_SUPERVISION)
            .allowCombineClasses(DEFAULT_ALLOW_COMBINE_CLASSES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamSession createUpdatedEntity() {
        return new ExamSession()
            .name(UPDATED_NAME)
            .sessionCode(UPDATED_SESSION_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .allowParallelStudies(UPDATED_ALLOW_PARALLEL_STUDIES)
            .allowOwnClassSupervision(UPDATED_ALLOW_OWN_CLASS_SUPERVISION)
            .allowCombineClasses(UPDATED_ALLOW_COMBINE_CLASSES);
    }

    @BeforeEach
    public void initTest() {
        examSession = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedExamSession != null) {
            examSessionRepository.delete(insertedExamSession);
            insertedExamSession = null;
        }
    }

    @Test
    @Transactional
    void createExamSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExamSession
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);
        var returnedExamSessionDTO = om.readValue(
            restExamSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExamSessionDTO.class
        );

        // Validate the ExamSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExamSession = examSessionMapper.toEntity(returnedExamSessionDTO);
        assertExamSessionUpdatableFieldsEquals(returnedExamSession, getPersistedExamSession(returnedExamSession));

        insertedExamSession = returnedExamSession;
    }

    @Test
    @Transactional
    void createExamSessionWithExistingId() throws Exception {
        // Create the ExamSession with an existing ID
        examSession.setId(1L);
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExamSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examSession.setName(null);

        // Create the ExamSession, which fails.
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        restExamSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSessionCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examSession.setSessionCode(null);

        // Create the ExamSession, which fails.
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        restExamSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examSession.setStartDate(null);

        // Create the ExamSession, which fails.
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        restExamSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examSession.setEndDate(null);

        // Create the ExamSession, which fails.
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        restExamSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllowParallelStudiesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examSession.setAllowParallelStudies(null);

        // Create the ExamSession, which fails.
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        restExamSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllowOwnClassSupervisionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examSession.setAllowOwnClassSupervision(null);

        // Create the ExamSession, which fails.
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        restExamSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllowCombineClassesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examSession.setAllowCombineClasses(null);

        // Create the ExamSession, which fails.
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        restExamSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamSessions() throws Exception {
        // Initialize the database
        insertedExamSession = examSessionRepository.saveAndFlush(examSession);

        // Get all the examSessionList
        restExamSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sessionCode").value(hasItem(DEFAULT_SESSION_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].allowParallelStudies").value(hasItem(DEFAULT_ALLOW_PARALLEL_STUDIES)))
            .andExpect(jsonPath("$.[*].allowOwnClassSupervision").value(hasItem(DEFAULT_ALLOW_OWN_CLASS_SUPERVISION)))
            .andExpect(jsonPath("$.[*].allowCombineClasses").value(hasItem(DEFAULT_ALLOW_COMBINE_CLASSES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExamSessionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(examSessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExamSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(examSessionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExamSessionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(examSessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExamSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(examSessionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getExamSession() throws Exception {
        // Initialize the database
        insertedExamSession = examSessionRepository.saveAndFlush(examSession);

        // Get the examSession
        restExamSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, examSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examSession.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sessionCode").value(DEFAULT_SESSION_CODE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.allowParallelStudies").value(DEFAULT_ALLOW_PARALLEL_STUDIES))
            .andExpect(jsonPath("$.allowOwnClassSupervision").value(DEFAULT_ALLOW_OWN_CLASS_SUPERVISION))
            .andExpect(jsonPath("$.allowCombineClasses").value(DEFAULT_ALLOW_COMBINE_CLASSES));
    }

    @Test
    @Transactional
    void getNonExistingExamSession() throws Exception {
        // Get the examSession
        restExamSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExamSession() throws Exception {
        // Initialize the database
        insertedExamSession = examSessionRepository.saveAndFlush(examSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examSession
        ExamSession updatedExamSession = examSessionRepository.findById(examSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExamSession are not directly saved in db
        em.detach(updatedExamSession);
        updatedExamSession
            .name(UPDATED_NAME)
            .sessionCode(UPDATED_SESSION_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .allowParallelStudies(UPDATED_ALLOW_PARALLEL_STUDIES)
            .allowOwnClassSupervision(UPDATED_ALLOW_OWN_CLASS_SUPERVISION)
            .allowCombineClasses(UPDATED_ALLOW_COMBINE_CLASSES);
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(updatedExamSession);

        restExamSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examSessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExamSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExamSessionToMatchAllProperties(updatedExamSession);
    }

    @Test
    @Transactional
    void putNonExistingExamSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSession.setId(longCount.incrementAndGet());

        // Create the ExamSession
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSession.setId(longCount.incrementAndGet());

        // Create the ExamSession
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSession.setId(longCount.incrementAndGet());

        // Create the ExamSession
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamSessionWithPatch() throws Exception {
        // Initialize the database
        insertedExamSession = examSessionRepository.saveAndFlush(examSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examSession using partial update
        ExamSession partialUpdatedExamSession = new ExamSession();
        partialUpdatedExamSession.setId(examSession.getId());

        partialUpdatedExamSession.sessionCode(UPDATED_SESSION_CODE).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restExamSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExamSession))
            )
            .andExpect(status().isOk());

        // Validate the ExamSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExamSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExamSession, examSession),
            getPersistedExamSession(examSession)
        );
    }

    @Test
    @Transactional
    void fullUpdateExamSessionWithPatch() throws Exception {
        // Initialize the database
        insertedExamSession = examSessionRepository.saveAndFlush(examSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examSession using partial update
        ExamSession partialUpdatedExamSession = new ExamSession();
        partialUpdatedExamSession.setId(examSession.getId());

        partialUpdatedExamSession
            .name(UPDATED_NAME)
            .sessionCode(UPDATED_SESSION_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .allowParallelStudies(UPDATED_ALLOW_PARALLEL_STUDIES)
            .allowOwnClassSupervision(UPDATED_ALLOW_OWN_CLASS_SUPERVISION)
            .allowCombineClasses(UPDATED_ALLOW_COMBINE_CLASSES);

        restExamSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExamSession))
            )
            .andExpect(status().isOk());

        // Validate the ExamSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExamSessionUpdatableFieldsEquals(partialUpdatedExamSession, getPersistedExamSession(partialUpdatedExamSession));
    }

    @Test
    @Transactional
    void patchNonExistingExamSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSession.setId(longCount.incrementAndGet());

        // Create the ExamSession
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examSessionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSession.setId(longCount.incrementAndGet());

        // Create the ExamSession
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examSession.setId(longCount.incrementAndGet());

        // Create the ExamSession
        ExamSessionDTO examSessionDTO = examSessionMapper.toDto(examSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(examSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamSession() throws Exception {
        // Initialize the database
        insertedExamSession = examSessionRepository.saveAndFlush(examSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the examSession
        restExamSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, examSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return examSessionRepository.count();
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

    protected ExamSession getPersistedExamSession(ExamSession examSession) {
        return examSessionRepository.findById(examSession.getId()).orElseThrow();
    }

    protected void assertPersistedExamSessionToMatchAllProperties(ExamSession expectedExamSession) {
        assertExamSessionAllPropertiesEquals(expectedExamSession, getPersistedExamSession(expectedExamSession));
    }

    protected void assertPersistedExamSessionToMatchUpdatableProperties(ExamSession expectedExamSession) {
        assertExamSessionAllUpdatablePropertiesEquals(expectedExamSession, getPersistedExamSession(expectedExamSession));
    }
}
