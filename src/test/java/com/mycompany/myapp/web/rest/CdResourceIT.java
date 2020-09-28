package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Cd;
import com.mycompany.myapp.repository.CdRepository;
import com.mycompany.myapp.repository.search.CdSearchRepository;
import com.mycompany.myapp.service.CdService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.State;
/**
 * Integration tests for the {@link CdResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CdResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERFORMER = "AAAAAAAAAA";
    private static final String UPDATED_PERFORMER = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_DISK_COUNT = "AAAAAAAAAA";
    private static final String UPDATED_DISK_COUNT = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIUM = "AAAAAAAAAA";
    private static final String UPDATED_MEDIUM = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final State DEFAULT_STATE = State.OK;
    private static final State UPDATED_STATE = State.AWAY;

    private static final Instant DEFAULT_ADDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CdRepository cdRepository;

    @Autowired
    private CdService cdService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.CdSearchRepositoryMockConfiguration
     */
    @Autowired
    private CdSearchRepository mockCdSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCdMockMvc;

    private Cd cd;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cd createEntity(EntityManager em) {
        Cd cd = new Cd()
            .name(DEFAULT_NAME)
            .performer(DEFAULT_PERFORMER)
            .releaseYear(DEFAULT_RELEASE_YEAR)
            .diskCount(DEFAULT_DISK_COUNT)
            .medium(DEFAULT_MEDIUM)
            .label(DEFAULT_LABEL)
            .state(DEFAULT_STATE)
            .added(DEFAULT_ADDED);
        return cd;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cd createUpdatedEntity(EntityManager em) {
        Cd cd = new Cd()
            .name(UPDATED_NAME)
            .performer(UPDATED_PERFORMER)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .diskCount(UPDATED_DISK_COUNT)
            .medium(UPDATED_MEDIUM)
            .label(UPDATED_LABEL)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);
        return cd;
    }

    @BeforeEach
    public void initTest() {
        cd = createEntity(em);
    }

    @Test
    @Transactional
    public void createCd() throws Exception {
        int databaseSizeBeforeCreate = cdRepository.findAll().size();
        // Create the Cd
        restCdMockMvc.perform(post("/api/cds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cd)))
            .andExpect(status().isCreated());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeCreate + 1);
        Cd testCd = cdList.get(cdList.size() - 1);
        assertThat(testCd.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCd.getPerformer()).isEqualTo(DEFAULT_PERFORMER);
        assertThat(testCd.getReleaseYear()).isEqualTo(DEFAULT_RELEASE_YEAR);
        assertThat(testCd.getDiskCount()).isEqualTo(DEFAULT_DISK_COUNT);
        assertThat(testCd.getMedium()).isEqualTo(DEFAULT_MEDIUM);
        assertThat(testCd.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testCd.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCd.getAdded()).isEqualTo(DEFAULT_ADDED);

        // Validate the Cd in Elasticsearch
        verify(mockCdSearchRepository, times(1)).save(testCd);
    }

    @Test
    @Transactional
    public void createCdWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cdRepository.findAll().size();

        // Create the Cd with an existing ID
        cd.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCdMockMvc.perform(post("/api/cds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cd)))
            .andExpect(status().isBadRequest());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cd in Elasticsearch
        verify(mockCdSearchRepository, times(0)).save(cd);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cdRepository.findAll().size();
        // set the field null
        cd.setName(null);

        // Create the Cd, which fails.


        restCdMockMvc.perform(post("/api/cds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cd)))
            .andExpect(status().isBadRequest());

        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCds() throws Exception {
        // Initialize the database
        cdRepository.saveAndFlush(cd);

        // Get all the cdList
        restCdMockMvc.perform(get("/api/cds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cd.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].performer").value(hasItem(DEFAULT_PERFORMER)))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR)))
            .andExpect(jsonPath("$.[*].diskCount").value(hasItem(DEFAULT_DISK_COUNT)))
            .andExpect(jsonPath("$.[*].medium").value(hasItem(DEFAULT_MEDIUM)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].added").value(hasItem(DEFAULT_ADDED.toString())));
    }
    
    @Test
    @Transactional
    public void getCd() throws Exception {
        // Initialize the database
        cdRepository.saveAndFlush(cd);

        // Get the cd
        restCdMockMvc.perform(get("/api/cds/{id}", cd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cd.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.performer").value(DEFAULT_PERFORMER))
            .andExpect(jsonPath("$.releaseYear").value(DEFAULT_RELEASE_YEAR))
            .andExpect(jsonPath("$.diskCount").value(DEFAULT_DISK_COUNT))
            .andExpect(jsonPath("$.medium").value(DEFAULT_MEDIUM))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.added").value(DEFAULT_ADDED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCd() throws Exception {
        // Get the cd
        restCdMockMvc.perform(get("/api/cds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCd() throws Exception {
        // Initialize the database
        cdService.save(cd);

        int databaseSizeBeforeUpdate = cdRepository.findAll().size();

        // Update the cd
        Cd updatedCd = cdRepository.findById(cd.getId()).get();
        // Disconnect from session so that the updates on updatedCd are not directly saved in db
        em.detach(updatedCd);
        updatedCd
            .name(UPDATED_NAME)
            .performer(UPDATED_PERFORMER)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .diskCount(UPDATED_DISK_COUNT)
            .medium(UPDATED_MEDIUM)
            .label(UPDATED_LABEL)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);

        restCdMockMvc.perform(put("/api/cds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCd)))
            .andExpect(status().isOk());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);
        Cd testCd = cdList.get(cdList.size() - 1);
        assertThat(testCd.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCd.getPerformer()).isEqualTo(UPDATED_PERFORMER);
        assertThat(testCd.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testCd.getDiskCount()).isEqualTo(UPDATED_DISK_COUNT);
        assertThat(testCd.getMedium()).isEqualTo(UPDATED_MEDIUM);
        assertThat(testCd.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testCd.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCd.getAdded()).isEqualTo(UPDATED_ADDED);

        // Validate the Cd in Elasticsearch
        verify(mockCdSearchRepository, times(2)).save(testCd);
    }

    @Test
    @Transactional
    public void updateNonExistingCd() throws Exception {
        int databaseSizeBeforeUpdate = cdRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCdMockMvc.perform(put("/api/cds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cd)))
            .andExpect(status().isBadRequest());

        // Validate the Cd in the database
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cd in Elasticsearch
        verify(mockCdSearchRepository, times(0)).save(cd);
    }

    @Test
    @Transactional
    public void deleteCd() throws Exception {
        // Initialize the database
        cdService.save(cd);

        int databaseSizeBeforeDelete = cdRepository.findAll().size();

        // Delete the cd
        restCdMockMvc.perform(delete("/api/cds/{id}", cd.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cd> cdList = cdRepository.findAll();
        assertThat(cdList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cd in Elasticsearch
        verify(mockCdSearchRepository, times(1)).deleteById(cd.getId());
    }

    @Test
    @Transactional
    public void searchCd() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cdService.save(cd);
        when(mockCdSearchRepository.search(queryStringQuery("id:" + cd.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cd), PageRequest.of(0, 1), 1));

        // Search the cd
        restCdMockMvc.perform(get("/api/_search/cds?query=id:" + cd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cd.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].performer").value(hasItem(DEFAULT_PERFORMER)))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR)))
            .andExpect(jsonPath("$.[*].diskCount").value(hasItem(DEFAULT_DISK_COUNT)))
            .andExpect(jsonPath("$.[*].medium").value(hasItem(DEFAULT_MEDIUM)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].added").value(hasItem(DEFAULT_ADDED.toString())));
    }
}
