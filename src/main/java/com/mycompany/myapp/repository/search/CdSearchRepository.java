package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Cd;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Cd} entity.
 */
public interface CdSearchRepository extends ElasticsearchRepository<Cd, Long> {
}
