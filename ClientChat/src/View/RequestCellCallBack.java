/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import DataTransferObject.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author mohamed
 */


public class RequestCellCallBack implements Callback<ListView<User>, ListCell<User>>{

    private ClientHomeController_2 homeView;
    
    public void intializeHomeController(ClientHomeController_2 homeView){
        this.homeView = homeView;
    }
     
    
    
    
    @Override
    public ListCell<User> call(ListView<User> param) {
 
        RequestCellFactory factory = new RequestCellFactory();
        
        factory.intializeHomeController(homeView);
    
        return factory;
       // return new  RequestCellFactory();
    
    }
    
    
    
    
}
