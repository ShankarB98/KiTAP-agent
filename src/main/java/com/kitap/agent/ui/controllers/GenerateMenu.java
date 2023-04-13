package com.kitap.agent.ui.controllers;

import com.kitap.agent.api.apicalls.ApiCalls;
import com.kitap.agent.generate.flow.Generator;
import com.kitap.agent.generate.flow.Validator;
import com.kitap.agent.generate.util.FileOperations;
import com.kitap.agent.ui.tray.AddEffectsToMenuAndMenuItems;
import com.kitap.agent.ui.tray.AgentTrayIcon;
import com.kitap.testresult.dto.agent.GenerationDetails;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Author KT1497
 * @Description Controller class for generateMenu.fxml file,
 * which includes functionality with fxml UI elements
 */
@Slf4j
@Component
public class GenerateMenu {
    final javafx.scene.image.Image generatingColour = new javafx.scene.image.Image(
            Objects.requireNonNull(AgentTrayIcon.class.getResource("/images/yellow.png")).toExternalForm());

    final javafx.scene.image.Image agentRunningColour = new javafx.scene.image.Image(
            Objects.requireNonNull(AgentTrayIcon.class.getResource("/images/green.png")).toExternalForm());

    final FileOperations operations = new FileOperations();
    final private ApiCalls apiCalls = new ApiCalls();
    @FXML
    public Label generatingBlinkLabel;
    File selectedDir;
    @FXML
    public ProgressIndicator generateTestsProgressIndicator;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public TextField folderTextField;
    @FXML
    public ComboBox<String> autCombo;
    @FXML
    public Button browseButton;
    @FXML
    public Button createAutButton;
    @FXML
    public Button generateTestsButton;
    @FXML
    public Button cancelButton;
    @FXML
    Label autTypeResult;

    @FXML
    public void initialize() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("method initializing the generation UI");
        generateTestsButton.setDisable(true);
        createAutButton.setDisable(true);
        folderTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String projectPath = t1;
                if (projectPath == "") {
                    autTypeResult.setText("");
                    generateTestsButton.setDisable(true);
                    createAutButton.setDisable(true);
                } else {
                    selectedDir = new File(projectPath);
                    validateProject();
                }
            }
        });
        log.info("method initialize for generation UI completed");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * @param actionEvent JavaFx UI Browse Button click
     * @Description Choosing the Test Project folder by clicking on Browse Button from JavaFX UI
     */
    @FXML
    public void choosingFolder(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("choosing the test project directory from generation UI");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        selectedDir = directoryChooser.showDialog(new Stage());
        if(selectedDir!=null) {
            log.info(selectedDir.toString());
            folderTextField.setText(selectedDir.toString());
        }else{
            log.info("No project selected");
        }
        log.info("method choosingFolder completed from generation UI");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    public void onClickAnchorPane(MouseEvent mouseEvent) {
        /*String projectPath = folderTextField.getText();
        if (projectPath == "") {
            autTypeResult.setText("");
        }else {
            selectedDir = new File(projectPath);
            checkProject();
        }*/
    }

    /**
     * @param actionEvent JavaFx UI CreateNewAUT button Click
     * @Description Creating new AUT and adding it to AUTs list dropdown by clicking on
     * CreateNewAUT Button from JavaFX UI
     */
    @FXML
    public void enterNewAut(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("clicked on CreateNewAut button from generation UI");
        Stage newAutStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root);
        newAutStage.setTitle("New AUT");

        Label autNameLabel = new Label("Enter AUT Name:");
        autNameLabel.setLayoutX(20);
        autNameLabel.setLayoutY(0);
        autNameLabel.setMinSize(100, 25);

        TextField autNameField = new TextField();
        autNameField.setLayoutX(20);
        autNameField.setLayoutY(25);
        autNameField.setMinSize(180, 25);

        Button cancelButton = new Button("Cancel");
        cancelButton.setTranslateX(20);
        cancelButton.setTranslateY(60);
        cancelButton.setMinSize(80, 25);
        cancelButton.setTextFill(Color.WHITE);
        cancelButton.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.GRAY, 10, 0, 0, 0));
        cancelButton.setStyle("-fx-background-color: #de3c27;");

        Button okButton = new Button("OK");
        okButton.setTranslateX(120);
        okButton.setTranslateY(60);
        okButton.setMinSize(80, 25);
        okButton.setTextFill(Color.WHITE);
        okButton.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.GRAY, 10, 0, 0, 0));
        okButton.setStyle("-fx-background-color: #096eb6;");

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                log.info("Onclick of OK button from NewAut UI");
                String autNameString = autNameField.getText();
                log.info("calling api to save AUT");
                String result = apiCalls.saveAUT(autNameString, autTypeResult.getText());
                if (result.equals("Duplicated AUT")) {
                    //TODO need to show alert
                } else {
                    autCombo.getItems().add(autNameString);
                    autCombo.setValue(autNameString);
                    operations.createAut(autNameString, autTypeResult.getText());
                }
                //new SaveAut().saveAut(autNameString, autTypeResult.getText());
                newAutStage.close();
                log.info("closed the NewAut UI");
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                log.info("clicked on cancel button from NewAUT UI");
                newAutStage.close();
                log.info("closed the NewAut UI");
            }
        });

        root.getChildren().add(autNameLabel);
        root.getChildren().add(autNameField);
        root.getChildren().add(okButton);
        root.getChildren().add(cancelButton);
        newAutStage.setWidth(235);
        newAutStage.setHeight(130);

        newAutStage.setScene(scene);
        newAutStage.show();
        log.info("New AUT UI is shown");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Function performed when Cancel Button is clicked in JavaFX UI
     *
     * @param actionEvent JavaFX UI Cancel Button Click
     */
    @FXML
    public void clickedCancelButton(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Clicked Cancel Button of generation UI");
        Stage generateStage = (Stage) anchorPane.getScene().getWindow();
        generateStage.close();
        log.info("closed the generation UI");
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
    }

    /**
     * Function performed when GenerateTests Button is clicked in JavaFX UI which means generating tests
     *
     * @param actionEvent JavaFX UI GenerateTests Button Click
     */
    @FXML
    public void generateTests(ActionEvent actionEvent) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("clicked on GenerateTests button from generation UI");
        new Thread() {
            public void run() {
                if(autCombo.getValue()!=null) {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            Stage genStage = (Stage) anchorPane.getScene().getWindow();
                            genStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent event) {
                                    event.consume();
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error to close");
                                    alert.setContentText("Not able to close the UI because generation is in process");
                                    alert.showAndWait();
                                }
                            });
                            //Giving GeneratingTests Status and disabling contextmenu Items
                            AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setGraphic(new ImageView(generatingColour));
                            AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setText("Generating Tests");
                            for (int i = 1; i <= AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().size() - 1; i++) {
                                AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(i).setDisable(true);
                            }
                            //Progress Indicator
                            generateTestsProgressIndicator.setVisible(true);

                            //Blinking Text
                            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), generatingBlinkLabel);
                            fadeTransition.setFromValue(1.0);
                            fadeTransition.setToValue(0.0);
                            fadeTransition.setCycleCount(Animation.INDEFINITE);
                            fadeTransition.play();
                            generatingBlinkLabel.setVisible(true);

                            //Disabling all the buttons in UI Page
                            browseButton.setDisable(true);
                            createAutButton.setDisable(true);
                            generateTestsButton.setDisable(true);
                            cancelButton.setDisable(true);
                        }
                    });

                    //setting the details for tests generation
                    GenerationDetails details = new GenerationDetails();
                    details.setProjectPath(selectedDir);
                    details.setAutName(autCombo.getValue());
                    details.setAutType(autTypeResult.getText());
                    details.setCreateNewVersion(true);
                    details.setPublishToServer(false);
                    log.info(details.getAutName());
                    Validator validator = new Validator();
                    log.info("compiling and packaging the test project");
                    validator.compileAndPackage(selectedDir);
                    log.info("Copying the files");
                    String version = validator.copyFiles(details);
                    details.setVersion(Long.parseLong(version));
                    log.info("Generating...");
                    new Generator().generate(details);

                    log.info("Generation Completed");

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //Enabling all the contextmenu Items
                            AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setGraphic(new ImageView(agentRunningColour));
                            AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(0).setText("Agent is running");
                            for (int i = 1; i <= AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().size() - 1; i++) {
                                AddEffectsToMenuAndMenuItems.button.getContextMenu().getItems().get(i).setDisable(false);
                            }
                            generateTestsProgressIndicator.setVisible(false); //stopping progressIndicator
                            generatingBlinkLabel.setVisible(false);// stopping Blinking Text

                            //Closing Stage after process completion
                            Stage generateStage = (Stage) anchorPane.getScene().getWindow();
                            generateStage.close();
                            log.info("closed the generation UI");
                            generateStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent event) {
                                    generateStage.close();
                                }
                            });
                        }
                    });
                }
                else{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("No AUT selected");
                            alert.setContentText("Please select AUT");
                            alert.showAndWait();
                            log.error("No AUT selected");
                        }
                    });
                }
            }
        }.start();
        stopWatch.stop();
        log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");

    }

    /**
     * @Description Checking the DirectoryPath selected whether a valid Java Project or not
     */

    private void validateProject() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("started the validation of selected test project");
        new Thread() {
            public void run() {
                if (selectedDir != null) {
                    String[] checker = new Validator().checkValidation(selectedDir);
                    log.info(Arrays.toString(checker));
                    if (checker[0].equals("invalid")) {
                        Platform.runLater(
                                () -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Project");
                        alert.setContentText("Please select valid project");
                        alert.showAndWait();
                        log.error("invalid project");

                                    autTypeResult.setText("Invalid");
                                    createAutButton.setDisable(true);
                                    generateTestsButton.setDisable(true);
                                }
                        );
                    } else {
                        Platform.runLater(
                                () -> {
                                    autTypeResult.setText(checker[1]);
                                    generateTestsButton.setDisable(false);
                                    createAutButton.setDisable(false);
                                    autCombo.getItems().removeAll(autCombo.getItems());
                                    //autCombo.getItems().addAll(operations.getListOfFolders(checker[1]));
                                    autCombo.getItems().addAll(apiCalls.getAllAUT(checker[1]));
                                    autCombo.getItems().remove("");
                                }
                        );
                    }
                    log.info("method validateProject completed");
                    stopWatch.stop();
                    log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                            " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
                }
                else{
                    log.info("Test project directory not selected");
                    log.info("method validateProject completed");
                    stopWatch.stop();
                    log.info("Execution time for "+new Object(){}.getClass().getEnclosingMethod().getName()+
                            " method is "+String.format("%.2f",stopWatch.getTotalTimeSeconds())+" seconds");
                }
            }
        }.start();
    }
}
