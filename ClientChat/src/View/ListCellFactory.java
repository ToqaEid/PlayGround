/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import DataTransferObject.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

/**
 *
 * @author toqae
 */
public class ListCellFactory extends ListCell<User> {

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        
        if (user != null && !empty) {
        
            System.out.println("View.ListCellFactory.updateItem() >> " + user.getUserEmail());
            
            FXMLLoader fxml = new FXMLLoader();
            Parent parent;
            try {
                parent = fxml.load((getClass().getResource("/View/ContactItemFXML.fxml")).openStream());
              
                ListViewNodeController listNode = fxml.getController();

                listNode.passListNodeToHomeController(listNode, user);

                setGraphic(parent);
                
            } catch (IOException ex) {
                Logger.getLogger(ClientHomeController_2.class.getName()).log(Level.SEVERE, null, ex);
            }

        }else{
            setGraphic(null);
        }
    }

}
