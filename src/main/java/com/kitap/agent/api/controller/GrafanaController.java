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

    /*@PostMapping("/test-result")
    public EExecutedTestCase createTestResult(@RequestBody EExecutedTestCase testResult) {

        testResult.setCreatedAt(ZonedDateTime.now().minusDays(0));
        return testResultRepository.save(testResult);
    }*/


    @GetMapping()
    public List<ExecutedTestCase> getTestsResults() {
        return executedTestCaseRepository.findAll();
    }

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

    @GetMapping("/aut")
    public List<String> getAuts() {
        return executedTestCaseRepository.getAllAUTNames();
    }

    @GetMapping("/version/{autName}")
    public List<Integer> getVersions(@PathVariable String autName) {
        return executedTestCaseRepository.getAllVersions(autName);
    }

    @GetMapping("/countByTimeForResult/{autName}/{result}/{testCaseVersion}/{fromTime}/{toTime}")
    public List<TestCaseTimeResponse> countByTimeForResult(@PathVariable String autName, @PathVariable String result, @PathVariable Integer testCaseVersion, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {

        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return executedTestCaseRepository.countByTimeForResult(autName, result, testCaseVersion, from, to);
    }

    @GetMapping("/countByName/{autName}/{result}/{testCaseVersion}/{fromTime}/{toTime}")
    public List<TestCaseCountResponse> countByName(@PathVariable String autName, @PathVariable String result, @PathVariable Integer testCaseVersion, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {

        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

        return executedTestCaseRepository.countByName(autName, result, testCaseVersion, from, to);
    }

    @GetMapping("/countByTime/{autName}/{testCaseVersion}/{fromTime}/{toTime}")
    public List<TestCaseTimeResponse> countByTime(@PathVariable String autName, @PathVariable Integer testCaseVersion, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {

        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return executedTestCaseRepository.countByTime(autName, testCaseVersion, from, to);
    }


}

/*@GetMapping("/test-result/count-by-test-name/{autName}/{result}/{version}/{fromTime}/{toTime}")
    public List<TestNameCountResponse> count(@PathVariable String autName, @PathVariable String result, @PathVariable Integer version, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {

        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return executedTestCaseRepository.findResultCount(autName, result, version, from, to);
    }

    @GetMapping("/test-result/count/{autName}/{result}/{version}/{fromTime}/{toTime}")
    public List<ITestsCountResponse> count2(@PathVariable String autName, @PathVariable String result, @PathVariable Integer version, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {

        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return executedTestCaseRepository.findNameCountttt(autName, result, version, from, to);
    }

    @GetMapping("/test-result/{autName}/{version}/{fromTime}/{toTime}")
    public List<EExecutedTestCase> getResults(@PathVariable String autName, @PathVariable Integer version, @PathVariable ZonedDateTime fromTime, @PathVariable ZonedDateTime toTime) {

        ZonedDateTime from = fromTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime to = toTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        return executedTestCaseRepository.findByAutNameAndTestCaseVersionAndCreatedAtBetweenOrderByCreatedAtDesc(autName, version, from, to);
    }

    @GetMapping("/test-result/aut-name")
    public List<String> getAutNames() {
        return executedTestCaseRepository.findAutNames();
    }

    @GetMapping("/test-result/version")
    public List<String> getResults() {
        return executedTestCaseRepository.findVersions();
    }*/

