package com.kitap.agent.ui.initializer;

import com.kitap.agent.KitapAgentApplication;
import com.kitap.agent.util.PropertyReader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.webdriver.DriverSource;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;

/**
 * @Author KT1497
 *
 * @Description Class that extends Application class to implement JavaFX features and
 * initializes the SpringBoot Application
 */
@Slf4j
public class AgentFxApplication extends Application {
    public ConfigurableApplicationContext applicationContext;

    private PropertyReader reader = new PropertyReader();

    /**
     * Initialize
     */
    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(KitapAgentApplication.class).run();
    }

    /**
     * Close the application Context
     */
    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    /**
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {
        File file = new File(reader.getProperty("destinationpath"));
        if(!file.exists()){
            try{
                file.mkdir();
            }catch (Exception e){
                log.error(e.toString());
                throw new RuntimeException(e);
            }
        }
        TrayIconAndMenuInitializer.startTrigger(stage);
    }

}