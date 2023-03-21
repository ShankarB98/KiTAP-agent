package com.kitap.agent.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "executed_test_step")
public class ExecutedTestStep {
    @Id
    @GeneratedValue
    private Long id;
    private String testStepName;
    private Integer testStepVersion;
    private ZonedDateTime testStartedAt;
    private ZonedDateTime testStepFinishedAt;
    private String result;
    @Column(length = 5000)
    private String errorDetails;
    private String screenSnipPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExecutedTestStep that = (ExecutedTestStep) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
