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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.ToggleSwitch;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
enum ValidBrowsers{
    CHROME,
    EDGE,
    FIREFOX,
    SAFARI
}
/**
 * Controller class for executeMenu.fxml file which includes functionality with fxml UI elements
 * @author KT1497
 */
@Slf4j
@Component
public class ExecuteMenu {
    private final javafx.scene.image.Image executingColour = new javafx.scene.image.Image(
            Objects.requireNonNull(AgentTrayIcon.class.getResource("/images/yellow.png")).toExternalForm());
    private final javafx.scene.image.Image agentRunningColour = new javafx.scene.image.Image(
            Objects.requireNonNull(AgentTrayIcon.class.getResource("/images/green.png")).toExternalForm());

    private ApiCalls apiCalls = new ApiCalls();
    private PropertyReader reader = new PropertyReader();
    private String separator = File.separator;
    private FileOperations operations = new FileOperations();
    @FXML
    private Label blinkLabel;
    @FXML
    private ProgressIndicator executeTestsProgressIndicator;
    @FXML
    private AnchorPane executeTestsAnchorPane;
    @FXML
    private ComboBox<String> versionCombo;
    @FXML
    private Button cancelButton;
    @FXML
    private Button executeTestButton;
    @FXML
    private Button viewTestResults;
    @FXML
    private ComboBox<String> executeAutCombo;
    @FXML
    private ComboBox<String> autType;

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
    public CheckComboBox<String> browserBox;
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
    public ToggleSwitch toggleSwitch;

    /**
     * Validating the configured browsers and getting them to add in CheckComboBox of execution UI
     *
     * @return returns array of browsers if validation is success else return empty array
     */
    private ArrayList<String> checkAndGetBrowsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("checking the browsers entered from config file and getting them if validation successful");
        ArrayList<String> validatedBrowsers = new ArrayList<>();
        String supportedBrowsers = reader.getProperty("supportedBrowsers");
         if (null == supportedBrowsers || supportedBrowsers.trim().isEmpty()) {
             log.warn("Invalid supportedBrowsers config value.");
             stopWatch.stop();
             log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                     " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
             return new ArrayList<>();
         }
        else{
            String[] supportingBrowsers = supportedBrowsers.replaceAll("\\s","").split(",");
             for(String browser : supportingBrowsers){
                 if(Arrays.stream(ValidBrowsers.values()).anyMatch(b -> b.name().equals(browser.toUpperCase()))){
                     validatedBrowsers.add(browser);
                 }else{
                     log.warn("{} is not a valid browser",browser);
                 }
             }
            log.info("returning the browsers after validating");
            stopWatch.stop();
            log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                    " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
            return validatedBrowsers;
        }
    }
    @FXML
    public void initialize() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("method initializing the execution UI");
        autType.getItems().removeAll(autType.getItems());
        autType.getItems().addAll(apiCalls.getAutTypes());

        if(checkAndGetBrowsers().size()!=0) {
            log.info(String.valueOf((checkAndGetBrowsers())));
            browserBox.getItems().addAll(checkAndGetBrowsers());
            log.info("browsers added in CheckComboBox of execution UI");
        }else{
            Platform.runLater(() -> {
                log.error("Error in configuring browsers");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Browser configuration error");
                alert.setContentText("Browser(s) may not be configured or empty.Please check!");
                alert.showAndWait();
                Stage execStage = (Stage) executeTestsAnchorPane.getScene().getWindow();
                execStage.close();
            });
        }

        log.info("method completed by updating AutTypes from api call in execution UI");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Function performed when ViewTestResults Button is clicked in JavaFX execution UI-to display test results
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
     * Function performed when ExecuteTests Button is clicked in JavaFX execution UI - tests will execute
     * @param actionEvent JavaFX UI ExecuteTests Button Click
     */
    @FXML
    public void testExecution(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("clicked on executetests button from execution UI");
        new Thread() {
            public void run() {
                ObservableList<String> browsers = browserBox.getCheckModel().getCheckedItems();
                log.info(browsers.toString());
                if(autType.getValue()!=null&&executeAutCombo.getValue()!=null&&
                        versionCombo.getValue()!=null&&browsers.size()!=0) {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                Stage exeStage = (Stage) executeTestsAnchorPane.getScene().getWindow();
                                exeStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        event.consume();
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Unable to close");
                                        alert.setContentText("Not able to close the UI because execution is in process");
                                        alert.showAndWait();
                                    }
                                });
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

                    if (!toggleSwitch.isSelected()) {
                        log.info("Sequential execution started.");
                       String[] browserArray = browsers.toArray(new String[browsers.size()]);
                        for (String browser : browserArray) {
                            settingBrowserProperties(browser);
                            ExecutionAutDetails details = new ExecutionAutDetails();
                            details.setTestType(autType.getValue());
                            details.setAut(executeAutCombo.getValue());
                            details.setVersion(versionCombo.getValue());
                            details.setTestCases(null);
                            log.info("Api call to execute tests " + details.toString());
                            apiCalls.executeTests(details);
                        }
                    }else {
                        log.warn("Parallel execution not supported.");
                        //TODO for parallel execution
                    }
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

                                Stage executeStage = (Stage) executeTestsAnchorPane.getScene().getWindow();
                                executeStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        executeStage.close();
                                    }
                                });
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
            else{
                Platform.runLater(() -> {
                    log.error("Field not selected");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Field not selected");
                    alert.setContentText("Please select all the fields");
                    alert.showAndWait();
                });
            }
        }).start();
    }

    /**
     * Function performed when Cancel Button is clicked in JavaFX execution UI
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

    /**
     * For execution, AUT names will be updated based on AUT type selected
     */
    @FXML
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

    /**
     * For execution, AUT versions will be updated based on AUT selected
     */
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

    /**
     * For execution, setting the browser properties in test project dynamically with respect to selected browser
     * @param browser selected browser as input
     */
    private void settingBrowserProperties(String browser){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("setting properties for {} browser",browser);
            String serenityPropertiesPath = reader.getProperty("destinationpath") + separator +
                    autType.getValue() + separator + executeAutCombo.getValue() + separator +
                    versionCombo.getValue() + separator + "serenity.properties";//Test project's serenity.properties file path
            log.info(serenityPropertiesPath);

            Properties properties = new Properties();
            try(FileInputStream in = new FileInputStream(serenityPropertiesPath)) {

                properties.load(in);//loading the serenity.properties file

                switch (browser) {
                    case "edge" : {
                        properties.setProperty(reader.getProperty("propertykey1"), browser);
                        properties.setProperty(reader.getProperty("propertykey2"), reader.getProperty("driverpath") + "msedgedriver.exe");
                        break;
                    }
                    case "chrome" : {
                        properties.setProperty(reader.getProperty("propertykey1"), browser);
                        properties.setProperty(reader.getProperty("propertykey2"), reader.getProperty("driverpath") + "chromedriver.exe");
                        break;
                    }
                    case "firefox" : {
                        properties.setProperty(reader.getProperty("propertykey1"), browser);
                        properties.setProperty(reader.getProperty("propertykey2"), reader.getProperty("driverpath") + "geckodriver.exe");
                        break;
                    }
                }
                try(FileOutputStream out = new FileOutputStream(serenityPropertiesPath)) {
                    properties.store(out, null);
                }
                log.info("{} browser properties setting completed",browser);
            } catch (Exception e) {
                log.error(e.toString());
                throw new RuntimeException(e);
            }
            stopWatch.stop();
            log.info("Execution time for " + new Object() {
            }.getClass().getEnclosingMethod().getName() +
                    " method is " + String.format("%.2f", stopWatch.getTotalTimeSeconds()) + " seconds");
    }
}