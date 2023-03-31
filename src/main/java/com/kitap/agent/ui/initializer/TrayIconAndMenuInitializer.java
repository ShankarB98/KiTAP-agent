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

        oldIconWithMenu = agentTrayIcon.createAndAddAgentTrayIconWithMenuToTray();

        registerOnAction(stage, agentTrayIcon);
        deRegisterOnAction(agentTrayIcon);
        generateOnAction(stage, agentTrayIcon);
        restartOnAction(agentTrayIcon);
        quitOnAction(agentTrayIcon);
        executeTestsOnAction(stage, agentTrayIcon);
    }
    public static void updateMenu(Stage stage) {
        SystemTray.getSystemTray().remove(oldIconWithMenu);

        AgentTrayIcon agentTrayIcon = new AgentTrayIcon();

        oldIconWithMenu = agentTrayIcon.changeAndAddAgentTrayIconWithMenuToTray();

        registerOnAction(stage, agentTrayIcon);
        deRegisterOnAction(agentTrayIcon);
        generateOnAction(stage, agentTrayIcon);
        restartOnAction(agentTrayIcon);
        quitOnAction(agentTrayIcon);
        executeTestsOnAction(stage, agentTrayIcon);
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
        agentTrayIcon.getReStart().setOnAction(e -> contextMenuItemsActionImpl.restartAgent(agentTrayIcon));
    }

    /**
     * Action call when Quit MenuItem Clicked
     *
     * @param agentTrayIcon
     */
    private static void quitOnAction(AgentTrayIcon agentTrayIcon) {
        agentTrayIcon.getQuit().setOnAction(e -> contextMenuItemsActionImpl.quitAgent(agentTrayIcon));
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


}
