package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Dvd;
import com.mycompany.myapp.repository.DvdRepository;
import com.mycompany.myapp.repository.search.DvdSearchRepository;
import com.mycompany.myapp.service.DvdService;

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
 * Integration tests for the {@link DvdResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DvdResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_DISK_COUNT = "AAAAAAAAAA";
    private static final String UPDATED_DISK_COUNT = "BBBBBBBBBB";

    private static final String DEFAULT_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_LANG = "AAAAAAAAAA";
    private static final String UPDATED_LANG = "BBBBBBBBBB";

    private static final State DEFAULT_STATE = State.OK;
    private static final State UPDATED_STATE = State.AWAY;

    private static final Instant DEFAULT_ADDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DvdRepository dvdRepository;

    @Autowired
    private DvdService dvdService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.DvdSearchRepositoryMockConfiguration
     */
    @Autowired
    private DvdSearchRepository mockDvdSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDvdMockMvc;

    private Dvd dvd;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dvd createEntity(EntityManager em) {
        Dvd dvd = new Dvd()
            .name(DEFAULT_NAME)
            .releaseYear(DEFAULT_RELEASE_YEAR)
            .diskCount(DEFAULT_DISK_COUNT)
            .format(DEFAULT_FORMAT)
            .lang(DEFAULT_LANG)
            .state(DEFAULT_STATE)
            .added(DEFAULT_ADDED);
        return dvd;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dvd createUpdatedEntity(EntityManager em) {
        Dvd dvd = new Dvd()
            .name(UPDATED_NAME)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .diskCount(UPDATED_DISK_COUNT)
            .format(UPDATED_FORMAT)
            .lang(UPDATED_LANG)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);
        return dvd;
    }

    @BeforeEach
    public void initTest() {
        dvd = createEntity(em);
    }

    @Test
    @Transactional
    public void createDvd() throws Exception {
        int databaseSizeBeforeCreate = dvdRepository.findAll().size();
        // Create the Dvd
        restDvdMockMvc.perform(post("/api/dvds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dvd)))
            .andExpect(status().isCreated());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeCreate + 1);
        Dvd testDvd = dvdList.get(dvdList.size() - 1);
        assertThat(testDvd.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDvd.getReleaseYear()).isEqualTo(DEFAULT_RELEASE_YEAR);
        assertThat(testDvd.getDiskCount()).isEqualTo(DEFAULT_DISK_COUNT);
        assertThat(testDvd.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testDvd.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testDvd.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testDvd.getAdded()).isEqualTo(DEFAULT_ADDED);

        // Validate the Dvd in Elasticsearch
        verify(mockDvdSearchRepository, times(1)).save(testDvd);
    }

    @Test
    @Transactional
    public void createDvdWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dvdRepository.findAll().size();

        // Create the Dvd with an existing ID
        dvd.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDvdMockMvc.perform(post("/api/dvds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dvd)))
            .andExpect(status().isBadRequest());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dvd in Elasticsearch
        verify(mockDvdSearchRepository, times(0)).save(dvd);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dvdRepository.findAll().size();
        // set the field null
        dvd.setName(null);

        // Create the Dvd, which fails.


        restDvdMockMvc.perform(post("/api/dvds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dvd)))
            .andExpect(status().isBadRequest());

        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDvds() throws Exception {
        // Initialize the database
        dvdRepository.saveAndFlush(dvd);

        // Get all the dvdList
        restDvdMockMvc.perform(get("/api/dvds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dvd.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR)))
            .andExpect(jsonPath("$.[*].diskCount").value(hasItem(DEFAULT_DISK_COUNT)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].added").value(hasItem(DEFAULT_ADDED.toString())));
    }
    
    @Test
    @Transactional
    public void getDvd() throws Exception {
        // Initialize the database
        dvdRepository.saveAndFlush(dvd);

        // Get the dvd
        restDvdMockMvc.perform(get("/api/dvds/{id}", dvd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dvd.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.releaseYear").value(DEFAULT_RELEASE_YEAR))
            .andExpect(jsonPath("$.diskCount").value(DEFAULT_DISK_COUNT))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.added").value(DEFAULT_ADDED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingDvd() throws Exception {
        // Get the dvd
        restDvdMockMvc.perform(get("/api/dvds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDvd() throws Exception {
        // Initialize the database
        dvdService.save(dvd);

        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();

        // Update the dvd
        Dvd updatedDvd = dvdRepository.findById(dvd.getId()).get();
        // Disconnect from session so that the updates on updatedDvd are not directly saved in db
        em.detach(updatedDvd);
        updatedDvd
            .name(UPDATED_NAME)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .diskCount(UPDATED_DISK_COUNT)
            .format(UPDATED_FORMAT)
            .lang(UPDATED_LANG)
            .state(UPDATED_STATE)
            .added(UPDATED_ADDED);

        restDvdMockMvc.perform(put("/api/dvds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDvd)))
            .andExpect(status().isOk());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);
        Dvd testDvd = dvdList.get(dvdList.size() - 1);
        assertThat(testDvd.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDvd.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testDvd.getDiskCount()).isEqualTo(UPDATED_DISK_COUNT);
        assertThat(testDvd.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDvd.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testDvd.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testDvd.getAdded()).isEqualTo(UPDATED_ADDED);

        // Validate the Dvd in Elasticsearch
        verify(mockDvdSearchRepository, times(2)).save(testDvd);
    }

    @Test
    @Transactional
    public void updateNonExistingDvd() throws Exception {
        int databaseSizeBeforeUpdate = dvdRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDvdMockMvc.perform(put("/api/dvds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dvd)))
            .andExpect(status().isBadRequest());

        // Validate the Dvd in the database
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dvd in Elasticsearch
        verify(mockDvdSearchRepository, times(0)).save(dvd);
    }

    @Test
    @Transactional
    public void deleteDvd() throws Exception {
        // Initialize the database
        dvdService.save(dvd);

        int databaseSizeBeforeDelete = dvdRepository.findAll().size();

        // Delete the dvd
        restDvdMockMvc.perform(delete("/api/dvds/{id}", dvd.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dvd> dvdList = dvdRepository.findAll();
        assertThat(dvdList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dvd in Elasticsearch
        verify(mockDvdSearchRepository, times(1)).deleteById(dvd.getId());
    }

    @Test
    @Transactional
    public void searchDvd() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        dvdService.save(dvd);
        when(mockDvdSearchRepository.search(queryStringQuery("id:" + dvd.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dvd), PageRequest.of(0, 1), 1));

        // Search the dvd
        restDvdMockMvc.perform(get("/api/_search/dvds?query=id:" + dvd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dvd.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR)))
            .andExpect(jsonPath("$.[*].diskCount").value(hasItem(DEFAULT_DISK_COUNT)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].added").value(hasItem(DEFAULT_ADDED.toString())));
    }
}
