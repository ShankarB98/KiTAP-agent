package com.kitap.agent.ui.initializer;

import com.kitap.agent.KitapAgentApplication;
import com.kitap.agent.util.PropertyReader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StopWatch;

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
    private final PropertyReader reader = new PropertyReader();

    /**
     * Initialize
     */
    @Override
    public void init() {
        log.info("Initializing the springboot application");
        applicationContext = new SpringApplicationBuilder(KitapAgentApplication.class).run();
    }

    /**
     * Close the application Context
     */
    @Override
    public void stop() {
        log.info("Closing the application context");
        applicationContext.close();
        Platform.exit();
        log.info("stopped application context");
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("JavaFX start method");

        log.info("Check KiTAP folder in ProgramData folder of C drive exists or not");
        File file = new File(reader.getProperty("destinationpath"));
        if(!file.exists()){
            try{
                log.info("creating KiTAP folder");
                file.mkdir();
            }catch (Exception e){
                log.error(e.toString());
                throw new RuntimeException(e);
            }
        }
        log.info("KiTAP folder present in ProgramData folder of C drive");
        log.info("Calling method to start addition of trayicon and menu");
        TrayIconAndMenuInitializer.startTrigger(stage);
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

}