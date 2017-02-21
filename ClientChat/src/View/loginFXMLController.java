/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import DataTransferObject.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author mohamed
 */
public class loginFXMLController extends Information implements Initializable, View.UserRegInt {

    ClientChat clientChat;

    @FXML
    private TextField uEmailField;
    @FXML
    private TextField uPasswordField;
    @FXML
    private Label errorEmailLabel;
    @FXML
    private Label errorPasswordLabel;
    @FXML
    private Label errorInvalidEmail;
    @FXML
    private Label NoConnLabel;
    @FXML
    private Button closeButton;
    @FXML
    private Label errorLogin;
    @FXML
    private ToolBar toolBar;

    double xOffset, yOffset;

    private boolean isConnected = true;

    public void setControllerRef(ClientChat clientChat) {
        this.clientChat = clientChat;
    }

    public boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
    //////////////// No connection Msg

    public void showNoConnection() {
        NoConnLabel.setText("Sorry, No Connection! Please Try Again Later :)");
    }
    public void errorConnection() {
        Platform.runLater(() -> {
            Notifications.create()
                    .title("No Connection")
                    .text("Sorry! Server issues :(")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(10))
                    .showError();
            
        });
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> Platform.exit());
        delay.play();

    }
    //////////// on Login button click listener
    @FXML
    private void handleLoginAction(ActionEvent event) {
        boolean returnFlag = true;
        errorLogin.setText("");
        ArrayList<TextField> inputFields = getInputFieldsData();
        ArrayList<Label> errorMsgLabelRef = getErrorMsgLabelRef();

        for (int i = 0; i < inputFields.size(); i++) {
            boolean validateResult = validateField(inputFields.get(i), errorMsgLabelRef.get(i), "*");
            if (!validateResult) {
                returnFlag = false;
            } else {
                if (inputFields.get(i).getId().equals("uEmailField")) {

                    boolean validateEmail = validateEmail(inputFields.get(i), errorInvalidEmail, "Invalid Email or Password");
                    if (!validateEmail) {
                        returnFlag = false;
                    }
                }
            }
        }
        if (!returnFlag) {
            uEmailField.clear();
            uPasswordField.clear();
            return;
        }
        if (getIsConnected() == false) {
            Notifications.create()
                    .title("No Connection!")
                    .text("Sorry. Server is not available now! Please try again later")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(5))
                    .show();
            showNoConnection();

            return;
        }

        User user = new User(uEmailField.getText(), uPasswordField.getText());
        System.out.println("pass");
        System.out.println(uPasswordField.getText());
        System.out.println(uPasswordField.getText() == null);
        
        User result = clientChat.signin(user);
        if (result == null) {
            System.out.println("Sorry NOT Matched");
            errorInvalidEmail.setText("Invalid Email Or Password");

            return;
        } else if (result.getUserEmail().equalsIgnoreCase("Already Loggedin")) {
            errorLogin.setText("Already Logged in fom another client");
            return;
        } else {
            System.out.println("Welcom ya " + result.getUserEmail());
        }

        System.out.println("Logging in ... please wait!");

        //System.out.println("Logging in ... please wait!");
        try {

            System.out.println("You are going to homePage");

            ((Node) (event.getSource())).getScene().getWindow().hide();

            //Parent parent = FXMLLoader.load(getClass().getResource("/View/ClientHomeView_2.fxml"));
            FXMLLoader fxml = new FXMLLoader();
            Parent parent = fxml.load((getClass().getResource("/View/ClientHomeView_2.fxml")).openStream());
            ClientHomeController_2 homeViewCont = fxml.getController();
            homeViewCont.intializeClientChatRef(clientChat);
            clientChat.intializeHomeRef(homeViewCont);
            //homeViewCont.setCurrentUser(user);
            
            Stage stage = new Stage();
            stage.setTitle("MITS");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(parent));
            stage.show();

            System.out.println("Showing Main \"Home\" Page");

        } catch (IOException ex) {
            System.out.println("ERROR:: " + ex.toString());
        }

    }

    //////////// on GoTo Signup link click listener
    @FXML
    private void handleGoToSignupAction(ActionEvent event) {

        try {

            System.out.println("Going to Signup page ... ");
            ((Node) (event.getSource())).getScene().getWindow().hide();

            //Parent parent = FXMLLoader.load(getClass().getResource("/View/signupFXML.fxml"));
            FXMLLoader fxml = new FXMLLoader();
            Parent parent = fxml.load((getClass().getResource("/View/signupFXML.fxml")).openStream());
            signupFXMLController signupViewCont = fxml.getController();
            signupViewCont.intializeClientChatRef(clientChat);
            clientChat.intializeSignupRef(signupViewCont);
            //signupViewCont.intializeHomeController(this);

            Stage stage = new Stage();
            stage.getIcons().add(new Image("/img/logo7.png"));
            stage.setTitle("MITS");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(parent));
            stage.show();

            System.out.println("Showing SignUp Page");

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
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        toolBar.setOnMousePressed(new EventHandler<MouseEvent>() {
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
        
    }
    /*=========================== VALIDATION FUNCTIONS =====================*/
    
    @Override

    public ArrayList<TextField> getInputFieldsData() {
        ArrayList<TextField> inputFields = new ArrayList<>();
        inputFields.add(uEmailField);
        inputFields.add(uPasswordField);
        return inputFields;

    }

    @Override
    public ArrayList<Label> getErrorMsgLabelRef() {
        ArrayList<Label> errorLabels = new ArrayList<>();
        errorLabels.add(errorEmailLabel);
        errorLabels.add(errorPasswordLabel);
        return errorLabels;

    }
}
