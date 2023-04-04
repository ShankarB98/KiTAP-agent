package com.kitap.agent;

import com.kitap.agent.ui.initializer.AgentFxApplication;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

/**
 * @Author KT1497
 * @Description SpringBoot Main class which is launching JavaFX Application
 */
@Slf4j
@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "KiTAP API", version = "1.0", description = "KiTAP Master and Result"))
public class KitapAgentApplication {
	public static void main(String[] args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		log.info("starting main method by launching JavaFX Application");
		Application.launch(AgentFxApplication.class, args);
		log.info("completed main method execution");
		stopWatch.stop();
		log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
				" method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
	}

}
