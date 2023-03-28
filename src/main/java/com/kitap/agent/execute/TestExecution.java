package com.kitap.agent.execute;

import com.kitap.agent.base.BaseClass;
import com.kitap.testresult.adapter.ConvertedResult;
import com.kitap.testresult.dto.ExecutedTestCase;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Properties;

@Slf4j
public class TestExecution {

    /**
     * Executes the test cases and returns the processed output to save in database
     * */

    final boolean singleOrMultiCase = true;
    final String separator = File.separator;
    final Properties properties = BaseClass.properties;

    final ExecutionHelper helper = new ExecutionHelper();
}
