/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author toqae
 */
public interface UserRegInt {
    ArrayList<TextField> getInputFieldsData();
    ArrayList<Label> getErrorMsgLabelRef();
}
