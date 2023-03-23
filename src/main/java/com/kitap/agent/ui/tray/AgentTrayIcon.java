package com.kitap.agent.ui.tray;

import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.base.BaseClass;
import com.kitap.agent.util.PropertyReader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Objects;

/**
 * @Author: KT1497
 * @Description: TrayIcon adding to System Tray. Adding Context Menu with MenuItems to our TrayIcon.
 */
@Slf4j
@Data
public class AgentTrayIcon {

    private AddEffectsToMenuAndMenuItems icon;

    //Creating ContextMenu and MenuItems
    private MenuItem runStatus;
    private SeparatorMenuItem seperatorLine;
    private MenuItem deRegister;
    private MenuItem register;
    private MenuItem reStart;
    private MenuItem quit;
    private MenuItem generateTests;
    private MenuItem executeTests;
    private MenuItem genOrExe;
    public ContextMenu menu = new ContextMenu();

    /**
     * @Author: KT1497
     * @Description: TrayIcon adding to systemtray,Adding ContextMenu to our TrayIcon
     */
    public void createAndAddAgentTrayIconWithMenuToTray() {

        final javafx.scene.image.Image runningShow = new javafx.scene.image.Image(
                Objects.requireNonNull(AgentTrayIcon.class.getResource("/images/green.png")).toExternalForm());

        try {
            System.setProperty("java.awt.headless", "false");
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        //Checking whether Machine Tray is supported or not
        if (!SystemTray.isSupported()) {
            log.info("system tray is not supported for this PC, please contact admin");
            System.exit(0);
        }
        //create process status displaying in Menu
        runStatus = new javafx.scene.control.MenuItem("Agent is Running...");

        //Create JavaFX MenuItems
        register = new javafx.scene.control.MenuItem("Register");

        genOrExe = new MenuItem("Generate/Execute");

        generateTests = new javafx.scene.control.MenuItem("Generate");

        executeTests = new javafx.scene.control.MenuItem("Execute");

        deRegister = new javafx.scene.control.MenuItem("Deregister");

        reStart = new javafx.scene.control.MenuItem("Restart");

        quit = new MenuItem("Quit");

        //Adding suitable Image beside MenuItem
        runStatus.setGraphic(new ImageView(runningShow));

        //Create Seperator in Context Menu
        seperatorLine = new SeparatorMenuItem();

        Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/kitapTrayIcon.png"));

        icon = new AddEffectsToMenuAndMenuItems(image, menu);
        addMenuToTrayIcon();
        addAgentTrayIconToTray("Agent is Starting", "Please Wait!!", TrayIcon.MessageType.NONE);
    }

    /**
     * @Author: KT1497
     * @Description: Adding ContextMenu to our TrayIcon
     */
    public void addMenuToTrayIcon() {
        ApiCalls apiCalls = new ApiCalls();

        String isServerless = BaseClass.properties.getProperty("isServerLess");
        Boolean serverCheck = Boolean.parseBoolean(isServerless);

        if (serverCheck) {
            menu.getItems().add(runStatus);
            menu.getItems().add(generateTests);
            menu.getItems().add(executeTests);
            menu.getItems().add(reStart);
            menu.getItems().add(seperatorLine);
            menu.getItems().add(quit);
        } else {
            log.info("calling api to know registration status of agent");
            boolean isRegistered = apiCalls.amIRegistered(BaseClass.machineInformation.macAddress);

            if (!isRegistered) {
                //add
                menu.getItems().add(runStatus);
                menu.getItems().add(register);
                menu.getItems().add(reStart);
                menu.getItems().add(seperatorLine);
                menu.getItems().add(quit);
                //remove
                menu.getItems().remove(deRegister);
                menu.getItems().remove(generateTests);
                menu.getItems().remove(executeTests);
            } else {
                //add
                menu.getItems().add(runStatus);
                menu.getItems().add(generateTests);
                menu.getItems().add(executeTests);
                menu.getItems().add(deRegister);
                menu.getItems().add(reStart);
                menu.getItems().add(seperatorLine);
                menu.getItems().add(quit);
                //remove
                menu.getItems().remove(register);
            }
        }


    }

    /**
     * @Author: KT1497
     * @Description: Adding TrayIcon to SystemTray
     * @params: caption is the title of the message popup window, text is any string displaying message,
     * messagetype is the type of message
     */
    public void addAgentTrayIconToTray(String caption, String text, TrayIcon.MessageType messageType) {
        try {
            SystemTray.getSystemTray().add(icon);//Add icon to SystemTray
            icon.setToolTip("KiTAP Agent"); //Displaying Text when hover
            icon.displayMessage(caption, text, messageType);
            log.info("added tray icon to system tray");
        } catch (AWTException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    /**
     * @Author: KT1497
     * @Description: Removing TrayIcon from System Tray
     * @params: caption is the title of the message popup window, text is any string displaying message,
     * messagetype is the type of message
     */
    public void removeAgentTrayIconFromTray(String caption, String text, TrayIcon.MessageType messageType) {
        icon.displayMessage(caption, text, messageType);
        try {
            SystemTray.getSystemTray().remove(icon);//Remove icon from SystemTray
            log.info("removed tray icon from system tray");
        } catch (Exception ex) {
            log.error(ex.toString());
            throw new RuntimeException(ex);
        }
    }
}