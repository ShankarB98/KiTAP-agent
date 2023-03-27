package com.kitap.agent.ui.initializer;

import com.kitap.agent.ui.menuitem.actions.ContextMenuItemsAction;
import com.kitap.agent.ui.tray.AgentTrayIcon;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @Author: KT1497
 *
 * @Description: Initializing the TrayIcon adding and ContextMenu with MenuItem Actions
 */
@Slf4j
@Component
public class TrayIconAndMenuInitializer {

    static ContextMenuItemsAction contextMenuItemsActionImpl = new ContextMenuItemsAction();

    /**
    * Start of TrayIcon Addition to system Tray and Menu
    *
    * @param stage JavaFX UI stage
    */
    public static void startTrigger(Stage stage) {

        AgentTrayIcon agentTrayIcon = new AgentTrayIcon();

        agentTrayIcon.createAndAddAgentTrayIconWithMenuToTray();

        registerOnAction(stage, agentTrayIcon);
        deRegisterOnAction(agentTrayIcon);
        generateOnAction(stage, agentTrayIcon);
        restartOnAction(agentTrayIcon);
        quitOnAction(agentTrayIcon);
        executeTestsOnAction(stage, agentTrayIcon);
        generateOrExecute(stage, agentTrayIcon);
    }

    /**
     * Action call when Register MenuItem Clicked
     *
     * @param stage JavaFX UI Stage
     * @param agentTrayIcon
     */
    private static void registerOnAction(Stage stage, AgentTrayIcon agentTrayIcon) {
        agentTrayIcon.getRegister().setOnAction(e ->
                contextMenuItemsActionImpl.registerMenuItemAction(stage));
    }

    /**
     * Action call when Deregister MenuItem Clicked
     *
     * @param agentTrayIcon
     */
    private static void deRegisterOnAction(AgentTrayIcon agentTrayIcon) {
        agentTrayIcon.getDeRegister().setOnAction(e -> contextMenuItemsActionImpl.deregisterMenuItemAction());
    }

    /**
     * Action call when Generate Tests MenuItem Clicked
     *
     * @param stage JavaFX UI stage
     * @param agentTrayIcon
     */
    private static void generateOnAction(Stage stage, AgentTrayIcon agentTrayIcon) {
        agentTrayIcon.getGenerateTests().setOnAction(e -> contextMenuItemsActionImpl.generateMenuItemAction(stage));
    }

    /**
     * Action call when Restart MenuItem Clicked
     *
     * @param agentTrayIcon
     */
    private static void restartOnAction(AgentTrayIcon agentTrayIcon) {
        agentTrayIcon.getReStart().setOnAction(e -> restartAgent(agentTrayIcon));
    }

    /**
     * Action call when Quit MenuItem Clicked
     *
     * @param agentTrayIcon
     */
    private static void quitOnAction(AgentTrayIcon agentTrayIcon) {
        agentTrayIcon.getQuit().setOnAction(e -> quitAgent(agentTrayIcon));
    }

    /**
     * Action call when Execute Tests MenuItem Clicked
     *
     * @param stage JavaFX UI stage
     * @param agentTrayIcon
     */
    private static void executeTestsOnAction(Stage stage, AgentTrayIcon agentTrayIcon) {
        agentTrayIcon.getExecuteTests().setOnAction(e -> contextMenuItemsActionImpl.executeMenuItemAction(stage));
    }

    /**
     * Action call when Generate/Execute MenuItem Clicked
     *
     * @param stage JavaFX UI stage
     * @param agentTrayIcon
     */
    private static void generateOrExecute(Stage stage, AgentTrayIcon agentTrayIcon) {
        agentTrayIcon.getGenOrExe().setOnAction(e -> contextMenuItemsActionImpl.genExeMenuItemAction(stage));
    }

    /**
     * Funtionality when restart MenuItem clicked
     *
     * @param agentTrayIcon
     */
    private static void restartAgent(AgentTrayIcon agentTrayIcon) {
        log.info("agent is restarting");
        agentTrayIcon.removeAgentTrayIconFromTray("Agent is Shutting Down!!", "Please Wait!!", TrayIcon.MessageType.INFO);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        agentTrayIcon.addMenuToTrayIcon();
        agentTrayIcon.addAgentTrayIconToTray("Agent is Starting", "Please Wait!!", TrayIcon.MessageType.NONE);
    }

    /**
     * Functionality when quit MenuItem clicked
     *
     * @param agentTrayIcon
     */
    private static void quitAgent(AgentTrayIcon agentTrayIcon) {
        agentTrayIcon.removeAgentTrayIconFromTray("Agent is Shutting Down!!", "", TrayIcon.MessageType.NONE);
        log.info("calling api to inform agent is shutting down");
        //apiCalls.quit(registrationService.getMacAddress());
        System.exit(0);
    }

}
