/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListsRendering;

import View.*;
import DataTransferObject.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author toqae
 */
public class GroupListCallBack implements Callback<ListView<String>, ListCell<String>>{

    @Override
    public ListCell<String> call(ListView<String> param) {
        /*ClientHomeController_2 home = new ClientHomeController_2();
        ClientHomeController_2.GroupListCellFactory listFactory = home.new GroupListCellFactory();
        return listFactory;*/
        return new GroupListCellFactory();
    }
    
}