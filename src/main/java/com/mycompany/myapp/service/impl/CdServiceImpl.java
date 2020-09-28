package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CdService;
import com.mycompany.myapp.domain.Cd;
import com.mycompany.myapp.repository.CdRepository;
import com.mycompany.myapp.repository.search.CdSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Cd}.
 */
@Service
@Transactional
public class CdServiceImpl implements CdService {

    private final Logger log = LoggerFactory.getLogger(CdServiceImpl.class);

    private final CdRepository cdRepository;

    private final CdSearchRepository cdSearchRepository;

    public CdServiceImpl(CdRepository cdRepository, CdSearchRepository cdSearchRepository) {
        this.cdRepository = cdRepository;
        this.cdSearchRepository = cdSearchRepository;
    }

    @Override
    public Cd save(Cd cd) {
        log.debug("Request to save Cd : {}", cd);
        Cd result = cdRepository.save(cd);
        cdSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cd> findAll(Pageable pageable) {
        log.debug("Request to get all Cds");
        return cdRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Cd> findOne(Long id) {
        log.debug("Request to get Cd : {}", id);
        return cdRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cd : {}", id);
        cdRepository.deleteById(id);
        cdSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cd> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cds for query {}", query);
        return cdSearchRepository.search(queryStringQuery(query), pageable);    }
}
