package com.kitap.agent;

import com.kitap.agent.ui.initializer.AgentFxApplication;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Author KT1497
 * @Description SpringBoot Main class which is launching JavaFX Application
 */
@Slf4j
@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "KiTAP API", version = "1.0", description = "KiTAP Master and Result"))
public class KitapAgentApplication {
	public static void main(String[] args) {
		log.info("Launching JavaFX Application");
		Application.launch(AgentFxApplication.class, args);
	}

}
