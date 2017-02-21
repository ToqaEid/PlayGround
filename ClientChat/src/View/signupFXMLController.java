/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import DataTransferObject.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author mohamed
 */
public class signupFXMLController extends Information implements Initializable, View.UserRegInt {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField countryField;
    @FXML
    private Label errorName;
    @FXML
    private Label errorEmail;
    @FXML
    private Label errorPassword;
    @FXML
    private Label errorCountry;
    @FXML
    private Label errorInvalidEmail;
    @FXML
    private Label errorInvalidPass;
    @FXML
    private Button closeButton;
    @FXML
    private ToolBar toolBar;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;

    double xOffset, yOffset;

    //ClientChat Reference
    private ClientChat clientChat;
    private boolean isConnected = true;

    //////////////// firstTime connection
    public boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
    //////////////////////// 

    public void showNoConnection() {
        System.out.println("noConn");
        //NoConnLabel.setText("Sorry, No Connection! Please Try Again Later :)");
    }
//////////////

    public void intializeClientChatRef(ClientChat clientChat) {
        this.clientChat = clientChat;
    }
    //////////// on Signup button click listener

    @FXML
    private void handleSignUpAction(ActionEvent event) {
        boolean returnFlag = true;
        ArrayList<TextField> inputFields = getInputFieldsData();
        ArrayList<Label> errorMsgLabelRef = getErrorMsgLabelRef();

        for (int i = 0; i < inputFields.size(); i++) {
            boolean validateResult = validateField(inputFields.get(i), errorMsgLabelRef.get(i), "*");

            if (!validateResult) {
                returnFlag = false;
            } else {
                if (inputFields.get(i).getId().equals("emailField")) {

                    boolean validateEmail = validateEmail(inputFields.get(i), errorInvalidEmail, "Invalid Email");
                    if (!validateEmail) {
                        returnFlag = false;
                    }
                }
                if (inputFields.get(i).getId().equals("passwordField")) {

                    boolean validatePassword = validatePassword(inputFields.get(i), errorInvalidPass);
                    if (!validatePassword) {
                        returnFlag = false;
                    }
                }
            }
        }
        if (!returnFlag) {
            return;
        }
        if (getIsConnected() == false) {
            showNoConnection();

            return;
        }

        try {

            String gender = null;
            if (maleRadioButton.isSelected()) {
                System.out.println(maleRadioButton.getText());
                gender = maleRadioButton.getText();
            }
            if (femaleRadioButton.isSelected()) {
                System.out.println(femaleRadioButton.getText());
                gender = femaleRadioButton.getText();
            }
            User tempUser = new User(emailField.getText(), passwordField.getText(), fullNameField.getText(), gender, countryField.getText());
            clientChat.signup(tempUser);
            ((Node) (event.getSource())).getScene().getWindow().hide();

            //Parent parent = FXMLLoader.load(getClass().getResource("/View/ClientHomeView_2.fxml"));
            FXMLLoader fxml = new FXMLLoader();
            Parent parent = fxml.load((getClass().getResource("/View/ClientHomeView_2.fxml")).openStream());
            ClientHomeController_2 home = fxml.getController();
            home.intializeClientChatRefForSignUp(clientChat);
            clientChat.intializeHomeRef(home);
            //home.setCurrentUser(tempUser);

            Stage stage = new Stage();
            stage.setTitle("MITS");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(parent));
            stage.show();

            Node n = parent.lookup("vds");

            //Testing Block
            
            System.out.println("signuppuser");
            System.out.println(tempUser);
            
            //controller.signUpUser(tempUser);

            //End of Testing Block
        } catch (IOException ex) {
            System.out.println("ERROR:: " + ex.toString());
        }

    }

    //////////// on GoTo Login button click listener
    @FXML
    private void handleGoToLoginAction(ActionEvent event) {

        //System.out.println("Going to login page ... ");
        try {

            System.out.println("Going to login page ... ");

            ((Node) (event.getSource())).getScene().getWindow().hide();
/*
            Parent parent = FXMLLoader.load(getClass().getResource("/View/loginFXML.fxml"));

            Stage stage = new Stage();
            stage.getIcons().add(new Image("/img/logo7.png"));
            stage.setTitle("MITS");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(parent));
            stage.show();
 */           
            FXMLLoader fxml = new FXMLLoader();
            Parent root = fxml.load((getClass().getResource("loginFXML.fxml")).openStream());
            loginFXMLController loginView = fxml.getController();
            loginView.setControllerRef(clientChat);
            clientChat.intializeLoginRef(loginView);
            
            
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/img/logo7.png"));
            stage.setTitle("MITS");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            System.out.println("ERROR:: " + ex.toString());
        }

    }

    public void closeButtonHandle(ActionEvent e) {
        Stage loginStage = (Stage) closeButton.getScene().getWindow();
        loginStage.close();
        Platform.exit();
    }


    @Override
    public ArrayList<TextField> getInputFieldsData() {
        ArrayList<TextField> inputFields = new ArrayList<>();
        inputFields.add(fullNameField);
        inputFields.add(emailField);
        inputFields.add(passwordField);
        inputFields.add(countryField);
        return inputFields;
    }

    @Override
    public ArrayList<Label> getErrorMsgLabelRef() {
        ArrayList<Label> errorLabels = new ArrayList<>();
        errorLabels.add(errorName);
        errorLabels.add(errorEmail);
        errorLabels.add(errorPassword);
        errorLabels.add(errorCountry);
        return errorLabels;
    }

        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ToggleGroup genderGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderGroup);
        maleRadioButton.setSelected(true);
        femaleRadioButton.setToggleGroup(genderGroup);
/*        toolBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = toolBar.getScene().getWindow().getX() - event.getScreenX();
                yOffset = toolBar.getScene().getWindow().getY() - event.getScreenY();
            }
        });
        toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                toolBar.getScene().getWindow().setX(event.getScreenX() + xOffset);
                toolBar.getScene().getWindow().setY(event.getScreenY() + yOffset);
            }
        });
*/    }

}
