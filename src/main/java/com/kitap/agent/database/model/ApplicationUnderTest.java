package com.kitap.agent.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * This is the entity class of ApplicationUnderTest with fields like name, displayName,
 *      description, url, executableFilePath, type of aut, version of aut, createdBy,
 *      createdAt time, modifiedAt time, modifiedBy, isActive
 * @author KT1450
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "application_under_test")
public class ApplicationUnderTest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String displayName;
    private String description;
    private URI url;
    private String executableFilePath;
    private String type;
    private Integer version;
    private String createdBy;
    private ZonedDateTime createdAt;
    private Long modifiedBy;
    private ZonedDateTime modifiedAt;
    private Boolean isActive;

    /**
     * Compare and checks two objects are equal or not
     * @param o input object
     * @return true if two objects are same, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationUnderTest that = (ApplicationUnderTest) o;
        return id != null && Objects.equals(id, that.id);
    }

    /**
     * getting the hashcode of an object
     * @return value of hashcode
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
