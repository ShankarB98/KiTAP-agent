package com.kitap.agent.database.repository;

import com.kitap.agent.database.model.ApplicationUnderTest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Aut Repository which is extending JpaRepository
 * @author KT1450
 */
@Transactional
public interface ApplicationUnderTestRepo extends JpaRepository<ApplicationUnderTest, Long> {
    /**
     * It fetches all details of an aut from aut table with single input
     * @param autName - uses to get that aut details
     * @return aut object
     */
    @Query(value = "SELECT * FROM application_under_test WHERE name = ?1", nativeQuery = true)
    ApplicationUnderTest isExists(String autName);

    /**
     * It fetches all details of an aut from aut table with two inputs
     * @param autName - uses to get that aut details
     * @param autType - uses to filter from different aut types
     * @return aut object
     */
    @Query(value = "SELECT * FROM application_under_test WHERE name = ?1 AND type = ?2", nativeQuery = true)
    ApplicationUnderTest isExists(String autName, String autType);

    /**
     * It fetches all aut names from aut table under a single aut type
     * @param autType - uses to get from same aut type
     * @return list of aut names
     */
    @Query(value = "SELECT name FROM application_under_test WHERE type = ?1", nativeQuery = true)
    List<String> getAllAUTNames(String autType);

    /**
     * It fetches all aut types from aut table
     * @return list of aut types
     */
    @Query(value = "SELECT DISTINCT type FROM application_under_test", nativeQuery = true)
    List<String> getAutTypes();

    /**
     * It fetches all aut names from aut table under a single aut type and deletes the input aut given
     * @param autName - uses to delete an aut from aut table
     */
    @Modifying
    @Query(value = "DELETE FROM application_under_test WHERE NAME = ?1", nativeQuery = true)
    void deleteAUT(String autName);
    //TODO need to verify based on aut type also
}