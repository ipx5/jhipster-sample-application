package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import com.mycompany.myapp.domain.enumeration.State;

/**
 * A Dvd.
 */
@Entity
@Table(name = "dvd")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dvd")
public class Dvd implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "release_year")
    private String releaseYear;

    @Column(name = "disk_count")
    private String diskCount;

    @Column(name = "format")
    private String format;

    @Column(name = "lang")
    private String lang;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "added")
    private Instant added;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Dvd name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public Dvd releaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDiskCount() {
        return diskCount;
    }

    public Dvd diskCount(String diskCount) {
        this.diskCount = diskCount;
        return this;
    }

    public void setDiskCount(String diskCount) {
        this.diskCount = diskCount;
    }

    public String getFormat() {
        return format;
    }

    public Dvd format(String format) {
        this.format = format;
        return this;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLang() {
        return lang;
    }

    public Dvd lang(String lang) {
        this.lang = lang;
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public State getState() {
        return state;
    }

    public Dvd state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Instant getAdded() {
        return added;
    }

    public Dvd added(Instant added) {
        this.added = added;
        return this;
    }

    public void setAdded(Instant added) {
        this.added = added;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dvd)) {
            return false;
        }
        return id != null && id.equals(((Dvd) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dvd{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", releaseYear='" + getReleaseYear() + "'" +
            ", diskCount='" + getDiskCount() + "'" +
            ", format='" + getFormat() + "'" +
            ", lang='" + getLang() + "'" +
            ", state='" + getState() + "'" +
            ", added='" + getAdded() + "'" +
            "}";
    }
}
