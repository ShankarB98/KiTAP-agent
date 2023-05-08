package com.kitap.agent.ui.menuitem.actions;

import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.ui.initializer.AgentFxApplication;
import com.kitap.agent.util.PropertyReaderHelper;
import com.kitap.agent.ui.initializer.TrayIconAndMenuInitializer;
import com.kitap.agent.ui.tray.AgentTrayIcon;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 *Actions performed when clicked on contextMenu Items of TrayIcon
 * @author KT1497
 */
@Slf4j
@Component
public class ContextMenuItemsAction {
    private ApiCalls apiCalls = new ApiCalls();
    private Stage openedStage;

    /**
     * Action performed when register menuitem from context menu is clicked
     * @param stage JavaFX stage for registration
     */
    public void registerMenuItemAction(Stage stage) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Loading the FXML page for Registration");
        URL xmlUrl = getClass().getResource("/menuItems/registerMenu.fxml");
        FXMLLoader loader = new FXMLLoader(xmlUrl);
        Parent root;
        try {
            root = loader.load();
        } catch (IOException ex) {
            log.error(ex.toString());
            throw new RuntimeException(ex);
        }
        Scene scene = new Scene(root);
        stage.setTitle("Registration");
        javafx.scene.image.Image icon = new javafx.scene.image.Image("images/kitapTrayIcon.png");
        stage.getIcons().add((icon));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        openedStage=stage;
        log.info("UI for Registration shown");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Action performed when deRegister menuitem from context menu is clicked
     */
    public void deregisterMenuItemAction() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("trying to deregister agent");
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Deregistration");
        confirmationAlert.setHeaderText("Confirmation Alert");
        confirmationAlert.setContentText("Are you sure ??");
        confirmationAlert.showAndWait().ifPresent((btnType) -> {
            if (btnType == ButtonType.OK) {
                apiCalls.deRegister(PropertyReaderHelper.machineInformation.macAddress);
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                informationAlert.setTitle("Deregistration");
                informationAlert.setContentText("Agent is Deregistered");
                informationAlert.showAndWait();
                log.info("Agent is Deregistered");

                log.info("Updating the menu");
                TrayIconAndMenuInitializer.updateMenu(new Stage());

                //agentTrayIcon.addMenuToTrayIcon();
            } else if (btnType == ButtonType.CANCEL) {
                log.info("Clicked on cancel button and Agent is NOT Deregistered");
            }
        });
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Action performed when execute menuitem from context menu is clicked
     * @param stage JavaFX stage for execution
     */
    public void executeMenuItemAction(Stage stage) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Loading the FXML page for Execution");
        URL xmlUrl = getClass().getResource("/menuItems/executeMenu.fxml");
        FXMLLoader loader = new FXMLLoader(xmlUrl);
        Parent root;
        try {
            root = loader.load();
        } catch (IOException ex) {
            log.error(ex.toString());
            throw new RuntimeException(ex);
        }
        Scene scene = new Scene(root);
        stage.setTitle("Execute Tests");
        javafx.scene.image.Image icon = new javafx.scene.image.Image("images/kitapTrayIcon.png");
        stage.getIcons().add((icon));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        openedStage=stage;
        log.info("UI for execution shown");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Action performed when generate menuitem from context menu is clicked
     * @param stage JavaFX stage for generation
     */
    public void generateMenuItemAction(Stage stage) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Loading the FXML page for Generation");
        URL xmlUrl = getClass().getResource("/menuItems/generateMenu.fxml");
        FXMLLoader loader = new FXMLLoader(xmlUrl);
        Parent root;
        try {
            root = loader.load();
        } catch (IOException ex) {
            log.error(ex.toString());
            throw new RuntimeException(ex);
        }
        Scene scene = new Scene(root);
        stage.setTitle("Generate Tests");
        javafx.scene.image.Image icon = new javafx.scene.image.Image("images/kitapTrayIcon.png");
        stage.getIcons().add((icon));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        openedStage=stage;
        log.info("UI for generation shown");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }


    /**
     * Funtionality when restart MenuItem clicked
     * @param agentTrayIcon agent icon in the system tray
     */
    public void restartAgent(AgentTrayIcon agentTrayIcon) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("agent is restarting");
        if(openedStage!=null){
            openedStage.close();
        }
        agentTrayIcon.removeAgentTrayIconFromTray("Agent is Shutting Down!!", "Please Wait!!", TrayIcon.MessageType.INFO);
        agentTrayIcon.menu.hide();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        AgentFxApplication agentFxApplication = new AgentFxApplication();
        agentFxApplication.start(new Stage());
        log.info("agent restarted");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Functionality when quit MenuItem clicked
     * @param agentTrayIcon agent icon in the system tray
     */
    public void quitAgent(AgentTrayIcon agentTrayIcon) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("agent is quitting");
        agentTrayIcon.removeAgentTrayIconFromTray("Agent is Shutting Down!!", "", TrayIcon.MessageType.NONE);
        System.exit(0);
        log.info("agent stopped");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}