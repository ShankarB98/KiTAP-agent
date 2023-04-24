package com.kitap.agent.ui.initializer;

import com.kitap.agent.KitapAgentApplication;
import com.kitap.agent.ui.tray.ServerCheck;
import com.kitap.agent.util.PropertyReader;
import com.kitap.agent.util.PropertyReaderHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that extends Application class to implement JavaFX features and initializes the SpringBoot Application
 * @author KT1497
 */
@Slf4j
public class AgentFxApplication extends Application {
    private ConfigurableApplicationContext applicationContext;
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
     * Applications may create other stages, if needed, but they will not be primary stages.
     * @param stage the primary stage for this application, onto which the application scene can be set.
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

        log.info("Separate thread executor to check the server status");
        String serverUrl = PropertyReaderHelper.getProperty("server.base.url");
        String serverAddress = serverUrl.substring(7,serverUrl.lastIndexOf(":"));
        int serverPort = Integer.parseInt(serverUrl.substring(serverUrl.lastIndexOf(":")+1,serverUrl.length()-1));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ServerCheck checker = new ServerCheck(serverAddress,serverPort,3000);
        executor.submit(checker);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}