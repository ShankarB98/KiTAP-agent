package com.kitap.agent.database.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "executed_test_case")
public class ExecutedTestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String testCaseName;
    private String autName;
    private Integer testCaseVersion;
    private ZonedDateTime testCaseStartedAt;
    private ZonedDateTime testCaseFinishedAt;
    private String result;
    private String browserName;
    private String browserVersion;
    private String osName;
    private String osVersion;
    private String deviceType;
    private String cloudVendor;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "testcase_id")
    private List<ExecutedTestStep> executedTestStepList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExecutedTestCase that = (ExecutedTestCase) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
