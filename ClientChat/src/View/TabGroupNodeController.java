/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import DataTransferObject.GroupMsg;
import DataTransferObject.User;
import DataTransferObject.UserMsg;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLElement;

/**
 * FXML Controller class
 *
 * @author toqae
 */
public class TabGroupNodeController extends Information implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private WebView outputArea;
    @FXML
    private TextArea inputField;
    @FXML
    private ChoiceBox fontFamilyChooser;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField fontSize;
    @FXML
    private Button sendButton;

    ClientHomeController_2 homeConroller;

    ArrayList<String> usersinGroup;
    User myUser;
    String groupName;

    WebEngine webEngine;
    Document doc;
    Element section;

    String pervious = null;

    public void intializeHomeController(ClientHomeController_2 homeController) {
        this.homeConroller = homeController;
        getMyUserObjectFromHome();
    }

    public void getMyUserObjectFromHome() {
        this.myUser = homeConroller.getUserObjectFromChat();
    }

    public void setUsersInGroup(ArrayList<String> usersinGroup) {
        this.usersinGroup = usersinGroup;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /*=========================Receiving msg======================*/
    public void displayGroupMsgInOutputArea(GroupMsg groupMsg) {
        System.out.println("outputmsg " + groupMsg.getMsg());
        if (usersinGroup == null) {
            this.usersinGroup = groupMsg.getUsersInGroup();
        }
        Platform.runLater(new Runnable() {
            public void run() {
                //Label label = new Label(msg);
                doc = webEngine.getDocument();
                if (doc == null) {
                    webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
                        if (newState == Worker.State.SUCCEEDED) {
                            doc = webEngine.getDocument();
                            receiverNode(groupMsg);

                        }
                    });
                } else {
                    receiverNode(groupMsg);
                }
            }
        });

    }

    public void receiverNode(GroupMsg groupMsg) {
        section = doc.getElementById("main");

        Element liElement = doc.createElement("li");

        liElement.setAttribute("class", "left clearfix");

        Element spanElement = doc.createElement("span");

        spanElement.setAttribute("class", "chat-img pull-left");
        Element divElement1 = doc.createElement("div");

        divElement1.setAttribute("class", "header");

        if (pervious==null || pervious != groupMsg.getSender().getUserEmail()) {
            Element imgElement = doc.createElement("img");
            imgElement.setAttribute("src", ""+getClass().getResource("/img/u.png")+"");
            imgElement.setAttribute("class", "img-circle");

            spanElement.appendChild(imgElement);
            Element strongElement = doc.createElement("strong");
            strongElement.setAttribute("class", "pull-left primary-font");

            Text userName = doc.createTextNode(groupMsg.getSender().getUserNickName()); //name

            strongElement.appendChild(userName);
            divElement1.appendChild(strongElement);
        }
        Element divElement = doc.createElement("div");

        divElement.setAttribute("class", "chat-body clearfix");

        Element smallElement = doc.createElement("small");

        smallElement.setAttribute("class", "text-muted");

        Element spanElement1 = doc.createElement("span");

        spanElement1.setAttribute("class", "glyphicon glyphicon-time pull-right");

        Element spanElement2 = doc.createElement("span");

        spanElement2.setAttribute("class", "pull-right");
        Text Date = doc.createTextNode(groupMsg.getMsgDate()); //Date

        spanElement2.appendChild(Date);

        smallElement.appendChild(spanElement2);

        smallElement.appendChild(spanElement1);

        Element pElement = doc.createElement("p");

        pElement.setAttribute("class", "text-left");
        pElement.setAttribute("style", "word-wrap:break-word; width:25em; color:" + groupMsg.getColor() + "; font-size:" + groupMsg.getFontSize() + "px; font-family:" + groupMsg.getFontFamily() + ";");
        Text msgText = doc.createTextNode(groupMsg.getMsg());   //msg

        pElement.appendChild(msgText);

        divElement.appendChild(pElement);

        divElement1.appendChild(smallElement);

        divElement.appendChild(divElement1);

        liElement.appendChild(spanElement);

        liElement.appendChild(divElement);

        section.appendChild(liElement);
        pervious = groupMsg.getSender().getUserEmail();
    }

    /*==================Sending Msg ================================*/
    @FXML
    private void sendButtonAction(ActionEvent event) {

        boolean validate = validateField(inputField, null, null);
        if (validate) {
            GroupMsg groupMsg = senderNode();
            homeConroller.sendGroupMsgToClientChat(groupMsg);
        }
        inputField.clear();

    }

    public GroupMsg senderNode() {
        doc = webEngine.getDocument();
        section = doc.getElementById("main");

        Element liElement = doc.createElement("li");
        liElement.setAttribute("class", "right clearfix");

        Element spanElement = doc.createElement("span");
        spanElement.setAttribute("class", "chat-img pull-right");
        Element divElement1 = doc.createElement("div");
        divElement1.setAttribute("class", "header");

        if (pervious == null || pervious != myUser.getUserEmail()) {
            Element imgElement = doc.createElement("img");
            //imgElement.setAttribute("src", "http://placehold.it/50/FA6F57/fff&text=ME");
            imgElement.setAttribute("src", ""+getClass().getResource("/img/me.png")+"");
            imgElement.setAttribute("class", "img-circle");
            pervious = myUser.getUserEmail();

            spanElement.appendChild(imgElement);
            Element strongElement = doc.createElement("strong");
            strongElement.setAttribute("class", "pull-right primary-font");
            Text userName = doc.createTextNode(myUser.getUserNickName()); //name
            strongElement.appendChild(userName);
            divElement1.appendChild(strongElement);
        }
        Element divElement = doc.createElement("div");
        divElement.setAttribute("class", "chat-body clearfix");

        Element smallElement = doc.createElement("small");
        smallElement.setAttribute("class", "text-muted");

        Element spanElement1 = doc.createElement("span");
        spanElement1.setAttribute("class", "glyphicon glyphicon-time");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String nowDate = dateFormat.format(date); //2016/11/16 12:08:43

        Text Date = doc.createTextNode(nowDate); //Date

        smallElement.appendChild(spanElement1);
        smallElement.appendChild(Date);

        Element pElement = doc.createElement("p");
        Text msgText = doc.createTextNode(inputField.getText());   //msg
        //changing java color to web color
        String color = String.format("#%02X%02X%02X",
                (int) (colorPicker.getValue().getRed() * 255),
                (int) (colorPicker.getValue().getGreen() * 255),
                (int) (colorPicker.getValue().getBlue() * 255));
        //styling the msg
        pElement.setAttribute("class", "text-right");
        pElement.setAttribute("style", "word-wrap:break-word; width:25em; color:" + color + "; font-size:" + fontSize.getText() + "px; font-family:" + fontFamilyChooser.getValue().toString() + ";");

        pElement.appendChild(msgText);
        divElement.appendChild(pElement);
        divElement1.appendChild(smallElement);

        divElement.appendChild(divElement1);

        liElement.appendChild(spanElement);
        liElement.appendChild(divElement);

        section.appendChild(liElement);

        GroupMsg groupMsg = new GroupMsg(myUser, groupName, usersinGroup, inputField.getText(), nowDate, color, fontSize.getText(), fontFamilyChooser.getValue().toString());
        return groupMsg;
    }

    /*===================Intializa===============================*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        fontFamilyChooser.setItems(FXCollections.observableArrayList(
                "Times New Roman", "Georgia", "Arial", "Verdana", "Courier New", "Lucida Console"));
        fontFamilyChooser.getSelectionModel().select(0);
        colorPicker.setValue(Color.BLACK);
        webEngine = outputArea.getEngine();
        webEngine.loadContent("<html><head>"
                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n"
                + "<style>"
                + "body{"
                + "     font-size: 20px;"
                + "}"
                + "p{"
                + "    margin-left: 100px;"
                + "}"
                + ".chat\n"
                + "{\n"
                + "    list-style: none;\n"
                + "    margin: 0;\n"
                + "    padding: 0;\n"
                + "}\n"
                + "\n"
                + "li\n"
                + "{\n"
                + "    margin-bottom: 10px;\n"
                + "    padding-bottom: 5px;\n"
                + "    border-bottom: 1px dotted #B3A9A9;\n"
                + "}\n"
                + "\n"
                + "li.left .chat-body\n"
                + "{\n"
                + "    margin-left: 60px;\n"
                + "}\n"
                + "\n"
                + "li.right .chat-body\n"
                + "{\n"
                + "    margin-right: 60px;\n"
                + "}\n"
                + "\n"
                + "\n"
                + ".chat li .chat-body p\n"
                + "{\n"
                + "    margin: 0;\n"
                + "    color: #777777;\n"
                + "}\n"
                + "\n"
                + ".panel .slidedown .glyphicon, .chat .glyphicon\n"
                + "{\n"
                + "    margin-right: 5px;\n"
                + "}\n"
                + "\n"
                + ".panel-body\n"
                + "{\n"
                + "    overflow-y: scroll;\n"
                + "    height: 250px;\n"
                + "}\n"
                + "\n"
                + "::-webkit-scrollbar-track\n"
                + "{\n"
                + "    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);\n"
                + "    background-color: #F5F5F5;\n"
                + "}\n"
                + "\n"
                + "::-webkit-scrollbar\n"
                + "{\n"
                + "    width: 12px;\n"
                + "    background-color: #F5F5F5;\n"
                + "}\n"
                + "\n"
                + "::-webkit-scrollbar-thumb\n"
                + "{\n"
                + "    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);\n"
                + "    background-color: #555;\n"
                + "}</style></head><body><div class=\"container\">\n"
                + "<div class=\"row\">\n"
                + "        <div class=\"col-md-5\">"
                + "                 <ul id=\"main\" class=\"chat\">"
                + "                 </ul>"
                + "         </div>\n"
                + "</div></body></html>");

    }

}
