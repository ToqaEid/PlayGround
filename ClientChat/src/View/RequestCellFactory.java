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
 * @author mohamed
 */
public class RequestCellFactory extends ListCell<User>{

    
    private ClientHomeController_2 homeView;
    
    public void intializeHomeController(ClientHomeController_2 homeView){
        this.homeView = homeView;
    }
     
    
    
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty); //To change body of generated methods, choose Tools | Templates.
   
        
         if (user != null && !empty) {
            FXMLLoader fxml = new FXMLLoader();
            Parent parent;
            try {
                parent = fxml.load((getClass().getResource("/View/RequestFXML.fxml")).openStream());
                //parent = fxml.load((getClass().getResource("/View/ListViewNode.fxml")).openStream());
               
                RequestFXMLController controller = fxml.getController();

                System.out.println("list user");
                System.out.println(user == null);

                System.out.println("controller");
                System.out.println(controller == null);

                controller.intializeHomeController(homeView);
                
                controller.passNodeToController(controller, user);
                setGraphic(parent);
                
            } catch (IOException ex) {
                Logger.getLogger(ClientHomeController_2.class.getName()).log(Level.SEVERE, null, ex);
            }

        }else{
            setGraphic(null);
        }
    
    
    
    }
    
    
    
    
}
