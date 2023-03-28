package com.kitap.agent.api.controller;

import com.kitap.agent.database.model.ExecutedTestCase;
import com.kitap.agent.database.model.dto.TestCaseCountResponse;
import com.kitap.agent.database.model.dto.TestCaseTimeResponse;
import com.kitap.agent.database.model.dto.TestResultTable;
import com.kitap.agent.database.repository.ExecutedTestCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/executed-testcases")
public class GrafanaController {

    private final ExecutedTestCaseRepository executedTestCaseRepository;


    @GetMapping()
    public List<ExecutedTestCase> getTestsResults() {
        return executedTestCaseRepository.findAll();
    }

    /**
     * @Description returns list test result tables
     * @param autName aut name
     * @param testCaseVersion aut version
     * @param fromTime range of time start time
     * @param toTime range of end time
     * */
    @GetMapping("/table/{autName}/{testCaseVersion}/{fromTime}/{toTime}")
    public List<TestResultTable> getTestsResultsTable(@PathVariable String autName, @PathVariable Integer testCaseVersion, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {
        System.out.println("TestTest");
        System.out.println(autName);
        System.out.println(testCaseVersion);
        System.out.println(fromTime);
        System.out.println(toTime);
        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return executedTestCaseRepository.getTestsResults(autName, testCaseVersion, from, to);
    }

    /**
     * @Description returns list of aut's those linked with executed test cases
     * */
    @GetMapping("/aut")
    public List<String> getAuts() {
        return executedTestCaseRepository.getAllAUTNames();
    }


    /**
     * @Description returns list of versions those linked with executed test cases
     * @param autName aut name
     * */
    @GetMapping("/version/{autName}")
    public List<Integer> getVersions(@PathVariable String autName) {
        return executedTestCaseRepository.getAllVersions(autName);
    }

    /**
     * @Description returns list test result tables of time response
     * @param autName aut name
     * @param result result of test case
     * @param testCaseVersion aut version
     * @param fromTime range of time start time
     * @param toTime range of end time
     * */
    @GetMapping("/countByTimeForResult/{autName}/{result}/{testCaseVersion}/{fromTime}/{toTime}")
    public List<TestCaseTimeResponse> countByTimeForResult(@PathVariable String autName, @PathVariable String result, @PathVariable Integer testCaseVersion, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {
        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return executedTestCaseRepository.countByTimeForResult(autName, result, testCaseVersion, from, to);
    }

    /**
     * @Description returns list test result tables of count response
     * @param autName aut name
     * @param result result of test case
     * @param testCaseVersion aut version
     * @param fromTime range of time start time
     * @param toTime range of end time
     * */
    @GetMapping("/countByName/{autName}/{result}/{testCaseVersion}/{fromTime}/{toTime}")
    public List<TestCaseCountResponse> countByName(@PathVariable String autName, @PathVariable String result, @PathVariable Integer testCaseVersion, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {

        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

        return executedTestCaseRepository.countByName(autName, result, testCaseVersion, from, to);
    }

    /**
     * @Description returns list test result tables of time response
     * @param autName aut name
     * @param testCaseVersion aut version
     * @param fromTime range of time start time
     * @param toTime range of end time
     * */
    @GetMapping("/countByTime/{autName}/{testCaseVersion}/{fromTime}/{toTime}")
    public List<TestCaseTimeResponse> countByTime(@PathVariable String autName, @PathVariable Integer testCaseVersion, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {

        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return executedTestCaseRepository.countByTime(autName, testCaseVersion, from, to);
    }
}

