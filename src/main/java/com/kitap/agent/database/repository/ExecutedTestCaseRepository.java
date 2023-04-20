package com.kitap.agent.database.repository;

import com.kitap.agent.database.model.ExecutedTestCase;
import com.kitap.agent.database.model.dto.TestCaseCountResponse;
import com.kitap.agent.database.model.dto.TestCaseTimeResponse;
import com.kitap.agent.database.model.dto.TestResultTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
/**
 * ExecutedTestCase Repository which is extending JpaRepository
 * @author KT1450
 */
@Repository
public interface ExecutedTestCaseRepository extends JpaRepository<ExecutedTestCase, Long> {

    /**
     * It fetches list of aut names from executed test cases
     * @return list of aut names
     */
    @Query(value = "SELECT aut_name FROM EXECUTED_TEST_CASE", nativeQuery = true)
    List<String> getAllAUTNames();

    /**
     * It fetches list of test case versions from executed test cases
     * @param autName - used get version of test cases under this aut
     * @return list of versions
     */
    @Query(value = "SELECT TEST_CASE_VERSION FROM EXECUTED_TEST_CASE WHERE AUT_NAME = ?1", nativeQuery = true)
    List<Integer> getAllVersions(String autName);
    //TODO need to verify with aut type also

    /**
     * It fetches list of custom defined results
     * @param autName - aut name
     * @param testCaseVersion - version of test case
     * @param from - range of time start time
     * @param to - range of end time
     * @return list of test result table
     */
    @Query("SELECT new com.kitap.agent.database.model.dto.TestResultTable" +
            "(t.testCaseName , t.result, t.testCaseStartedAt) FROM ExecutedTestCase AS t " +
            "WHERE t.autName = ?1 AND t.testCaseVersion = ?2 AND t.testCaseStartedAt BETWEEN ?3 AND ?4 "+
            "ORDER BY t.testCaseStartedAt DESC")
    List<TestResultTable>getTestsResults(String autName, Integer testCaseVersion, ZonedDateTime from, ZonedDateTime to);

    /**
     * It fetches list of custom defined time response results
     * @param autName - aut name
     * @param result - test case result
     * @param testCaseVersion - version of test case
     * @param from - range of time start time
     * @param to - range of end time
     * @return list of time response test result table
     */
    @Query(value = "SELECT CAST(TEST_CASE_STARTED_AT AS DATE) AS executedAt, count(id) AS countResult " +
            "FROM EXECUTED_TEST_CASE " +
            "WHERE aut_name = ?1 AND " +
            "result = ?2 AND " +
            "test_case_version = ?3 AND " +
            "TEST_CASE_STARTED_AT BETWEEN ?4 AND ?5 " +
            "GROUP BY executedAt", nativeQuery = true)
    List<TestCaseTimeResponse> countByTimeForResult(String autName, String result, Integer testCaseVersion, ZonedDateTime from, ZonedDateTime to);

    /**
     * It fetches list of custom defined count response results
     * @param autName - aut name -> used to filter test result
     * @param result - test case result -> used to filter test result
     * @param version - version of test case -> used to filter test result
     * @param from - range of time start time -> used to filter test result
     * @param to - range of end time -> used to filter test result
     * @return list of count response test result table
     */
    @Query("SELECT new com.kitap.agent.database.model.dto.TestCaseCountResponse(t.testCaseName , COUNT(t.id) AS countResult) " +
            "FROM ExecutedTestCase AS t " +
            "WHERE t.autName = ?1 " +
            "AND t.result = ?2 " +
            "AND t.testCaseVersion = ?3 " +
            "AND t.testCaseStartedAt BETWEEN ?4 AND ?5 " +
            "GROUP BY t.testCaseName")
    List<TestCaseCountResponse> countByName(String autName, String result, Integer version, ZonedDateTime from, ZonedDateTime to);

    /**
     * It fetches list of custom defined time response results
     * @param autName - aut name -> used to filter test result
     * @param testCaseVersion - version of test case -> use to filter test result
     * @param from - range of time start time -> used to filter test result
     * @param to - range of end time -> used to filter test result
     * @return list of time response test result table
     */
    @Query(value = "SELECT CAST(TEST_CASE_STARTED_AT AS DATE) AS executedAt, count(id) AS countResult " +
            "FROM EXECUTED_TEST_CASE " +
            "WHERE aut_name = ?1 AND " +
            "test_case_version = ?2 AND " +
            "TEST_CASE_STARTED_AT BETWEEN ?3 AND ?4 " +
            "GROUP BY executedAt", nativeQuery = true)
    List<TestCaseTimeResponse> countByTime(String autName, Integer testCaseVersion, ZonedDateTime from, ZonedDateTime to);
}