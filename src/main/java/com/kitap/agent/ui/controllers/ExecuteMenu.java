package com.kitap.agent.ui.controllers;

import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.generate.util.FileOperations;
import com.kitap.agent.ui.tray.AddEffectsToMenuAndMenuItems;
import com.kitap.agent.ui.tray.AgentTrayIcon;
import com.kitap.agent.util.PropertyReader;
import com.kitap.testresult.dto.execute.ExecutionAutDetails;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    PropertyReader reader = new PropertyReader();
    String separator = File.separator;

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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("method initializing the execution UI");
        autType.getItems().removeAll(autType.getItems());
        autType.getItems().addAll(apiCalls.getAutTypes());
        log.info("method completed by updating AutTypes from api call in execution UI");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Function performed when ViewTestResults Button is clicked in JavaFX UI
     *
     * @param actionEvent JavaFX UI ViewTestResults Button Click
     */
    @FXML
    public void displayTestResults(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("clicked on viewtestresults button from execution UI");
        if(autType.getValue()!=null&&executeAutCombo.getValue()!=null&&versionCombo.getValue()!=null) {
            try {
                String reportsPath = reader.getProperty("destinationpath") + separator +
                        autType.getValue() + separator + executeAutCombo.getValue() + separator +
                        versionCombo.getValue() + separator + "target" + separator + "site" + separator + "serenity" + separator + "index.html";
                File file = new File(reportsPath);
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException e) {
                log.error(e.toString());
                throw new RuntimeException(e);
            }
            log.info("displaying test results completed");
        }
        else{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    log.error("Field not selected");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Field not selected");
                    alert.setContentText("Please select all the fields");
                    alert.showAndWait();
                }
            });
        }
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Function performed when ExecuteTests Button is clicked in JavaFX UI
     *
     * @param actionEvent JavaFX UI ExecuteTests Button Click
     */
    @FXML
    public void testExecution(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("clicked on executetests button from execution UI");
        new Thread() {
            public void run() {
                if(autType.getValue()!=null&&executeAutCombo.getValue()!=null&&versionCombo.getValue()!=null) {
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
                    log.info("api call to execute tests");
                    apiCalls.executeTests(details);

                    log.info("testExecution method completed");
                    stopWatch.stop();
                    log.info("Execution time for " + new Object() {
                    }.getClass().getEnclosingMethod().getName() +
                            " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //Enabling all the contextmenu Items
                            executeTestsProgressIndicator.setVisible(false); //stopping progressIndicator
                            blinkLabel.setVisible(false); // stopping Blinking Text

                            //Closing Stage after process completion
                            //Stage executeStage = (Stage) executeTestsAnchorPane.getScene().getWindow();
                            //executeStage.close();

                            /**
                             * Enabling all the buttons in UI
                             * */
                            executeTestButton.setDisable(false);
                            viewTestResults.setDisable(false);
                            cancelButton.setDisable(false);


                            //Enabling all the contextmenu Items
                            AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setGraphic(new ImageView(agentRunningColour));
                            AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setText("Agent is running");
                            for (int i = 1; i <= AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().size() - 1; i++) {
                                AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(i).setDisable(false);
                            }
                        }
                    });
                }
                else{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            log.error("Field not selected");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Field not selected");
                            alert.setContentText("Please select all the fields");
                            alert.showAndWait();
                        }
                    });
                }
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("clicked on cancel button from execution UI");
        Stage executeStage = (Stage) executeTestsAnchorPane.getScene().getWindow();
        executeStage.close();
        log.info("closed the execution UI");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    public void onChangeOfAutType(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("on change of autType,updating the auts present");
        executeAutCombo.getItems().removeAll(executeAutCombo.getItems());
        executeAutCombo.getItems().addAll(apiCalls.getAllAUT(autType.getValue()));
        executeAutCombo.getItems().remove("");
        log.info("method onChangeOfAurType completed");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    public void onAutSelection(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("on change of aut name selection, updating the versions present");
        versionCombo.getItems().removeAll(versionCombo.getItems());
        versionCombo.getItems().addAll(operations.getListOfFolders(autType.getValue() + File.separator + executeAutCombo.getValue()));
        log.info("method onAutSelection completed");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }
}
