package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Dvd;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Dvd} entity.
 */
public interface DvdSearchRepository extends ElasticsearchRepository<Dvd, Long> {
}
