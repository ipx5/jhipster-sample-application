package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Dvd;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Dvd}.
 */
public interface DvdService {

    /**
     * Save a dvd.
     *
     * @param dvd the entity to save.
     * @return the persisted entity.
     */
    Dvd save(Dvd dvd);

    /**
     * Get all the dvds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dvd> findAll(Pageable pageable);


    /**
     * Get the "id" dvd.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dvd> findOne(Long id);

    /**
     * Delete the "id" dvd.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the dvd corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dvd> search(String query, Pageable pageable);
}
