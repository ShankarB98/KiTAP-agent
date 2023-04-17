package com.kitap.agent;

import com.kitap.agent.ui.initializer.AgentFxApplication;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

/**
 * SpringBoot Main class which is launching JavaFX Application
 * @author KT1497
 */
@Slf4j
@SpringBootApplication
public class KitapAgentApplication {

	/**
	 * Launches the application
	 * @param args - Application startup arguments
	 */
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
