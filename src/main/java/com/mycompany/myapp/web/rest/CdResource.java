package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Cd;
import com.mycompany.myapp.service.CdService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Cd}.
 */
@RestController
@RequestMapping("/api")
public class CdResource {

    private final Logger log = LoggerFactory.getLogger(CdResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationCd";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CdService cdService;

    public CdResource(CdService cdService) {
        this.cdService = cdService;
    }

    /**
     * {@code POST  /cds} : Create a new cd.
     *
     * @param cd the cd to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cd, or with status {@code 400 (Bad Request)} if the cd has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cds")
    public ResponseEntity<Cd> createCd(@Valid @RequestBody Cd cd) throws URISyntaxException {
        log.debug("REST request to save Cd : {}", cd);
        if (cd.getId() != null) {
            throw new BadRequestAlertException("A new cd cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cd result = cdService.save(cd);
        return ResponseEntity.created(new URI("/api/cds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cds} : Updates an existing cd.
     *
     * @param cd the cd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cd,
     * or with status {@code 400 (Bad Request)} if the cd is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cds")
    public ResponseEntity<Cd> updateCd(@Valid @RequestBody Cd cd) throws URISyntaxException {
        log.debug("REST request to update Cd : {}", cd);
        if (cd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cd result = cdService.save(cd);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cd.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cds} : get all the cds.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cds in body.
     */
    @GetMapping("/cds")
    public ResponseEntity<List<Cd>> getAllCds(Pageable pageable) {
        log.debug("REST request to get a page of Cds");
        Page<Cd> page = cdService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cds/:id} : get the "id" cd.
     *
     * @param id the id of the cd to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cd, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cds/{id}")
    public ResponseEntity<Cd> getCd(@PathVariable Long id) {
        log.debug("REST request to get Cd : {}", id);
        Optional<Cd> cd = cdService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cd);
    }

    /**
     * {@code DELETE  /cds/:id} : delete the "id" cd.
     *
     * @param id the id of the cd to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cds/{id}")
    public ResponseEntity<Void> deleteCd(@PathVariable Long id) {
        log.debug("REST request to delete Cd : {}", id);
        cdService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/cds?query=:query} : search for the cd corresponding
     * to the query.
     *
     * @param query the query of the cd search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/cds")
    public ResponseEntity<List<Cd>> searchCds(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cds for query {}", query);
        Page<Cd> page = cdService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
