package com.kitap.agent.database.service;


import com.kitap.agent.database.model.ApplicationUnderTest;
import com.kitap.agent.database.model.ExecutedTestCase;
import com.kitap.agent.database.model.ExecutedTestStep;
import com.kitap.testresult.dto.dateandtime.ZonedDateTime;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DtoToEntityConverter {

    /**
     * This class converts provided dto objects to entity object for database operation purpose
     * */

    /**
     * @Description converts executed test case dto into entity object
     * @param tcase - dto object
     * @param details - aut details which need for entity object
     * @return a test case entity
     * */
    public ExecutedTestCase convertDtoToEntity(com.kitap.testresult.dto.ExecutedTestCase tcase, ExecutionAutDetails details) {
        ExecutedTestCase etc = new ExecutedTestCase();
        etc.setTestCaseName(tcase.getTestCaseName());
        etc.setTestCaseVersion(tcase.getTestCaseVersion());
        etc.setTestCaseStartedAt(getZonedDatetime(tcase.getStartedAt()));
        etc.setTestCaseFinishedAt(getZonedDatetime(tcase.getFinishedAt()));
        etc.setResult(tcase.getResult().toString());
        etc.setBrowserName(tcase.getBrowserName());
        etc.setBrowserVersion(tcase.getBrowserVersion());
        etc.setTestCaseVersion(Integer.parseInt(details.getVersion()));
        etc.setDeviceType("windows");
        etc.setOsName("windows");
        etc.setOsVersion("windows 10");
        etc.setAutName(details.getAut());
        etc.setExecutedTestStepList(getExecutedTestSteps(tcase.getTestSteps(), details.getVersion()));
        return etc;
    }

    public void getExecutedTestSteps(List<com.kitap.testresult.dto.ExecutedTestStep> steps, ExecutedTestCase etc) {
        for (com.kitap.testresult.dto.ExecutedTestStep step : steps) {
            ExecutedTestStep ets = new ExecutedTestStep();
            ets.setTestStepName(step.getStepName());
            ets.setTestStartedAt(getZonedDatetime(step.getStartedAt()));
            ets.setTestStepFinishedAt(getZonedDatetime(step.getFinishedAt()));
            ets.setResult(step.getResult().toString());
            ets.setErrorDetails(step.getErrorDetails());
            ets.setScreenSnipPath(step.getScreenSnipPath());
            ets.setTestStepVersion(1);
            //etc.addExecutedTestStep(ets);
        }
    }


    /**
     * @Description converts the step dto to step entity
     * @param steps - list of steps to be converted
     * @param version - version to use reference in steps
     * @return a list of step entities
     * */
    public List<ExecutedTestStep> getExecutedTestSteps(List<com.kitap.testresult.dto.ExecutedTestStep> steps, String version) {
        List<ExecutedTestStep> list = new ArrayList<>();
        for (com.kitap.testresult.dto.ExecutedTestStep step : steps) {
            ExecutedTestStep ets = new ExecutedTestStep();
            ets.setTestStepName(step.getStepName());
            ets.setTestStartedAt(getZonedDatetime(step.getStartedAt()));
            ets.setTestStepFinishedAt(getZonedDatetime(step.getFinishedAt()));
            ets.setResult(step.getResult().toString());
            ets.setErrorDetails(step.getErrorDetails());
            ets.setScreenSnipPath(step.getScreenSnipPath());
            ets.setTestStepVersion(Integer.parseInt(version));
            list.add(ets);
        }
        return list;
    }


    /**
     * @Description converts own zoned date time object to Java's zoned date time object
     * @param zonedDateTimeObject - our own zoned date time object
     * @return a java's zoned date time object
     * */
    public java.time.ZonedDateTime getZonedDatetime(ZonedDateTime zonedDateTimeObject) {
        //System.out.println("2016-10-05T08:20:10+05:30[Asia/Kolkata]");
        return java.time.ZonedDateTime.parse(zonedDateTimeObject.toString());
    }


    /**
     * @Description used to convert aut dto to aut entity
     * @param autdto - aut dto object
     * @return a aut entity
     * */
    public ApplicationUnderTest convertDtoToEntity(com.kitap.testresult.dto.ApplicationUnderTest autdto) {
        ApplicationUnderTest aut = new ApplicationUnderTest();
        aut.setName(autdto.getName());
        aut.setDisplayName(autdto.getDisplayName());
        aut.setDescription(autdto.getDescription());
        aut.setUrl(autdto.getUrl());
        aut.setExecutableFilePath(autdto.getExecutableFilePath());
        aut.setType(autdto.getType());
        aut.setVersion(autdto.getVersion());
//        aut.setCreatedBy(ad.getCreatedBy());
//        aut.setCreatedAt(ad.getCreatedAt());
//        aut.setModifiedBy(ad.getModifiedBy());
//        aut.setModifiedAt(ad.getModifiedAt());
        aut.setIsActive(autdto.getIsActive());
        return aut;
    }
}
