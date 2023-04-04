package com.kitap.agent.ui.tray;

import com.kitap.agent.ui.util.PlatformUtil;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
/**
 * @Author: KT1497
 *
 * @Description: Creating JavaFX ContextMenu with CSS Styling along with MenuItems
 */
@Slf4j
public class AddEffectsToMenuAndMenuItems extends TrayIcon {
    private static Stage iconStage;
    public static javafx.scene.control.Button button;
    private ContextMenu contextMenu;

    //CSS effects to context Menu and MenuItems
    private static final String STYLE_SHEET = Objects.requireNonNull(AgentTrayIcon.class.getResource("/tray-style.css")).toExternalForm();
    private static final String CONTEXT_MENU_STYLE_CLASS = "tray-context";
    private static final String MENU_ITEMS_STYLE_CLASS = "tray-context-menu-item";
    private static final String FIRST_STYLE_CLASS = "tray-context-menu-item-first";
    private static final String LAST_STYLE_CLASS = "tray-context-menu-item-last";

    /**
     * @param image       AWT Image. Not a JavaFX Image
     * @param contextMenu JavaFX ContextMenu
     */
    public AddEffectsToMenuAndMenuItems(Image image, ContextMenu contextMenu) {
        super(image);
        this.contextMenu = contextMenu;
        init();
    }

    /**
     * Initialize
     */
    private void init() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Initializing the menu and adding CSS styles");
        setImageAutoSize(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Platform.runLater(() -> {
                        iconStage = new Stage();
                        iconStage.initStyle(StageStyle.UTILITY);
                        iconStage.setMaxHeight(0);
                        iconStage.setMaxWidth(0);
                        iconStage.setX(Double.MAX_VALUE);
                        button = new Button();
                        button.setContextMenu(contextMenu);
                        Scene scene = new Scene(button);
                        scene.getStylesheets().add(STYLE_SHEET);
                        scene.setFill(Color.TRANSPARENT);
                        iconStage.setScene(scene);
                        contextMenu.getStyleClass().add(CONTEXT_MENU_STYLE_CLASS);
                        //Add StyleClass to all MenuItems in the ContextMenu
                        contextMenu.getItems().forEach(menuItem -> menuItem.getStyleClass().add(MENU_ITEMS_STYLE_CLASS));
                        //Check the First and Last Menuitem to add an extra StyleClass
                        if (contextMenu.getItems().size() > 1) {
                            contextMenu.getItems().get(0).getStyleClass().add(FIRST_STYLE_CLASS);
                            contextMenu.getItems().get(contextMenu.getItems().size() - 1).getStyleClass().add(LAST_STYLE_CLASS);
                        }
                        iconStage.show();
                        contextMenu.show(iconStage);
                        contextMenu.setAutoHide(true);

                        //specify the location of context menu based on Platform(Windows/Linux/Mac)
                        //contextMenu.setX(e.getX()-contextMenu.getX());
                        contextMenu.setX(e.getX());
                        if (PlatformUtil.isMac()) {
                            contextMenu.setY(e.getY());
                        } else {
                            contextMenu.setY(e.getY() - contextMenu.getHeight());
                        }
                    });
                }
            }
        });
        log.info("Added CSS styling to trayicon menu");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}

