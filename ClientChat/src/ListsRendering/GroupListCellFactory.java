/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListsRendering;

import View.*;
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
public class GroupListCellFactory extends ListCell<String> {

    @Override
    protected void updateItem(String group, boolean empty) {
        super.updateItem(group, empty);
        
        if (group != null && !empty) {
        
            FXMLLoader fxml = new FXMLLoader();
            Parent parent;
            try {
                parent = fxml.load((getClass().getResource("/ListsRendering/GroupItem.fxml")).openStream());
              
                GroupItemController groupNode = fxml.getController();

                groupNode.passListNodeToHomeController(group);

                setGraphic(parent);
                
            } catch (IOException ex) {
                Logger.getLogger(ClientHomeController_2.class.getName()).log(Level.SEVERE, null, ex);
            }

        }else{
            setGraphic(null);
        }
    }

}
