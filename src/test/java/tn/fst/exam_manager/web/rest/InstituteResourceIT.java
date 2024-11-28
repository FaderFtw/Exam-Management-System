package tn.fst.exam_manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.fst.exam_manager.domain.InstituteAsserts.*;
import static tn.fst.exam_manager.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
import tn.fst.exam_manager.domain.Institute;
import tn.fst.exam_manager.repository.InstituteRepository;
import tn.fst.exam_manager.service.dto.InstituteDTO;
import tn.fst.exam_manager.service.mapper.InstituteMapper;

/**
 * Integration tests for the {@link InstituteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InstituteResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/institutes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private InstituteMapper instituteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstituteMockMvc;

    private Institute institute;

    private Institute insertedInstitute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institute createEntity() {
        return new Institute()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .website(DEFAULT_WEBSITE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institute createUpdatedEntity() {
        return new Institute()
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE);
    }

    @BeforeEach
    public void initTest() {
        institute = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInstitute != null) {
            instituteRepository.delete(insertedInstitute);
            insertedInstitute = null;
        }
    }

    @Test
    @Transactional
    void createInstitute() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Institute
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);
        var returnedInstituteDTO = om.readValue(
            restInstituteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instituteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InstituteDTO.class
        );

        // Validate the Institute in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInstitute = instituteMapper.toEntity(returnedInstituteDTO);
        assertInstituteUpdatableFieldsEquals(returnedInstitute, getPersistedInstitute(returnedInstitute));

        insertedInstitute = returnedInstitute;
    }

    @Test
    @Transactional
    void createInstituteWithExistingId() throws Exception {
        // Create the Institute with an existing ID
        institute.setId(1L);
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstituteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instituteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Institute in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        institute.setName(null);

        // Create the Institute, which fails.
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);

        restInstituteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instituteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        institute.setLocation(null);

        // Create the Institute, which fails.
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);

        restInstituteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instituteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInstitutes() throws Exception {
        // Initialize the database
        insertedInstitute = instituteRepository.saveAndFlush(institute);

        // Get all the instituteList
        restInstituteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));
    }

    @Test
    @Transactional
    void getInstitute() throws Exception {
        // Initialize the database
        insertedInstitute = instituteRepository.saveAndFlush(institute);

        // Get the institute
        restInstituteMockMvc
            .perform(get(ENTITY_API_URL_ID, institute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(institute.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64.getEncoder().encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE));
    }

    @Test
    @Transactional
    void getNonExistingInstitute() throws Exception {
        // Get the institute
        restInstituteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInstitute() throws Exception {
        // Initialize the database
        insertedInstitute = instituteRepository.saveAndFlush(institute);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the institute
        Institute updatedInstitute = instituteRepository.findById(institute.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInstitute are not directly saved in db
        em.detach(updatedInstitute);
        updatedInstitute
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE);
        InstituteDTO instituteDTO = instituteMapper.toDto(updatedInstitute);

        restInstituteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, instituteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(instituteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Institute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInstituteToMatchAllProperties(updatedInstitute);
    }

    @Test
    @Transactional
    void putNonExistingInstitute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        institute.setId(longCount.incrementAndGet());

        // Create the Institute
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstituteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, instituteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(instituteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Institute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstitute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        institute.setId(longCount.incrementAndGet());

        // Create the Institute
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstituteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(instituteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Institute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstitute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        institute.setId(longCount.incrementAndGet());

        // Create the Institute
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstituteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(instituteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Institute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInstituteWithPatch() throws Exception {
        // Initialize the database
        insertedInstitute = instituteRepository.saveAndFlush(institute);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the institute using partial update
        Institute partialUpdatedInstitute = new Institute();
        partialUpdatedInstitute.setId(institute.getId());

        partialUpdatedInstitute.phone(UPDATED_PHONE);

        restInstituteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitute.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInstitute))
            )
            .andExpect(status().isOk());

        // Validate the Institute in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInstituteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInstitute, institute),
            getPersistedInstitute(institute)
        );
    }

    @Test
    @Transactional
    void fullUpdateInstituteWithPatch() throws Exception {
        // Initialize the database
        insertedInstitute = instituteRepository.saveAndFlush(institute);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the institute using partial update
        Institute partialUpdatedInstitute = new Institute();
        partialUpdatedInstitute.setId(institute.getId());

        partialUpdatedInstitute
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE);

        restInstituteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitute.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInstitute))
            )
            .andExpect(status().isOk());

        // Validate the Institute in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInstituteUpdatableFieldsEquals(partialUpdatedInstitute, getPersistedInstitute(partialUpdatedInstitute));
    }

    @Test
    @Transactional
    void patchNonExistingInstitute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        institute.setId(longCount.incrementAndGet());

        // Create the Institute
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstituteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, instituteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(instituteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Institute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstitute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        institute.setId(longCount.incrementAndGet());

        // Create the Institute
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstituteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(instituteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Institute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstitute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        institute.setId(longCount.incrementAndGet());

        // Create the Institute
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstituteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(instituteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Institute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInstitute() throws Exception {
        // Initialize the database
        insertedInstitute = instituteRepository.saveAndFlush(institute);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the institute
        restInstituteMockMvc
            .perform(delete(ENTITY_API_URL_ID, institute.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return instituteRepository.count();
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

    protected Institute getPersistedInstitute(Institute institute) {
        return instituteRepository.findById(institute.getId()).orElseThrow();
    }

    protected void assertPersistedInstituteToMatchAllProperties(Institute expectedInstitute) {
        assertInstituteAllPropertiesEquals(expectedInstitute, getPersistedInstitute(expectedInstitute));
    }

    protected void assertPersistedInstituteToMatchUpdatableProperties(Institute expectedInstitute) {
        assertInstituteAllUpdatablePropertiesEquals(expectedInstitute, getPersistedInstitute(expectedInstitute));
    }
}
