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
 * @author toqae
 */
public class ListCellCallBack implements Callback<ListView<User>, ListCell<User>>{

    @Override
    public ListCell<User> call(ListView<User> param) {
        /*ClientHomeController_2 home = new ClientHomeController_2();
        ClientHomeController_2.ListCellFactory listFactory = home.new ListCellFactory();
        return listFactory;*/
        return new ListCellFactory();
    }
    
}