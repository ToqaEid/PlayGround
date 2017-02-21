/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import DataTransferObject.User;
import DataTransferObject.UserMsg;
import chatData.*;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.swing.JFileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.controlsfx.control.Notifications;
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
public class TabNodeController extends Information implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    WebView outputArea;

    @FXML
    TextArea inputField;
    @FXML
    ChoiceBox fontFamilyChooser;
    @FXML
    ColorPicker colorPicker;
    @FXML
    TextField fontSize;
    @FXML
    Button filesend;
    @FXML
    ScrollBar scrollBar;

    ClientHomeController_2 homeConroller;
    String uEmail;
    User friend;
    User myUser;
    WebEngine webEngine;
    Document doc;
    Element section;
    String fontsize;

    String pervious = null;
    /*===for save chat===*/
    ArrayList<UserMsg> messages = new ArrayList<>();

    @FXML
    private Button sendButton;
    File receivedFile = null;

    /*===================== INTIALIZATION =========================*/
    public void intializeHomeController(ClientHomeController_2 homeController) {
        this.homeConroller = homeController;
        //getMyUserObjectFromHome();
    }

    public void setMyUserObject(User myUserr) {
        System.out.println("myuser tab");
        System.out.println(myUserr.getUserEmail());
        System.out.println(myUserr.getUserNickName());
        this.myUser = myUserr;
    }

    public void getMyUserObjectFromHome() {
        this.myUser = homeConroller.getUserObjectFromChat();
    }

    public void setUserEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public void setUserObject(User user) {
        this.friend = user;
    }

    /*=========================Receiving msg======================*/
    public void displayMsgInOutputArea(UserMsg userMsg) {
        System.out.println("outputmsg " + userMsg.getMsg());
        /*===for save chat===*/
        messages.add(userMsg);
        Platform.runLater(new Runnable() {
            public void run() {
                //Label label = new Label(msg);
                doc = webEngine.getDocument();
                System.out.println("doc");
                System.out.println(doc == null);
                System.out.println("webEgine");
                System.out.println(webEngine == null);
                if (doc == null) {
                    webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
                        if (newState == State.SUCCEEDED) {
                            doc = webEngine.getDocument();
                            receiverNode(userMsg);
                        }
                    });
                } else {
                    receiverNode(userMsg);

                }
            }
        });

    }

    public void receiverNode(UserMsg userMsg) {
        System.out.println("doc2");
        System.out.println(doc == null);
        //webEngine.loadContent("<p>"+msg+"</p>");
        section = doc.getElementById("main");

        Element liElement = doc.createElement("li");
        liElement.setAttribute("class", "left clearfix");
        Element spanElement = doc.createElement("span");
        spanElement.setAttribute("class", "chat-img pull-left");
        Element divElement1 = doc.createElement("div");
        divElement1.setAttribute("class", "header");

        if (pervious == null || !pervious.equals(uEmail)) {
            Element imgElement = doc.createElement("img");
            imgElement.setAttribute("src", "" + getClass().getResource("/img/u.png") + "");
            imgElement.setAttribute("class", "img-circle");

            spanElement.appendChild(imgElement);
            pervious = uEmail;
            Element strongElement = doc.createElement("strong");
            strongElement.setAttribute("class", "pull-left primary-font");

            Text userName = doc.createTextNode(userMsg.getSender().getUserNickName()); //name

            strongElement.appendChild(userName);
            divElement1.appendChild(strongElement);
        }
        Element divElement = doc.createElement("div");
        divElement.setAttribute("class", "chat-body clearfix");
        //<small>
        Element smallElement = doc.createElement("small");
        smallElement.setAttribute("class", "text-muted");
        //<spane>
        Element spanElement1 = doc.createElement("span");
        spanElement1.setAttribute("class", "glyphicon glyphicon-time pull-right");
        //<span>
        Element spanElement2 = doc.createElement("span");
        spanElement2.setAttribute("class", "pull-right");
        Text Date = doc.createTextNode(userMsg.getMsgDate()); //Date
        spanElement2.appendChild(Date);
        smallElement.appendChild(spanElement2);
        smallElement.appendChild(spanElement1);
        //<p>
        Element pElement = doc.createElement("p");
        pElement.setAttribute("class", "text-left");
        pElement.setAttribute("style", "word-wrap:break-word; width:25em; color:" + userMsg.getColor() + "; font-size:" + userMsg.getFontSize() + "px; font-family:" + userMsg.getFontFamily() + ";");
        Text msgText = doc.createTextNode(userMsg.getMsg());   //msg
        pElement.appendChild(msgText);
        divElement.appendChild(pElement);
        divElement1.appendChild(smallElement);
        divElement.appendChild(divElement1);
        liElement.appendChild(spanElement);
        liElement.appendChild(divElement);
        section.appendChild(liElement);

    }

    /*==================Sending Msg ================================*/
    @FXML

    private void sendButtonAction(ActionEvent event) {
        boolean validate = validateField(inputField, null, null);
        if (validate) {
            //1.build node and show on screen 
            UserMsg msgSent = senderNode();
            //2.prepre for save chat
            /*===for save chat===*/
            messages.add(msgSent);
            //3.send msg to receiver
            homeConroller.sendMsgToClientChat(msgSent);
        }
        inputField.clear();
    }

    public UserMsg senderNode() {

        doc = webEngine.getDocument();
        section = doc.getElementById("main");

        Element liElement = doc.createElement("li");
        liElement.setAttribute("class", "right clearfix");

        Element spanElement = doc.createElement("span");
        spanElement.setAttribute("class", "chat-img pull-right");
        Element divElement1 = doc.createElement("div");
        divElement1.setAttribute("class", "header");

        if (pervious == null || !(pervious.equals(myUser.getUserEmail()))) {
            Element imgElement = doc.createElement("img");
            imgElement.setAttribute("src", "" + getClass().getResource("/img/me.png") + "");
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

        if (fontSize.getText().trim().length() == 0) {
            fontsize = Integer.toString(20);
        } else {
            fontsize = fontSize.getText();
        }
        pElement.setAttribute("style", "word-wrap:break-word; width:25em; color:" + color + "; font-size:" + fontsize + "px; font-family:" + fontFamilyChooser.getValue().toString() + ";");

        pElement.appendChild(msgText);
        divElement.appendChild(pElement);
        divElement1.appendChild(smallElement);

        divElement.appendChild(divElement1);

        liElement.appendChild(spanElement);
        liElement.appendChild(divElement);

        section.appendChild(liElement);
        UserMsg msgSent = new UserMsg(myUser, friend, inputField.getText(), nowDate, color, fontSize.getText(), fontFamilyChooser.getValue().toString());
        return msgSent;
    }

    /*================== sending file=============*/
    @FXML
    public void sendFileAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        Node source = (Node) event.getSource();

        File sentFile = fileChooser.showOpenDialog(source.getScene().getWindow());
        if (sentFile != null) {
            //System.out.println("sending " + fileChooser.getSelectedFile().getName() + "...");
            new Thread(new Runnable() {
                public void run() {
                    FileInputStream in;
                    try {

                        in = new FileInputStream(sentFile.getAbsoluteFile());
                        byte[] mydata = new byte[1024 * 1024];
         
       //                 homeConroller.sendFileToClientChat(uEmail, in, mydata, sentFile.getName());

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TabNodeController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }).start();
        }
        /*
            int myLength;
            try {
                myLength = in.read(mydata);
                while (myLength > 0) {
                if (uEmail != null) {
                    myfriend.receiveFile(fileChooser.getSelectedFile().getName(), mydata, myLength);
                    myLength = in.read(mydata);
                } else {
                    System.out.println("ERROR : your are not connect with any friend");
                    break;
                }

            /*
            int myLength;
            try {
                myLength = in.read(mydata);
                while (myLength > 0) {
                if (uEmail != null) {
                    myfriend.receiveFile(fileChooser.getSelectedFile().getName(), mydata, myLength);
                    myLength = in.read(mydata);
                } else {
                    System.out.println("ERROR : your are not connect with any friend");
                    break;
                }
            }*/
    }


    /*==================== Receive File ========================*/
    public File openReceivedFile(String fileName) {
        //Platform.runLater(new Runnable() {
        //  @Override
        //public void run() {

        // new Thread(task).start();
        try {
            final FutureTask query = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialFileName(fileName);
                    receivedFile = fileChooser.showSaveDialog(outputArea.getScene().getWindow());
                    System.out.println("openReceivedFile recivedFile");
                    System.out.println(receivedFile == null);
                    //   }
                    // });
                    return receivedFile;
                }
            });
            Platform.runLater(query);
            return (File) query.get();
            /*FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileName);
            receivedFile = fileChooser.showSaveDialog(outputArea.getScene().getWindow());
            System.out.println("openReceivedFile recivedFile");
            System.out.println(receivedFile == null);
            //   }
            // });
            return receivedFile;*/
        } catch (InterruptedException ex) {
            Logger.getLogger(TabNodeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(TabNodeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void receiveFileTab(String fileName, byte[] data, int length, File receivedFile) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {
                    receivedFile.getAbsoluteFile().createNewFile();
                    FileOutputStream out = new FileOutputStream(receivedFile, true);
                    out.write(data, 0, length);
                    out.flush();
                    out.close();
                    System.out.println("receiving  data...");

                } catch (IOException ex) {
                    Logger.getLogger(TabNodeController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                FileOutputStream out;
                try {
                    out = new FileOutputStream(receivedFile, true);
                    out.write(data, 0, length);
                    out.flush();
                    out.close();
                    System.out.println("receiving  data...");

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(TabNodeController.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {
                    Logger.getLogger(TabNodeController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

    }

    public void announceReceivingFile() {
        receivedFile = null;
        System.out.println("announceReceivingFile tab");
        System.out.println(receivedFile == null);
        Platform.runLater(() -> {
            Notifications.create()
                    .title("File Received")
                    .text("File Received Successfully")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(5))
                    .showInformation();

            //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });
    }

    /*========================== SAVE CHAT ========================*/
    @FXML
    public void handleSaveChatBtnAction() {
        try {

            JAXBContext context = JAXBContext.newInstance("chatData");
            Unmarshaller unmarsh = context.createUnmarshaller();
            JAXBElement JAXBChat = (JAXBElement) unmarsh.unmarshal(new File("chat.xml"));
            ChatDetail chatDetail = (ChatDetail) JAXBChat.getValue();
            ObjectFactory factory = new ObjectFactory();
            chatDetail.getUsers().getChatCreatorClient().setEmail(myUser.getUserEmail());
            chatDetail.getUsers().getOtherClient().clear();
            UserData otherClient = factory.createUserData();
            otherClient.setEmail(uEmail);
            chatDetail.getUsers().getOtherClient().add(otherClient);
            List chatMessages = chatDetail.getMessages().getMsg();
            chatMessages.clear();
            // ObjectFactory factory = new `ectFactory();

            for (int i = 0; i < messages.size(); i++) {
                MsgData newMsg = factory.createMsgData();
                newMsg.setFrom(messages.get(i).getSender().getUserEmail());
                newMsg.setTo(messages.get(i).getReceiver().getUserEmail());
                newMsg.setBody(messages.get(i).getMsg());
                newMsg.setDate(messages.get(i).getMsgDate());
                newMsg.setFont(messages.get(i).getFontFamily());
                newMsg.setSize(messages.get(i).getFontSize());
                newMsg.setColor(messages.get(i).getColor());
                chatMessages.add(newMsg);
            }

            JAXBElement chat = factory.createChat(chatDetail);
            Marshaller marsh = context.createMarshaller();
            marsh.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "chatSchema.xsd");
            marsh.setProperty("com.sun.xml.internal.bind.xmlHeaders",
                    "<?xml-stylesheet type='text/xsl' href='chatStyle.xsl'?>");
            String date = messages.get(messages.size() - 1).getMsgDate();
            String name = date.substring(0, 4) + "-" + date.substring(5, 7) + "-" + date.substring(8, 10) + "," + date.substring(11, 13) + "." + date.substring(14, 16);
            String outputFile = uEmail + name + ".xml";
            marsh.marshal(chat, new FileOutputStream(outputFile));
            /*save chat notification*/
            Platform.runLater(() -> {
                Notifications.create()
                        .title("Save Chat")
                        .text("Chat is saved successfully")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(5))
                        .showConfirm();

            });

        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

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
