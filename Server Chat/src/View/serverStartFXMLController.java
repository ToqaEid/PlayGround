/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author mohamed
 */
public class serverStartFXMLController implements Initializable {

    @FXML
    Button closeButton;

    View view;

    public void setControllerRef(View view) {
        this.view = view;
    }

    public void closeButtonHandle(ActionEvent e) {
        Stage loginStage = (Stage) closeButton.getScene().getWindow();
        loginStage.close();
        Platform.exit();
    }

    @FXML
    private void handleStartServerAction(ActionEvent event) {
        try {
            System.out.println("Openning Server Manager");

            //new Controller().startserver();
            ((Node) (event.getSource())).getScene().getWindow().hide();

            //Parent parent =  FXMLLoader.load(getClass().getResource("/View/ServerHomeFXMLDoc.fxml"));
            FXMLLoader fxml = new FXMLLoader();

            Parent parent = fxml.load((getClass().getResource("/View/ServerHomeFXMLDoc.fxml")).openStream());

            SeverHomeFXMLDocController severHomeFXMLDocController
                    = fxml.getController();

            severHomeFXMLDocController.intializeViewRef(view);
            view.intializeHomeRef(severHomeFXMLDocController);

            ///////////////////////////////////////
            view.startServer();

            /////////////////////////////////////
            Stage stage = new Stage();

            stage.setTitle("Server Administrator");
            stage.setScene(new Scene(parent));
            stage.show();

            System.out.println("Server Page is OPENED");

        } catch (IOException ex) {
            System.out.println("ERROR:: " + ex.toString());
            ex.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
