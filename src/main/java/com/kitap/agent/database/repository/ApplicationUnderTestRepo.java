package com.kitap.agent.database.repository;

import com.kitap.agent.database.model.ApplicationUnderTest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


@Transactional
public interface ApplicationUnderTestRepo extends JpaRepository<ApplicationUnderTest, Long> {

    //@Transactional
    @Query(value = "SELECT * FROM application_under_test WHERE name = ?1", nativeQuery = true)
    ApplicationUnderTest isExists(String autName);

    @Query(value = "SELECT * FROM application_under_test WHERE name = ?1 AND type = ?2", nativeQuery = true)
    ApplicationUnderTest isExists(String autName, String autType);


    @Query(value = "SELECT name FROM application_under_test WHERE type = ?1", nativeQuery = true)
    List<String> getAllAUTNames(String autType);

    @Query(value = "SELECT DISTINCT type FROM application_under_test", nativeQuery = true)
    List<String> getAutTypes();


    @Modifying
    @Query(value = "DELETE FROM application_under_test WHERE NAME = ?1", nativeQuery = true)
    void deleteAUT(String autName);


}
