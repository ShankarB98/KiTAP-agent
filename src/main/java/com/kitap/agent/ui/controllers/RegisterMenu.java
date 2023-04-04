package com.kitap.agent.ui.controllers;

import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.base.BaseClass;
import com.kitap.agent.ui.initializer.TrayIconAndMenuInitializer;
import com.kitap.agent.ui.tray.AgentTrayIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @Author KT1497
 *
 * @Description Controller class for registerMenu.fxml file,
 * which includes functionality with fxml UI elements
 */
@Slf4j
@Component
public class RegisterMenu {
    ApiCalls apiCalls = new ApiCalls();
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField keyTextField;
    @FXML
    public Button registerButton;
    @FXML
    public AnchorPane registrationAnchorPane;

    /**
     * Function performed when Register Button is clicked in JavaFX UI
     *
     * @param actionEvent JavaFX UI Register Button Click
     */
    @FXML
    public void registerAgent(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("clicked register button from Register UI");

        String agentName = nameTextField.getText();
        log.info("Agent Name : " + agentName);
        String agentKey = keyTextField.getText();
        log.info("Agent Key : " + agentKey);
        int length = agentKey.length();
        if (length != 36) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Key");
            alert.setContentText("Invalid Key");
            alert.showAndWait();
            log.info("invalid key #" + agentKey);
        } else {
            log.info("calling api to register agent");
            apiCalls.register(BaseClass.machineInformation.getAgentDto(agentName), agentKey);

            log.info("Updating the menu");
            TrayIconAndMenuInitializer.updateMenu(new Stage());

            Stage registrationStage = (Stage) registrationAnchorPane.getScene().getWindow();
            registrationStage.close();
            log.info("Closed the Registration UI");
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
        }
    }
}
