package com.kitap.agent.ui.controllers;

import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.base.BaseClass;
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

/**
 * @Author KT1497
 *
 * @Description Controller class for registerMenu.fxml file,
 * which includes functionality with fxml UI elements
 */
@Slf4j
@Component
public class RegisterMenu {
//    @Autowired
//    ApiCalls apiCalls;
//    @Autowired
//    MachineInformation machineInformation;

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

        AgentTrayIcon agentTrayIcon = new AgentTrayIcon();

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
            agentTrayIcon.addMenuToTrayIcon();
            Stage registrationStage = (Stage) registrationAnchorPane.getScene().getWindow();
            registrationStage.close();
        }
    }
}
