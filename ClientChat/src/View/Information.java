/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author toqae
 */
public class Information {

    public boolean validateField(TextField field, Label error, String msg) {
        String str = field.getText().trim();
        if (str.length() == 0) {
            error.setText(msg);
            return false;
        }

        error.setText("");

        return true;
    }

    public boolean validateField(TextArea field, Label error, String msg) {
        String str = field.getText().trim();
        if (str.length() == 0) {
            //              error.setText(msg);
            return false;
        }

//            error.setText("");
        return true;
    }
    public boolean validateEmail(TextField email, Label error, String errorMsg) {
        //Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Pattern pattern =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email.getText());
        if (matcher.find() && matcher.group().equals(email.getText())) {
            error.setText("");
            return true;

        } else {
            email.setText("");
            //error.setText("Invalid Email or Password");
            error.setText(errorMsg);
            return false;
        }

    }

    public boolean validatePassword(TextField password, Label error) {
        //Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%]).{6,10})");
        Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-zA-Z]).{6,10})");
        Matcher matcher = pattern.matcher(password.getText());
        if (matcher.matches()) {
            error.setText("");
            return true;

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Password");
            alert.setHeaderText(null);
            alert.setContentText("Password must contains at least one digit and length must be between 6-10");
            alert.showAndWait();
            password.setText("");
            error.setText("please enter valid password");
            return false;
        }

    }
    void showPopUpMsg(String msg) {
    }

    void showNotification(String msg) {
    }
}
