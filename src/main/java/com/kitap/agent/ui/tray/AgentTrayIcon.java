package com.kitap.agent.ui.tray;

import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.util.PropertyReaderHelper;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.awt.*;
import java.util.Objects;

/**
 * TrayIcon adding to System Tray. Adding Context Menu with MenuItems to our TrayIcon.
 * @author KT1497
 */
@Slf4j
@Data
public class AgentTrayIcon {

    private AddEffectsToMenuAndMenuItems icon;

    //Creating ContextMenu and MenuItems
    private MenuItem runStatus = new javafx.scene.control.MenuItem("Agent is Running...");
    private SeparatorMenuItem seperatorLine = new SeparatorMenuItem();
    private MenuItem deRegister = new javafx.scene.control.MenuItem("Deregister");
    private MenuItem register = new javafx.scene.control.MenuItem("Register");
    private MenuItem reStart = new javafx.scene.control.MenuItem("Restart");
    private MenuItem quit = new MenuItem("Quit");
    private MenuItem generateTests = new javafx.scene.control.MenuItem("Generate");
    private MenuItem executeTests = new javafx.scene.control.MenuItem("Execute");
    public ContextMenu menu = new ContextMenu();

    /**
     * TrayIcon adding to systemtray,Adding ContextMenu to our TrayIcon
     * @return icon in the system tray is returned
     */
    public TrayIcon createAndAddAgentTrayIconWithMenuToTray() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Creating and adding Agent trayicon along with menu");
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
            log.error("system tray is not supported for this PC, please contact admin");
            System.exit(0);
        }
        //Adding suitable Image beside MenuItem
        runStatus.setGraphic(new ImageView(runningShow));

        Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/kitapTrayIcon.png"));

        icon = new AddEffectsToMenuAndMenuItems(image, menu);
        addMenuToTrayIcon();
        addAgentTrayIconToTray("Agent is Starting", "Please Wait!!", TrayIcon.MessageType.NONE);
        log.info("Added Trayicon along with menu to system tray and returning that icon");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return icon;
    }

    /**
     * Changing the menu and adding the trayicon to system tray
     * @return icon in the system tray is returned
     */
    public TrayIcon changeAndAddAgentTrayIconWithMenuToTray() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Updating and adding Agent trayicon along with menu");
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
            log.error("system tray is not supported for this PC, please contact admin");
            System.exit(0);
        }

        //Adding suitable Image beside MenuItem
        runStatus.setGraphic(new ImageView(runningShow));

        Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/kitapTrayIcon.png"));
        icon = new AddEffectsToMenuAndMenuItems(image,menu);
        try {
            SystemTray.getSystemTray().add(icon);//Add icon to SystemTray
            icon.setToolTip("KiTAP Agent");
        } catch (AWTException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        addMenuToTrayIcon();
        log.info("Added Trayicon along with updated menu to system tray and returning that icon");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        return icon;
    }

    /**
     * Adding ContextMenu to our TrayIcon
     */
    public void addMenuToTrayIcon() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Adding menu to trayicon");
        ApiCalls apiCalls = new ApiCalls();

        String serverCheck = PropertyReaderHelper.getProperty("isServerLess");
        Boolean serverLess = Boolean.parseBoolean(serverCheck);

        if (serverLess) {
            log.info("Menu for serverless is adding");
            menu.getItems().add(runStatus);
            menu.getItems().add(generateTests);
            menu.getItems().add(executeTests);
            menu.getItems().add(reStart);
            menu.getItems().add(seperatorLine);
            menu.getItems().add(quit);
        } else {
            log.info("Menu for server is adding");
            log.info("calling api to know registration status of agent");
            boolean isRegistered = apiCalls.amIRegistered(PropertyReaderHelper.machineInformation.macAddress);
            log.info("Registration status of agent is {}", isRegistered);
            if (!isRegistered) {
                log.info("Menu if agent is - Not Registered");
                //remove
                menu.getItems().remove(deRegister);
                //add
                menu.getItems().add(runStatus);
                menu.getItems().add(register);
                menu.getItems().add(generateTests);
                menu.getItems().add(executeTests);
                menu.getItems().add(reStart);
                menu.getItems().add(seperatorLine);
                menu.getItems().add(quit);
            } else {
                log.info("Menu if agent is - Registered");
                //remove
                menu.getItems().remove(register);
                //add
                menu.getItems().add(runStatus);
                menu.getItems().add(generateTests);
                menu.getItems().add(executeTests);
                menu.getItems().add(deRegister);
                menu.getItems().add(reStart);
                menu.getItems().add(seperatorLine);
                menu.getItems().add(quit);
            }
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Adding TrayIcon to SystemTray
     * @param caption is the title of the message popup window
     * @param text is any string displaying message,
     * @param messageType is the type of message
     */
    public void addAgentTrayIconToTray(String caption, String text, TrayIcon.MessageType messageType) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Trayicon adding with a popup message");
        try {
            SystemTray.getSystemTray().add(icon);//Add icon to SystemTray
            icon.setToolTip("KiTAP Agent"); //Displaying Text when hover
            icon.displayMessage(caption, text, messageType);
            log.info("added tray icon to system tray");
        } catch (AWTException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Removing TrayIcon from System Tray
     * @param caption is the title of the message popup window
     * @param text is any string displaying message,
     * @param messageType is the type of message
     */
    public void removeAgentTrayIconFromTray(String caption, String text, TrayIcon.MessageType messageType) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("TrayIcon removing with a popup message");
        icon.displayMessage(caption, text, messageType);
        try {
            SystemTray.getSystemTray().remove(icon);//Remove icon from SystemTray
            log.info("removed tray icon from system tray");
        } catch (Exception ex) {
            log.error(ex.toString());
            throw new RuntimeException(ex);
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}