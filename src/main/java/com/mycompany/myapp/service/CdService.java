package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Cd;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Cd}.
 */
public interface CdService {

    /**
     * Save a cd.
     *
     * @param cd the entity to save.
     * @return the persisted entity.
     */
    Cd save(Cd cd);

    /**
     * Get all the cds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cd> findAll(Pageable pageable);


    /**
     * Get the "id" cd.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cd> findOne(Long id);

    /**
     * Delete the "id" cd.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cd corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cd> search(String query, Pageable pageable);
}
