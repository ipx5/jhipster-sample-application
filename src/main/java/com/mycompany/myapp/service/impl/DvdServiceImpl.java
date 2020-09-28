package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.DvdService;
import com.mycompany.myapp.domain.Dvd;
import com.mycompany.myapp.repository.DvdRepository;
import com.mycompany.myapp.repository.search.DvdSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Dvd}.
 */
@Service
@Transactional
public class DvdServiceImpl implements DvdService {

    private final Logger log = LoggerFactory.getLogger(DvdServiceImpl.class);

    private final DvdRepository dvdRepository;

    private final DvdSearchRepository dvdSearchRepository;

    public DvdServiceImpl(DvdRepository dvdRepository, DvdSearchRepository dvdSearchRepository) {
        this.dvdRepository = dvdRepository;
        this.dvdSearchRepository = dvdSearchRepository;
    }

    @Override
    public Dvd save(Dvd dvd) {
        log.debug("Request to save Dvd : {}", dvd);
        Dvd result = dvdRepository.save(dvd);
        dvdSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dvd> findAll(Pageable pageable) {
        log.debug("Request to get all Dvds");
        return dvdRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Dvd> findOne(Long id) {
        log.debug("Request to get Dvd : {}", id);
        return dvdRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dvd : {}", id);
        dvdRepository.deleteById(id);
        dvdSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dvd> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Dvds for query {}", query);
        return dvdSearchRepository.search(queryStringQuery(query), pageable);    }
}
