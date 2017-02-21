/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import DataTransferObject.User;
import com.sun.prism.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author toqae
 */
public class ListViewNodeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Label userName;
    @FXML
    private Label FriendName;
    @FXML
    private Label FriendStatus;
    @FXML
    private Circle statusColor;

    
    private ClientHomeController_2 homeView;
    
    public void intializeHomeController(ClientHomeController_2 homeView){
        this.homeView = homeView;
    }
    public void passListNodeToHomeController(ListViewNodeController listNode, User user){
       
        System.out.println("View.ListViewNodeController.passListNodeToHomeController() >> " + user.getUserEmail()); 
        FriendName.setText(user.getUserNickName());
        FriendStatus.setText(user.getUserStatus());
        switch(user.getUserStatus().toLowerCase()){
            case "online":
                statusColor.setFill(Paint.valueOf("#24905a"));
                break;
            case "away":
                statusColor.setFill(Paint.valueOf("#ff9e05"));
                break;
            case "busy":
                statusColor.setFill(Paint.valueOf("#ff2105"));
                break;
            default:
                statusColor.setFill(Paint.valueOf("#111"));
                
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
