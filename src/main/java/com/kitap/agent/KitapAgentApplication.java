package com.kitap.agent;

import com.kitap.agent.base.BaseClass;
import com.kitap.agent.ui.initializer.AgentFxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "KiTAP API", version = "1.0", description = "KiTAP Master and Result"))
public class KitapAgentApplication {
	public static void main(String[] args) {
		Application.launch(AgentFxApplication.class, args);
		//SpringApplication.run(KitapAgentApplication.class, args);
	}

}
