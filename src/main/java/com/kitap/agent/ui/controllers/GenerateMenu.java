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
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
/**
 * @Author KT1497
 *
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
                }else {
                    selectedDir = new File(projectPath);
                    validateProject();
                }
            }
        });
    }

    /**
     * @Description Choosing the Test Project folder by clicking on Browse Button from JavaFX UI
     *
     * @param actionEvent JavaFx UI Browse Button click
     */
    @FXML
    public void choosingFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        selectedDir = directoryChooser.showDialog(new Stage());
        log.info(selectedDir.toString());
        folderTextField.setText(selectedDir.toString());
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
     * @Description Creating new AUT and adding it to AUTs list dropdown by clicking on
     * CreateNewAUT Button from JavaFX UI
     *
     * @param  actionEvent JavaFx UI CreateNewAUT button Click
     */
    @FXML
    public void enterNewAut(ActionEvent actionEvent) {
        /*TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Input");
        inputDialog.setHeaderText("New AUT");
        inputDialog.setContentText("Enter AUT:");
        Optional<String> result = inputDialog.showAndWait();
        log.info(result.toString());
        result.ifPresent(aut -> {
            autCombo.setValue(aut);
            autCombo.getItems().add(aut);
        });*/
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
        cancelButton.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX,Color.GRAY,10,0,0,0));
        cancelButton.setStyle("-fx-background-color: #de3c27;");

        Button okButton = new Button("OK");
        okButton.setTranslateX(120);
        okButton.setTranslateY(60);
        okButton.setMinSize(80, 25);
        okButton.setTextFill(Color.WHITE);
        okButton.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX,Color.GRAY,10,0,0,0));
        okButton.setStyle("-fx-background-color: #096eb6;");

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String autNameString = autNameField.getText();

                String result  = apiCalls.saveAUT(autNameString, autTypeResult.getText());
                if(result.equals("Duplicated AUT")){
                    //TODO need to show alert
                }else{
                    autCombo.getItems().add(autNameString);
                    autCombo.setValue(autNameString);
                    operations.createAut(autNameString, autTypeResult.getText());
                }
                //new SaveAut().saveAut(autNameString, autTypeResult.getText());



                newAutStage.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                newAutStage.close();
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
    }

    /**
     * Function performed when Cancel Button is clicked in JavaFX UI
     *
     * @param actionEvent JavaFX UI Cancel Button Click
     */
    @FXML
    public void clickedCancelButton(ActionEvent actionEvent) {
        Stage generateStage = (Stage) anchorPane.getScene().getWindow();
        generateStage.close();
        log.info("Clicked Cancel Button");
    }

    /**
     * Function performed when GenerateTests Button is clicked in JavaFX UI which means generating tests
     *
     * @param actionEvent JavaFX UI GenerateTests Button Click
     */
    @FXML
    public void generateTests(ActionEvent actionEvent) {

        new Thread(){
            public void run(){
                Platform.runLater(new Runnable() {
                    public void run () {

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
                validateProject();
                Validator validator = new Validator();
                validator.compileAndPackage(selectedDir);
                String version = validator.copyFiles(details);
                details.setVersion(Long.parseLong(version));
                new Generator().generate(details);

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
                    }
                });
            }
        }.start();
    }

    /**
     * @Description Checking the DirectoryPath selected whether a valid Java Project or not
     */

    private void validateProject() {
        String [] checker = new Validator().checkValidation(selectedDir);
        System.out.println(Arrays.toString(checker));
        if (checker[0].equals("invalid")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Project");
            alert.setContentText("Please select valid project");
            alert.showAndWait();
            log.info("invalid project");
            autTypeResult.setText("Invalid");
            createAutButton.setDisable(true);
            generateTestsButton.setDisable(true);
        } else {
            autTypeResult.setText(checker[1]);
            createAutButton.setDisable(false);
            generateTestsButton.setDisable(false);
            createAutButton.setDisable(false);
            autCombo.getItems().removeAll(autCombo.getItems());
            //autCombo.getItems().addAll(operations.getListOfFolders(checker[1]));
            autCombo.getItems().addAll(apiCalls.getAllAUT(checker[1]));
        }
    }

    private void pomFileReading() {
        final String GROUPID_WEB =  "<groupId>com.kitap.fw.web</groupId>";
        final String GROUPID_SF = "<groupId>com.kitap.fw.salesforce</groupId>";
        final String GROUPID_MOBILE = "<groupId>com.kitap.fw.mobile</groupId>";
        final String GROUPID_DESKTOP = "<groupId>com.kitap.fw.desktop</groupId>";

        final String ARTFID_WEB = "<artifactId>kitap-web</artifactId>";
        final String ARTFID_SF = "<artifactId>kitap-salesforce</artifactId>";
        final String ARTFID_MOBILE = "<artifactId>kitap-mobile</artifactId>";
        final String ARTFID_DESKTOP = "<artifactId>kitap-desktop</artifactId>";

        File pom = new File(selectedDir + "\\pom.xml");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pom));
            String line;
            String trimmedLine;
            while ((line = reader.readLine()) != null) {
                trimmedLine = line.trim();
                if (trimmedLine.contains(GROUPID_WEB)||
                        (trimmedLine.contains(GROUPID_SF))||
                        (trimmedLine.contains(GROUPID_MOBILE))||
                        (trimmedLine.contains(GROUPID_DESKTOP))) {
                    trimmedLine = trimmedLine.replace(" ", "");
                    if (trimmedLine.contains("<!--")) continue;

                    String artifactLine, trimLine;
                    while((artifactLine = reader.readLine())!=null){
                        trimLine = artifactLine.trim();
                        if((trimLine.contains(ARTFID_WEB))||
                                (trimLine.contains(ARTFID_SF))||
                                (trimLine.contains(ARTFID_MOBILE))||
                                (trimLine.contains(ARTFID_DESKTOP))){
                            trimLine = trimLine.replace(" ", "");
                            if (trimLine.contains("<!--")) continue;
                            String result = (trimLine.substring(trimLine.indexOf("-")+1, trimLine.lastIndexOf("<")));
                            String updatedAutType=result.substring(0,1).toUpperCase()+ result.substring(1);
                            autTypeResult.setText(updatedAutType);
                            createAutButton.setDisable(false);
                            generateTestsButton.setDisable(false);
                            break;
                        }
                    }
                    break;
                }else {
                    autTypeResult.setText("Only KiTAP Core");
                    createAutButton.setDisable(true);
                    generateTestsButton.setDisable(true);
                }
            }
        } catch (IOException ex) {
            log.error(ex.toString());
            throw new RuntimeException(ex);
        }

        /*final String GROUPID_WEB =  "com.kitap.fw.web";
        final String GROUPID_SF = "com.kitap.fw.salesforce";
        final String GROUPID_MOBILE = "com.kitap.fw.mobile";
        final String GROUPID_DESKTOP = "com.kitap.fw.desktop";

        final String ARTFID_WEB = "kitap-web";
        final String ARTFID_SF = "kitap-salesforce";
        final String ARTFID_MOBILE = "kitap-mobile";
        final String ARTFID_DESKTOP = "kitap-desktop";

        File pom = new File(selectedDir + "\\pom.xml");
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(pom);

            NodeList nodeList = document.getElementsByTagName("dependency");
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr); // here node.getNodeName() = dependency

                if (node.getNodeType() == Node.ELEMENT_NODE) // (1 == 1)
                {
                    Element eElement = (Element) node;
                    String groupId = eElement.getElementsByTagName("groupId").item(0).getTextContent();
                    if((groupId.equals(GROUPID_WEB))||(groupId.equals(GROUPID_SF))||
                            (groupId.equals(GROUPID_MOBILE))||(groupId.equals(GROUPID_DESKTOP))){
                        String artifactId =  eElement.getElementsByTagName("artifactId").item(0).getTextContent();
                        if((artifactId.equals(ARTFID_WEB))||(artifactId.equals(ARTFID_SF))||
                                (artifactId.equals(ARTFID_MOBILE))||(artifactId.equals(ARTFID_DESKTOP))){
                            String trimmedAutType = artifactId.substring((artifactId.indexOf("-")+1));
                            String updatedAutType = trimmedAutType.substring(0,1).toUpperCase() + trimmedAutType.substring(1);
                            autTypeResult.setText(updatedAutType);
                            createAutButton.setDisable(false);
                            generateTestsButton.setDisable(false);
                            break;
                        }
                    }else {
                        autTypeResult.setText("Only KiTAP Core");
                        createAutButton.setDisable(true);
                        generateTestsButton.setDisable(true);
                    }
                }
            }
        }
        catch (Exception ex){
            log.error(ex.toString());
        }*/
        }
}
