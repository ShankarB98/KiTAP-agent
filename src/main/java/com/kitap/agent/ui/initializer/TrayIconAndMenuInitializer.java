package com.kitap.agent.ui.initializer;

import com.kitap.agent.ui.menuitem.actions.ContextMenuItemsAction;
import com.kitap.agent.ui.tray.AgentTrayIcon;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.awt.*;

/**
 *Initializing the TrayIcon adding and ContextMenu with MenuItem Actions
 * @author KT1497
 */
@Slf4j
@Component
public class TrayIconAndMenuInitializer {

    static TrayIcon oldIconWithMenu; //variable is declared as static to avoid circular dependency error in creating the object
    private static ContextMenuItemsAction contextMenuItemsActionImpl = new ContextMenuItemsAction();//declared as static to use it in static methods

    /**
    * Start of TrayIcon Addition to system Tray and Menu
    * @param stage JavaFX UI stage
    */
    public static void startTrigger(Stage stage) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AgentTrayIcon agentTrayIcon = new AgentTrayIcon();

        log.info("calling method to create and add agentTrayicon");
        oldIconWithMenu = agentTrayIcon.createAndAddAgentTrayIconWithMenuToTray();

        log.info("calling methods for all menuitem actions");
        registerOnAction(stage, agentTrayIcon);
        deRegisterOnAction(agentTrayIcon);
        generateOnAction(stage, agentTrayIcon);
        restartOnAction(agentTrayIcon);
        quitOnAction(agentTrayIcon);
        executeOnAction(stage, agentTrayIcon);
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Removing the old trayicon menu and updating the Trayicon Menu
     * @param stage JavaFX UI stage
     */
    public static void updateMenu(Stage stage) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("Removing the old tray icon after any menuitem action trigger");
        SystemTray.getSystemTray().remove(oldIconWithMenu);

        AgentTrayIcon agentTrayIcon = new AgentTrayIcon();

        log.info("calling method for adding updated menu with trayicon");
        oldIconWithMenu = agentTrayIcon.changeAndAddAgentTrayIconWithMenuToTray();

        log.info("calling methods for all menuitem actions");
        registerOnAction(stage, agentTrayIcon);
        deRegisterOnAction(agentTrayIcon);
        generateOnAction(stage, agentTrayIcon);
        restartOnAction(agentTrayIcon);
        quitOnAction(agentTrayIcon);
        executeOnAction(stage, agentTrayIcon);

        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Action call when Register MenuItem Clicked
     * @param stage JavaFX UI Stage
     * @param agentTrayIcon - tray icon added in system tray
     */
    private static void registerOnAction(Stage stage, AgentTrayIcon agentTrayIcon) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("setting action for register menuitem");
        agentTrayIcon.getRegister().setOnAction(e ->
                contextMenuItemsActionImpl.registerMenuItemAction(stage));
        log.info("completed setting action for register menuitem");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");

    }

    /**
     * Action call when Deregister MenuItem Clicked
     * @param agentTrayIcon - tray icon added in system tray
     */
    private static void deRegisterOnAction(AgentTrayIcon agentTrayIcon) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("setting action for deregister menuitem");
        agentTrayIcon.getDeRegister().setOnAction(e -> contextMenuItemsActionImpl.deregisterMenuItemAction());
        log.info("completed setting action for deregister menuitem");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Action call when Generate Tests MenuItem Clicked
     * @param stage JavaFX UI stage
     * @param agentTrayIcon tray icon added in system tray
     */
    private static void generateOnAction(Stage stage, AgentTrayIcon agentTrayIcon) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("setting action for generate menuitem");
        agentTrayIcon.getGenerateTests().setOnAction(e ->
                contextMenuItemsActionImpl.generateMenuItemAction(stage));
        log.info("completed setting action for generate menuitem");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Action call when Restart MenuItem Clicked
     * @param agentTrayIcon tray icon added in system tray
     */
    private static void restartOnAction(AgentTrayIcon agentTrayIcon) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("setting action for restart menuitem");
        agentTrayIcon.getReStart().setOnAction(e -> contextMenuItemsActionImpl.restartAgent(agentTrayIcon));
        log.info("completed setting action for restart menuitem");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Action call when Quit MenuItem Clicked
     * @param agentTrayIcon tray icon added in system tray
     */
    private static void quitOnAction(AgentTrayIcon agentTrayIcon) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("setting action for quit menuitem");
        agentTrayIcon.getQuit().setOnAction(e -> contextMenuItemsActionImpl.quitAgent(agentTrayIcon));
        log.info("completed setting action for quit menuitem");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Action call when Execute Tests MenuItem Clicked
     * @param stage JavaFX UI stage
     * @param agentTrayIcon tray icon added in system tray
     */
    private static void executeOnAction(Stage stage, AgentTrayIcon agentTrayIcon) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("setting action for execute menuitem");
        agentTrayIcon.getExecuteTests().setOnAction(e -> contextMenuItemsActionImpl.executeMenuItemAction(stage));
        log.info("completed setting action for execute menuitem");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}
