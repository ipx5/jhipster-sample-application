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
 * A Cd.
 */
@Entity
@Table(name = "cd")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cd")
public class Cd implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "performer")
    private String performer;

    @Column(name = "release_year")
    private String releaseYear;

    @Column(name = "disk_count")
    private String diskCount;

    @Column(name = "medium")
    private String medium;

    @Column(name = "label")
    private String label;

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

    public Cd name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerformer() {
        return performer;
    }

    public Cd performer(String performer) {
        this.performer = performer;
        return this;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public Cd releaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDiskCount() {
        return diskCount;
    }

    public Cd diskCount(String diskCount) {
        this.diskCount = diskCount;
        return this;
    }

    public void setDiskCount(String diskCount) {
        this.diskCount = diskCount;
    }

    public String getMedium() {
        return medium;
    }

    public Cd medium(String medium) {
        this.medium = medium;
        return this;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLabel() {
        return label;
    }

    public Cd label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public State getState() {
        return state;
    }

    public Cd state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Instant getAdded() {
        return added;
    }

    public Cd added(Instant added) {
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
        if (!(o instanceof Cd)) {
            return false;
        }
        return id != null && id.equals(((Cd) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cd{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", performer='" + getPerformer() + "'" +
            ", releaseYear='" + getReleaseYear() + "'" +
            ", diskCount='" + getDiskCount() + "'" +
            ", medium='" + getMedium() + "'" +
            ", label='" + getLabel() + "'" +
            ", state='" + getState() + "'" +
            ", added='" + getAdded() + "'" +
            "}";
    }
}
