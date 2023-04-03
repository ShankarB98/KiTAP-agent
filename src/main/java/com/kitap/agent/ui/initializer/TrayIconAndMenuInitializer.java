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

    static TrayIcon oldIconWithMenu;
    static ContextMenuItemsAction contextMenuItemsActionImpl = new ContextMenuItemsAction();

    /**
    * Start of TrayIcon Addition to system Tray and Menu
    *
    * @param stage JavaFX UI stage
    */
    public static void startTrigger(Stage stage) {
        AgentTrayIcon agentTrayIcon = new AgentTrayIcon();

        log.info("calling method to create and add agenttrayicon");
        oldIconWithMenu = agentTrayIcon.createAndAddAgentTrayIconWithMenuToTray();

        log.info("calling methods for all menuitem actions");
        registerOnAction(stage, agentTrayIcon);
        deRegisterOnAction(agentTrayIcon);
        generateOnAction(stage, agentTrayIcon);
        restartOnAction(agentTrayIcon);
        quitOnAction(agentTrayIcon);
        executeOnAction(stage, agentTrayIcon);
    }
    public static void updateMenu(Stage stage) {
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
    }

    /**
     * Action call when Register MenuItem Clicked
     *
     * @param stage JavaFX UI Stage
     * @param agentTrayIcon
     */
    private static void registerOnAction(Stage stage, AgentTrayIcon agentTrayIcon) {
        log.info("setting action for register menuitem");
        agentTrayIcon.getRegister().setOnAction(e ->
                contextMenuItemsActionImpl.registerMenuItemAction(stage));
    }

    /**
     * Action call when Deregister MenuItem Clicked
     *
     * @param agentTrayIcon
     */
    private static void deRegisterOnAction(AgentTrayIcon agentTrayIcon) {
        log.info("setting action for deregister menuitem");
        agentTrayIcon.getDeRegister().setOnAction(e -> contextMenuItemsActionImpl.deregisterMenuItemAction());
    }

    /**
     * Action call when Generate Tests MenuItem Clicked
     *
     * @param stage JavaFX UI stage
     * @param agentTrayIcon
     */
    private static void generateOnAction(Stage stage, AgentTrayIcon agentTrayIcon) {
        log.info("setting action for generate menuitem");
        agentTrayIcon.getGenerateTests().setOnAction(e -> contextMenuItemsActionImpl.generateMenuItemAction(stage));
    }

    /**
     * Action call when Restart MenuItem Clicked
     *
     * @param agentTrayIcon
     */
    private static void restartOnAction(AgentTrayIcon agentTrayIcon) {
        log.info("setting action for restart menuitem");
        agentTrayIcon.getReStart().setOnAction(e -> contextMenuItemsActionImpl.restartAgent(agentTrayIcon));
    }

    /**
     * Action call when Quit MenuItem Clicked
     *
     * @param agentTrayIcon
     */
    private static void quitOnAction(AgentTrayIcon agentTrayIcon) {
        log.info("setting action for quit menuitem");
        agentTrayIcon.getQuit().setOnAction(e -> contextMenuItemsActionImpl.quitAgent(agentTrayIcon));
    }

    /**
     * Action call when Execute Tests MenuItem Clicked
     *
     * @param stage JavaFX UI stage
     * @param agentTrayIcon
     */
    private static void executeOnAction(Stage stage, AgentTrayIcon agentTrayIcon) {
        log.info("setting action for execute menuitem");
        agentTrayIcon.getExecuteTests().setOnAction(e -> contextMenuItemsActionImpl.executeMenuItemAction(stage));
    }


}
