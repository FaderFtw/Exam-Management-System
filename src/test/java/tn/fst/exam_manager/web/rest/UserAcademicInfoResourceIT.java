package tn.fst.exam_manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.fst.exam_manager.domain.UserAcademicInfoAsserts.*;
import static tn.fst.exam_manager.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import tn.fst.exam_manager.domain.User;
import tn.fst.exam_manager.domain.UserAcademicInfo;
import tn.fst.exam_manager.repository.UserAcademicInfoRepository;
import tn.fst.exam_manager.repository.UserRepository;
import tn.fst.exam_manager.service.UserAcademicInfoService;
import tn.fst.exam_manager.service.dto.UserAcademicInfoDTO;
import tn.fst.exam_manager.service.mapper.UserAcademicInfoMapper;

/**
 * Integration tests for the {@link UserAcademicInfoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserAcademicInfoResourceIT {

    private static final String ENTITY_API_URL = "/api/user-academic-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAcademicInfoRepository userAcademicInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserAcademicInfoRepository userAcademicInfoRepositoryMock;

    @Autowired
    private UserAcademicInfoMapper userAcademicInfoMapper;

    @Mock
    private UserAcademicInfoService userAcademicInfoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAcademicInfoMockMvc;

    private UserAcademicInfo userAcademicInfo;

    private UserAcademicInfo insertedUserAcademicInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAcademicInfo createEntity(EntityManager em) {
        UserAcademicInfo userAcademicInfo = new UserAcademicInfo();
        // Add required entity
        String uniqueLogin = PublicUserResourceIT.generateUniqueLogin();
        User user = UserResourceIT.initTestUserWithCustomLogin(uniqueLogin);
        em.persist(user);
        em.flush();
        userAcademicInfo.setUser(user);
        return userAcademicInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAcademicInfo createUpdatedEntity(EntityManager em) {
        UserAcademicInfo updatedUserAcademicInfo = new UserAcademicInfo();
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedUserAcademicInfo.setUser(user);
        return updatedUserAcademicInfo;
    }

    @BeforeEach
    public void initTest() {
        userAcademicInfo = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserAcademicInfo != null) {
            userAcademicInfoRepository.delete(insertedUserAcademicInfo);
            insertedUserAcademicInfo = null;
        }
    }

    @Test
    @Transactional
    void createUserAcademicInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAcademicInfo
        UserAcademicInfoDTO userAcademicInfoDTO = userAcademicInfoMapper.toDto(userAcademicInfo);
        var returnedUserAcademicInfoDTO = om.readValue(
            restUserAcademicInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAcademicInfoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAcademicInfoDTO.class
        );

        // Validate the UserAcademicInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserAcademicInfo = userAcademicInfoMapper.toEntity(returnedUserAcademicInfoDTO);
        assertUserAcademicInfoUpdatableFieldsEquals(returnedUserAcademicInfo, getPersistedUserAcademicInfo(returnedUserAcademicInfo));

        insertedUserAcademicInfo = returnedUserAcademicInfo;
    }

    @Test
    @Transactional
    void createUserAcademicInfoWithExistingId() throws Exception {
        // Create the UserAcademicInfo with an existing ID
        userAcademicInfo.setId(1L);
        UserAcademicInfoDTO userAcademicInfoDTO = userAcademicInfoMapper.toDto(userAcademicInfo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAcademicInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAcademicInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAcademicInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAcademicInfos() throws Exception {
        // Initialize the database
        insertedUserAcademicInfo = userAcademicInfoRepository.saveAndFlush(userAcademicInfo);

        // Get all the userAcademicInfoList
        restUserAcademicInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAcademicInfo.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAcademicInfosWithEagerRelationshipsIsEnabled() throws Exception {
        when(userAcademicInfoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAcademicInfoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userAcademicInfoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAcademicInfosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userAcademicInfoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAcademicInfoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userAcademicInfoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserAcademicInfo() throws Exception {
        // Initialize the database
        insertedUserAcademicInfo = userAcademicInfoRepository.saveAndFlush(userAcademicInfo);

        // Get the userAcademicInfo
        restUserAcademicInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, userAcademicInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAcademicInfo.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserAcademicInfo() throws Exception {
        // Get the userAcademicInfo
        restUserAcademicInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAcademicInfo() throws Exception {
        // Initialize the database
        insertedUserAcademicInfo = userAcademicInfoRepository.saveAndFlush(userAcademicInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAcademicInfo
        UserAcademicInfo updatedUserAcademicInfo = userAcademicInfoRepository.findById(userAcademicInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAcademicInfo are not directly saved in db
        em.detach(updatedUserAcademicInfo);
        UserAcademicInfoDTO userAcademicInfoDTO = userAcademicInfoMapper.toDto(updatedUserAcademicInfo);

        restUserAcademicInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAcademicInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAcademicInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAcademicInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAcademicInfoToMatchAllProperties(updatedUserAcademicInfo);
    }

    @Test
    @Transactional
    void putNonExistingUserAcademicInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAcademicInfo.setId(longCount.incrementAndGet());

        // Create the UserAcademicInfo
        UserAcademicInfoDTO userAcademicInfoDTO = userAcademicInfoMapper.toDto(userAcademicInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAcademicInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAcademicInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAcademicInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAcademicInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAcademicInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAcademicInfo.setId(longCount.incrementAndGet());

        // Create the UserAcademicInfo
        UserAcademicInfoDTO userAcademicInfoDTO = userAcademicInfoMapper.toDto(userAcademicInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAcademicInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAcademicInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAcademicInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAcademicInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAcademicInfo.setId(longCount.incrementAndGet());

        // Create the UserAcademicInfo
        UserAcademicInfoDTO userAcademicInfoDTO = userAcademicInfoMapper.toDto(userAcademicInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAcademicInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAcademicInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAcademicInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAcademicInfoWithPatch() throws Exception {
        // Initialize the database
        insertedUserAcademicInfo = userAcademicInfoRepository.saveAndFlush(userAcademicInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAcademicInfo using partial update
        UserAcademicInfo partialUpdatedUserAcademicInfo = new UserAcademicInfo();
        partialUpdatedUserAcademicInfo.setId(userAcademicInfo.getId());

        restUserAcademicInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAcademicInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAcademicInfo))
            )
            .andExpect(status().isOk());

        // Validate the UserAcademicInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAcademicInfoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAcademicInfo, userAcademicInfo),
            getPersistedUserAcademicInfo(userAcademicInfo)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAcademicInfoWithPatch() throws Exception {
        // Initialize the database
        insertedUserAcademicInfo = userAcademicInfoRepository.saveAndFlush(userAcademicInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAcademicInfo using partial update
        UserAcademicInfo partialUpdatedUserAcademicInfo = new UserAcademicInfo();
        partialUpdatedUserAcademicInfo.setId(userAcademicInfo.getId());

        restUserAcademicInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAcademicInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAcademicInfo))
            )
            .andExpect(status().isOk());

        // Validate the UserAcademicInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAcademicInfoUpdatableFieldsEquals(
            partialUpdatedUserAcademicInfo,
            getPersistedUserAcademicInfo(partialUpdatedUserAcademicInfo)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserAcademicInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAcademicInfo.setId(longCount.incrementAndGet());

        // Create the UserAcademicInfo
        UserAcademicInfoDTO userAcademicInfoDTO = userAcademicInfoMapper.toDto(userAcademicInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAcademicInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAcademicInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAcademicInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAcademicInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAcademicInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAcademicInfo.setId(longCount.incrementAndGet());

        // Create the UserAcademicInfo
        UserAcademicInfoDTO userAcademicInfoDTO = userAcademicInfoMapper.toDto(userAcademicInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAcademicInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAcademicInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAcademicInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAcademicInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAcademicInfo.setId(longCount.incrementAndGet());

        // Create the UserAcademicInfo
        UserAcademicInfoDTO userAcademicInfoDTO = userAcademicInfoMapper.toDto(userAcademicInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAcademicInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAcademicInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAcademicInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAcademicInfo() throws Exception {
        // Initialize the database
        insertedUserAcademicInfo = userAcademicInfoRepository.saveAndFlush(userAcademicInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAcademicInfo
        restUserAcademicInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAcademicInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAcademicInfoRepository.count();
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

    protected UserAcademicInfo getPersistedUserAcademicInfo(UserAcademicInfo userAcademicInfo) {
        return userAcademicInfoRepository.findById(userAcademicInfo.getId()).orElseThrow();
    }

    protected void assertPersistedUserAcademicInfoToMatchAllProperties(UserAcademicInfo expectedUserAcademicInfo) {
        assertUserAcademicInfoAllPropertiesEquals(expectedUserAcademicInfo, getPersistedUserAcademicInfo(expectedUserAcademicInfo));
    }

    protected void assertPersistedUserAcademicInfoToMatchUpdatableProperties(UserAcademicInfo expectedUserAcademicInfo) {
        assertUserAcademicInfoAllUpdatablePropertiesEquals(
            expectedUserAcademicInfo,
            getPersistedUserAcademicInfo(expectedUserAcademicInfo)
        );
    }
}
