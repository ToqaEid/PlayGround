/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import DataTransferObject.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author mohamed
 */
public class RequestFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    
    @FXML
    private Label friendEmailLabel;
    
    @FXML
    private Button acceptBtn;
    
     @FXML
    private Button rejectBtn;
    
     
    private ClientHomeController_2 homeView;
    
    public void intializeHomeController(ClientHomeController_2 homeView){
        this.homeView = homeView;
    }
     
    
    
    public void passNodeToController ( RequestFXMLController controller, User user )
    {
    
        System.out.println("View.RequestFXMLController.passNodeToController() >> " + user.getUserEmail());
         
        friendEmailLabel.setText(user.getUserEmail());       
        ///////// 1. accept       
        acceptBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("## Accepted >>" );

                homeView.acceptRequest(user);
            }
        });

        //////// 2.  reject 
        

        rejectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("XX Rejected >>");

                homeView.rejectRequest(user);
                
            }
        });
        
    }
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
