/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListsRendering;

import DataTransferObject.User;
import View.ClientHomeController_2;
import View.ListViewNodeController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author toqae
 */
public class GroupItemController implements Initializable {
    @FXML
    private Label groupName;
    @FXML
    private Label particpantsNum;
    ClientHomeController_2 homeView;
    /**
     * Initializes the controller class.
     */
    
    public void intializeHomeController(ClientHomeController_2 homeView){
        this.homeView = homeView;
    }
    public void passListNodeToHomeController(String group){
        String[] groupInfo =group.split(">>");
        groupName.setText(groupInfo[0]);
        particpantsNum.setText("Participants: "+groupInfo[1]);
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
