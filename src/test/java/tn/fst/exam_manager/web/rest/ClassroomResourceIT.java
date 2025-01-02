package tn.fst.exam_manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.fst.exam_manager.domain.ClassroomAsserts.*;
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
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.repository.ClassroomRepository;
import tn.fst.exam_manager.service.dto.ClassroomDTO;
import tn.fst.exam_manager.service.mapper.ClassroomMapper;

/**
 * Integration tests for the {@link ClassroomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = { "ROLE_SUPER_ADMIN" })
class ClassroomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final String ENTITY_API_URL = "/api/classrooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ClassroomMapper classroomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassroomMockMvc;

    private Classroom classroom;

    private Classroom insertedClassroom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classroom createEntity() {
        return new Classroom().name(DEFAULT_NAME).capacity(DEFAULT_CAPACITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classroom createUpdatedEntity() {
        return new Classroom().name(UPDATED_NAME).capacity(UPDATED_CAPACITY);
    }

    @BeforeEach
    public void initTest() {
        classroom = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedClassroom != null) {
            classroomRepository.delete(insertedClassroom);
            insertedClassroom = null;
        }
    }

    @Test
    @Transactional
    void createClassroom() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Classroom
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);
        var returnedClassroomDTO = om.readValue(
            restClassroomMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classroomDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClassroomDTO.class
        );

        // Validate the Classroom in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClassroom = classroomMapper.toEntity(returnedClassroomDTO);
        assertClassroomUpdatableFieldsEquals(returnedClassroom, getPersistedClassroom(returnedClassroom));

        insertedClassroom = returnedClassroom;
    }

    @Test
    @Transactional
    void createClassroomWithExistingId() throws Exception {
        // Create the Classroom with an existing ID
        classroom.setId(1L);
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classroomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classroom.setName(null);

        // Create the Classroom, which fails.
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classroomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classroom.setCapacity(null);

        // Create the Classroom, which fails.
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classroomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassrooms() throws Exception {
        // Initialize the database
        insertedClassroom = classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)));
    }

    @Test
    @Transactional
    void getClassroom() throws Exception {
        // Initialize the database
        insertedClassroom = classroomRepository.saveAndFlush(classroom);

        // Get the classroom
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL_ID, classroom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classroom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY));
    }

    @Test
    @Transactional
    void getNonExistingClassroom() throws Exception {
        // Get the classroom
        restClassroomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClassroom() throws Exception {
        // Initialize the database
        insertedClassroom = classroomRepository.saveAndFlush(classroom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classroom
        Classroom updatedClassroom = classroomRepository.findById(classroom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClassroom are not directly saved in db
        em.detach(updatedClassroom);
        updatedClassroom.name(UPDATED_NAME).capacity(UPDATED_CAPACITY);
        ClassroomDTO classroomDTO = classroomMapper.toDto(updatedClassroom);

        restClassroomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classroomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classroomDTO))
            )
            .andExpect(status().isOk());

        // Validate the Classroom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClassroomToMatchAllProperties(updatedClassroom);
    }

    @Test
    @Transactional
    void putNonExistingClassroom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classroom.setId(longCount.incrementAndGet());

        // Create the Classroom
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classroomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classroomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassroom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classroom.setId(longCount.incrementAndGet());

        // Create the Classroom
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classroomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassroom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classroom.setId(longCount.incrementAndGet());

        // Create the Classroom
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classroomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classroom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassroomWithPatch() throws Exception {
        // Initialize the database
        insertedClassroom = classroomRepository.saveAndFlush(classroom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classroom using partial update
        Classroom partialUpdatedClassroom = new Classroom();
        partialUpdatedClassroom.setId(classroom.getId());

        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassroom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassroom))
            )
            .andExpect(status().isOk());

        // Validate the Classroom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassroomUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClassroom, classroom),
            getPersistedClassroom(classroom)
        );
    }

    @Test
    @Transactional
    void fullUpdateClassroomWithPatch() throws Exception {
        // Initialize the database
        insertedClassroom = classroomRepository.saveAndFlush(classroom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classroom using partial update
        Classroom partialUpdatedClassroom = new Classroom();
        partialUpdatedClassroom.setId(classroom.getId());

        partialUpdatedClassroom.name(UPDATED_NAME).capacity(UPDATED_CAPACITY);

        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassroom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassroom))
            )
            .andExpect(status().isOk());

        // Validate the Classroom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassroomUpdatableFieldsEquals(partialUpdatedClassroom, getPersistedClassroom(partialUpdatedClassroom));
    }

    @Test
    @Transactional
    void patchNonExistingClassroom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classroom.setId(longCount.incrementAndGet());

        // Create the Classroom
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classroomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classroomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassroom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classroom.setId(longCount.incrementAndGet());

        // Create the Classroom
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classroomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassroom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classroom.setId(longCount.incrementAndGet());

        // Create the Classroom
        ClassroomDTO classroomDTO = classroomMapper.toDto(classroom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(classroomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classroom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassroom() throws Exception {
        // Initialize the database
        insertedClassroom = classroomRepository.saveAndFlush(classroom);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the classroom
        restClassroomMockMvc
            .perform(delete(ENTITY_API_URL_ID, classroom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return classroomRepository.count();
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

    protected Classroom getPersistedClassroom(Classroom classroom) {
        return classroomRepository.findById(classroom.getId()).orElseThrow();
    }

    protected void assertPersistedClassroomToMatchAllProperties(Classroom expectedClassroom) {
        assertClassroomAllPropertiesEquals(expectedClassroom, getPersistedClassroom(expectedClassroom));
    }

    protected void assertPersistedClassroomToMatchUpdatableProperties(Classroom expectedClassroom) {
        assertClassroomAllUpdatablePropertiesEquals(expectedClassroom, getPersistedClassroom(expectedClassroom));
    }
}
