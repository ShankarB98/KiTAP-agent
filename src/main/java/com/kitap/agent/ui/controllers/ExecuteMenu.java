package com.kitap.agent.ui.controllers;

import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.generate.util.FileOperations;
import com.kitap.agent.ui.tray.AddEffectsToMenuAndMenuItems;
import com.kitap.agent.ui.tray.AgentTrayIcon;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * @Author KT1497
 * @Description Controller class for executeMenu.fxml file,
 * which includes functionality with fxml UI elements
 */
@Slf4j
@Component
public class ExecuteMenu {
    final javafx.scene.image.Image executingColour = new javafx.scene.image.Image(
            Objects.requireNonNull(AgentTrayIcon.class.getResource("/images/yellow.png")).toExternalForm());
    final javafx.scene.image.Image agentRunningColour = new javafx.scene.image.Image(
            Objects.requireNonNull(AgentTrayIcon.class.getResource("/images/green.png")).toExternalForm());

    ApiCalls apiCalls = new ApiCalls();

    final FileOperations operations = new FileOperations();

    @FXML
    public Label blinkLabel;
    @FXML
    public ProgressIndicator executeTestsProgressIndicator;
    @FXML
    public AnchorPane executeTestsAnchorPane;
    @FXML
    public ComboBox<String> versionCombo;
    @FXML
    public Button cancelButton;
    @FXML
    public Button executeTestButton;
    @FXML
    public Button viewTestResults;

    @FXML
    public ComboBox<String> executeAutCombo;

    @FXML
    public ComboBox<String> autType;


    @FXML
    public void initialize() {
        autType.getItems().removeAll(autType.getItems());
        autType.getItems().addAll(apiCalls.getAutTypes());
    }

    /**
     * Function performed when ViewTestResults Button is clicked in JavaFX UI
     *
     * @param actionEvent JavaFX UI ViewTestResults Button Click
     */
    @FXML
    public void displayTestResults(ActionEvent actionEvent) {

    }

    /**
     * Function performed when ExecuteTests Button is clicked in JavaFX UI
     *
     * @param actionEvent JavaFX UI ExecuteTests Button Click
     */
    @FXML
    public void testExecution(ActionEvent actionEvent) {
        new Thread() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        //Giving ExecutingTests Status and disabling contextmenu Items
                        AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setGraphic(new ImageView(executingColour));
                        AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setText("Executing Tests");
                        for (int i = 1; i <= AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().size() - 1; i++) {
                            AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(i).setDisable(true);
                        }

                        //Progress Indicator
                        executeTestsProgressIndicator.setVisible(true);

                        //Blinking Text
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), blinkLabel);
                        fadeTransition.setFromValue(1.0);
                        fadeTransition.setToValue(0.0);
                        fadeTransition.setCycleCount(Animation.INDEFINITE);
                        fadeTransition.play();
                        blinkLabel.setVisible(true);

                        //Disabling all the buttons in UI Page
                        executeTestButton.setDisable(true);
                        viewTestResults.setDisable(true);
                        cancelButton.setDisable(true);
                    }
                });

                ExecutionAutDetails details = new ExecutionAutDetails();
                details.setTestType(autType.getValue());
                details.setAut(executeAutCombo.getValue());
                details.setVersion(versionCombo.getValue());
                details.setTestCases(null);
                apiCalls.executeTests(details);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //Enabling all the contextmenu Items
                        executeTestsProgressIndicator.setVisible(false); //stopping progressIndicator
                        blinkLabel.setVisible(false); // stopping Blinking Text

                        //Closing Stage after process completion
                        Stage executeStage = (Stage) executeTestsAnchorPane.getScene().getWindow();
                        executeStage.close();


                        //Enabling all the contextmenu Items
                        AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setGraphic(new ImageView(agentRunningColour));
                        AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setText("Agent is running");
                        for (int i = 1; i <= AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().size() - 1; i++) {
                            AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(i).setDisable(false);
                        }
                    }
                });

            }
        }.start();
    }

            /**
             * Function performed when Cancel Button is clicked in JavaFX UI
             *
             * @param actionEvent JavaFX UI Cancel Button Click
             */
            @FXML
            public void cancelClicked(ActionEvent actionEvent) {
                Stage executeStage = (Stage) executeTestsAnchorPane.getScene().getWindow();
                executeStage.close();
                log.info("Clicked Cancel Button");
            }

            public void onChangeOfAutType(ActionEvent actionEvent) {
                executeAutCombo.getItems().removeAll(executeAutCombo.getItems());
                executeAutCombo.getItems().addAll(apiCalls.getAllAUT(autType.getValue()));
            }

            public void onAutSelection(ActionEvent actionEvent) {
                versionCombo.getItems().removeAll(versionCombo.getItems());
                versionCombo.getItems().addAll(operations.getListOfFolders(autType.getValue() + File.separator + executeAutCombo.getValue()));
            }
        }
