/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import DataTransferObject.*;
import ListsRendering.GroupListCallBack;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

/**
 *
 * @author toqae
 */
public class ClientHomeController_2 extends Information implements Initializable {

    @FXML
    private BorderPane rootPane;
    @FXML
    private Label label;
    @FXML
    private TextArea chatInputField;
    @FXML
    private TextArea chatOutputField;
    @FXML
    private Label userName;
    @FXML
    private ListView contactsList;
    @FXML
    private ListView groupsList;
    @FXML
    private ToolBar toolBar;
    /*===Add Friend==*/
    @FXML
    private ListView requestsList;
    @FXML
    private TextField addFriendTextField;
    /*===Status===*/
    @FXML
    private ChoiceBox MyStatus;
    /*===Ads===*/
    @FXML
    private ImageView announceImageView;

    private TabPane tabPane;
    //flags for tabPane
    private boolean firstEnteryContacts = true;
    private boolean firstEnteryGroups = true;
    private boolean firstEntryRequests = true;  //Friend Requests
    //end of flags
    private HashMap<String, Tab> openedTabs = new HashMap<>();
    private HashMap<String, TabNodeController> openedTabNodes = new HashMap<>();

    private HashMap<String, ArrayList<String>> groups = new HashMap<>();
    private HashMap<String, TabGroupNodeController> openedGroupTabNodes = new HashMap<>();

    private ArrayList<String> users;
    private ArrayList<ContactList> contactList;
    /*==Friend Reauests + addFriend ==*/
    private ArrayList<String> friendREquests; ///// holds comming requests
    private ArrayList<User> friendREquests_2;
    List<User> values; ////////////// holds my friends emails
    private User currentUser;

    ArrayList<String> myFriends_2 = new ArrayList<>();
    ObservableList<User> tempValues = FXCollections.observableArrayList();
    ObservableList<User> friendRequestObservable = FXCollections.observableArrayList();
    ObservableList<String> groupObservable = FXCollections.observableArrayList();

    private ClientChat clientChat;
    double xOffset, yOffset;

    public void intializeClientChatRef(ClientChat clientChat) {
        values = new ArrayList<User>();
        this.clientChat = clientChat;
        getContactListFromClientChat();
        prepareContactList();
        getFriendRequestsFromClientChat();
        prepareRequestsList();
        this.currentUser = clientChat.getMyUserObjectFromController();
        userName.setText(currentUser.getUserNickName());
        changeMyStatus("Online");
        MyStatus.getSelectionModel().selectFirst();
        MyStatus.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            System.out.println("New Status is >>>> " + newValue.toString());
            changeMyStatus(newValue.toString());

        });
    }

    public void intializeClientChatRefForSignUp(ClientChat clientChat) {
        values = new ArrayList<User>();
        this.clientChat = clientChat;
        prepareContactList();
        this.currentUser = clientChat.getMyUserObjectFromController();
        userName.setText(currentUser.getUserNickName());
        MyStatus.getSelectionModel().selectFirst();
        MyStatus.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            System.out.println("New Status is >>>> " + newValue.toString());
            changeMyStatus(newValue.toString());

        });
    }

    public User getUserObjectFromChat() {
        return currentUser;
    }

    /*public Stage getHomeStage(){
        
    }*/
 /*For Signout or server !! check later:D */
    void closeUrgently() {
        //////// Show a notification of signing out

        System.out.println("Turning-Off because of Server is Away");
        Platform.runLater(() -> {
            Notifications.create()
                    .title("No Connection")
                    .text("Sorry! Logging out because server issues :(")
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(Duration.seconds(5))
                    .showError();

            //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });

        /////////// log out to the login page
//           try {
//
//            System.out.println("You are going to loginPage");
//
//            //((Node) (event.getSource())).getScene().getWindow().hide();
//
//            
//            
//            FXMLLoader fxml = new FXMLLoader();
//            Parent parent = fxml.load((getClass().getResource("/View/loginFXML.fxml")).openStream());
//            loginFXMLController homeViewCont = fxml.getController();
//            homeViewCont.setControllerRef(clientChat);
//
//            Stage stage = new Stage();
//            stage.setTitle("SitChat");
//            stage.setScene(new Scene(parent));
//            stage.show();
//
//            System.out.println("Showing login page");
//
//        } catch (IOException ex) {
//            System.out.println("ERROR:: " + ex.toString());
//        }
//
    }

    /*============================== STATUS ===================================*/
    public void changeMyStatus(String status) {
        String myEmail = currentUser.getUserEmail();

        clientChat.changeMyStatus(myEmail, status, values);
    }

    public void changeFriendStatusIcon(User friend) {

        System.out.println("Your friend : " + friend.getUserEmail() + " changed his status to " + friend.getUserStatus());

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //int x = tempValues.indexOf(friend);

                int x = myFriends_2.indexOf(friend.getUserEmail());

                System.out.println(">>>>>Your friend : " + friend.getUserEmail() + " changed his status to " + friend.getUserStatus());
                System.out.println(">>>>>Your friend : " + friend.getUserNickName() + " changed his status to " + friend.getUserStatus());

                System.out.println("???? Indx is >> " + x);

                System.out.println("TempSize" + tempValues.size());

                tempValues.set(x, friend);
                System.out.println("New Status Updated in list");
                //contactsList.edit(friendIndex);    
            }
        });

    }

    /*===========================  FRIEND REQUEST =============================*/
    // show friend request Alert
    public void getFriendRequest(String userEmail, String friendEmail) {

    }

    // add to contactsGUI in case of OnlineAcceptence
    public void addToContactList(User friend) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                friend.setUserStatus("Online");
                values.add(friend);
                //add to observableList
                tempValues.add(friend);
                myFriends_2.add(friend.getUserEmail());
            }
        });
    }

    // add to RequestsGUI
    public void addToRequestsList(User friend) {
        System.out.println("View.ClientHomeController_2.addToList()");
        System.out.println("Ready to add " + friend.getUserEmail() + " to your contact list");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                friendRequestObservable.add(friend);
                //requestsList.getItems().add(friend);
            }
        });
    }

    /*public void setCurrentUser(User user) {
        System.out.println("Current user is " + user.getUserEmail()+" "+ user.getUserNickName());
        this.currentUser = user;
    }
     */
    public void getFriendRequestsFromClientChat() {
        this.friendREquests_2 = clientChat.getFriendFromController();
    }

    public void prepareRequestsList() {

        // requestsList.getItems().addAll(friendREquests);
        if (friendREquests_2 != null) {
            friendRequestObservable.addAll(friendREquests_2);
        }
        requestsList.setItems(friendRequestObservable);

        RequestCellCallBack callBack = new RequestCellCallBack();

        callBack.intializeHomeController(this);

        requestsList.setCellFactory(callBack);

        // requestsList.setCellFactory(new RequestCellCallBack());
//
//        requestsList.setCellFactory((ListView<String> param) -> {
//            return new ListCell<String>() {
//
//                String friendMail = "";
//
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//
//                    HBox hbox = new HBox();
//                    Label label = new Label("-");
//                    Button accept = new Button("Accept");
//                    Button reject = new Button("Reject");
//
//                    if (item == null || empty) {
//                        friendMail = null;
//                        setGraphic(null);
//                    } else {
//                        friendMail = item;
//                        label.setText(friendMail + " Wants to..");
//                        hbox.getChildren().addAll(label, accept, reject);
//                        setGraphic(hbox);
//                    }
//
//                    accept.setOnAction(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent event) {
//
//                            System.out.println("## Accepted >>" + friendMail);
//
//                            clientChat.acceptRequest(currentUser.getUserEmail(), friendMail);
//
//                            requestsList.getItems().remove(friendMail);
//
//                        }
//                    });
//
//                    reject.setOnAction(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent event) {
//
//                            System.out.println("XX Rejected >>" + friendMail);
//
//                            clientChat.rejectRequest(currentUser.getUserEmail(), friendMail);
//
//                            requestsList.getItems().remove(friendMail);
//
//                        }
//                    });
//
//                }
//
//            };
//
//        });
        requestsList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue observable, User oldValue, User newValue) {

                System.out.println("Selected Request is " + newValue.getUserEmail());

            }
        });
    }

    //accept REquest 
    public void acceptRequest(User friend) {

        clientChat.acceptRequest(currentUser, friend);
        friendRequestObservable.remove(friend);
        friendREquests_2.remove(friend);
    }

    //reject REquest
    public void rejectRequest(User friend) {

        clientChat.rejectRequest(currentUser.getUserEmail(), friend.getUserEmail());
        friendRequestObservable.remove(friend);
        friendREquests_2.remove(friend);

    }

    @FXML
    public void addNewFriend() {
        String friendEmail = addFriendTextField.getText().trim();
        //////////0. check if you are sending to your self 
        if (friendEmail.equalsIgnoreCase(currentUser.getUserEmail())) {
            System.out.println("Sorry... You can NOT add yourSelf as a friend");
            addFriendTextField.clear();
            return;
        }

        //// 1. check if already a friend >> in contactsArrayList
        boolean found = false;
        if (tempValues != null) {
            for (int i = 0; i < tempValues.size(); i++) {
                if (friendEmail.equalsIgnoreCase(tempValues.get(i).getUserEmail())) {
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            System.out.println("###You are already FRIENDS");
            //return;
        } else {
            //// 2. Go and check for friend acceptence
            System.out.println("### sending " + friendEmail + " a friend request");
            boolean added = clientChat.addNewFriend(currentUser, friendEmail);

        }

        addFriendTextField.clear();

    }

    /*=========================== SIGN OUT ====================================*/
    @FXML
    private void signOutButtonAction(ActionEvent event) {
        try {

            ((Node) (event.getSource())).getScene().getWindow().hide();

            FXMLLoader fxml = new FXMLLoader();
            Parent parent = fxml.load((getClass().getResource("/View/loginFXML.fxml")).openStream());
            loginFXMLController homeViewCont = fxml.getController();
            homeViewCont.setControllerRef(clientChat);

            changeMyStatus("Offline");
            clientChat.signOut();

            Stage stage = new Stage();
            stage.setTitle("MITS");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex) {
            System.out.println("ERROR:: " + ex.toString());
        }

    }

    /*============================ NOTIFICATION ===============================*/
    public void showNotification(User user) {

        System.out.println("Your friend " + user.getUserEmail() + "  is Online NOW");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Notifications.create()
                        .title("New Online Friend")
                        .text("Hi, Your Friend " + user.getUserEmail() + " is Now ONLINE.")
                        .position(Pos.BOTTOM_RIGHT)
                        .hideAfter(Duration.seconds(5))
                        .show();
            }
        });
    }

    /*============================ CHAT =======================================*/
    public void sendMsgToClientChat(UserMsg msg) {
        clientChat.send(msg);
    }

    public void display(UserMsg userMsg) {

        TabNodeController tabNode = openedTabNodes.get(userMsg.getSender().getUserEmail());

        if (tabNode != null) {  //if there's opened Tab

            tabNode.displayMsgInOutputArea(userMsg);

        } else {    //if there's no opened tab
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    if (!firstEnteryContacts) { //there's tabpane before
                        addToTabPane(tabPane, userMsg);

                    } else {    //there's no tabpane before
                        tabPane = new TabPane();
                        //3.format the tabpane so that tabs appears at the bottom
                        tabPane.setSide(Side.BOTTOM);
                        //5.add the tabpane to rootpane
                        rootPane.setCenter(tabPane);
                        addToTabPane(tabPane, userMsg);

                        firstEnteryContacts = false;
                    }

                }
            });
        }

    }

    public void addToTabPane(TabPane tabPane, UserMsg userMsg) {
        //1.build the tab
        Tab tab = addNewTabNode(tabPane, userMsg.getSender());
        //2.add the tab on tabPane
        tabPane.getTabs().add(tab);
        //3.make the tab in focus
        tabPane.getSelectionModel().select(tab);
        //4.display msg 
        TabNodeController tabNode = openedTabNodes.get(userMsg.getSender().getUserEmail());
        tabNode.displayMsgInOutputArea(userMsg);

    }

    /*===================== send File ===========================*/
   /* public void sendFileToClientChat(String uEmail, FileInputStream in, byte[] mydata, String fileName) {
        clientChat.sendFileToController(uEmail, in, mydata, fileName);
    }*/

    /*====================== receive file ========================*/
    public File openReceivedFile(String email, String sentFile){
        TabNodeController tabNodeController = openedTabNodes.get(email);
        return tabNodeController.openReceivedFile(sentFile);
    }
    public void receiveFileHome(String uEmail, String fileName, byte[] data, int length, File receivedFile) {
        TabNodeController tabNodeController = openedTabNodes.get(uEmail);
        System.out.println("openedTabs");
        for (String x : openedTabNodes.keySet()) {
            System.out.println(x);
        }
        System.out.println(uEmail);
        System.out.println("receiveFileHome");
        System.out.println(tabNodeController == null);
        System.out.println("receivedFile");
        System.out.println(receivedFile == null);
        tabNodeController.receiveFileTab(fileName, data, length, receivedFile);
    }
    public void announceReceivingFile(String uEmail){
        System.out.println("announceReceivingFile home");
        System.out.println(uEmail);
        TabNodeController tabNodeController = openedTabNodes.get(uEmail);
        System.out.println(tabNodeController == null);
        tabNodeController.announceReceivingFile();
    }
    /*===================== CONTACTLIST ==========================*/
    public void getContactListFromClientChat() {
        contactList = clientChat.getContactListFromController();

        System.out.println("============================================");
        System.out.println("View.ClientHomeController_2.getContactListFromClientChat()");
        for (int i = 0; i < contactList.size(); i++) {
            values.add(contactList.get(i).getFriend());
        }
    }

    public void prepareContactList() {

        if (contactList != null) {
            for (int i = 0; i < contactList.size(); i++) {
                //add to observable list
                tempValues.add(contactList.get(i).getFriend());
                myFriends_2.add(contactList.get(i).getFriend().getUserEmail());
            }
        }
        contactsList.setItems(tempValues);
        contactsList.setCellFactory(new ListCellCallBack());

        contactsList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<User>() {
            @Override
            //object changed
            public void changed(ObservableValue observable, User oldValue, User newValue) {
                if (firstEnteryContacts) {  //no previous tabpane
                    tabPane = new TabPane();
                    firstEnteryContacts = false;
                }
                if (openedTabNodes.get(newValue.getUserEmail()) == null) {
                    Tab tab = addNewTabNode(tabPane, newValue);
                    tabPane.getTabs().add(tab);
                    //3.format the tabpane so that tabs appears at the bottom
                    tabPane.setSide(Side.BOTTOM);
                    //4.make the tab in focus
                    tabPane.getSelectionModel().select(tab);
                    //5.add the tabpane to rootpane
                    rootPane.setCenter(tabPane);

                }

            }
        });

    }

    public Tab addNewTabNode(TabPane tabPane, User newValue) {
        Tab tab = new Tab();

        FXMLLoader fxml = new FXMLLoader();
        Parent parent;
        try {
            parent = fxml.load((getClass().getResource("/View/TabNode.fxml")).openStream());
            TabNodeController tabNode = fxml.getController();
            tabNode.intializeHomeController(ClientHomeController_2.this);
            tabNode.setUserEmail(newValue.getUserEmail());
            tabNode.setUserObject(newValue);
            tabNode.setMyUserObject(currentUser);

            openedTabNodes.put(newValue.getUserEmail(), tabNode);
            tab.setContent(parent);
            //tab.setText(newValue.getUserNickName());
            tab.setText(newValue.getUserEmail());
            tab.setOnCloseRequest(new EventHandler<Event>() {
                @Override
                public void handle(Event arg0) {
                    System.out.println("close tab");
                    openedTabNodes.remove(tabPane.getSelectionModel().getSelectedItem().getText());
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ClientHomeController_2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tab;
    }

    /*====================== GROUP CHAT ==============================*/
    public void addToGroupTabPane(TabPane tabPane, GroupMsg groupMsg) {
        //1.build the tab
        Tab tab = addNewGroupTabNode(tabPane, groupMsg);
        //2.add the tab on tabPane
        tabPane.getTabs().add(tab);
        //3.make the tab in focus
        tabPane.getSelectionModel().select(tab);
        //4.display msg 
        TabGroupNodeController tabNode = openedGroupTabNodes.get(groupMsg.getGroupName());
        System.out.println("tabNode ");
        System.out.println(tabNode == null);
        tabNode.displayGroupMsgInOutputArea(groupMsg);

    }

    public Tab addNewGroupTabNode(TabPane tabPane, GroupMsg groupMsg) {
        Tab tab = new Tab();

        FXMLLoader fxml = new FXMLLoader();
        Parent parent;
        try {
            parent = fxml.load((getClass().getResource("/View/TabGroupNode.fxml")).openStream());
            TabGroupNodeController tabNode = fxml.getController();
            tabNode.intializeHomeController(ClientHomeController_2.this);
            tabNode.setGroupName(groupMsg.getGroupName());

            openedGroupTabNodes.put(groupMsg.getGroupName(), tabNode);
            tab.setContent(parent);
            tab.setText(groupMsg.getGroupName());
            tab.setOnCloseRequest(new EventHandler<Event>() {
                @Override
                public void handle(Event arg0) {
                    System.out.println("close tab");
                    openedGroupTabNodes.remove(tabPane.getSelectionModel().getSelectedItem().getText());
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ClientHomeController_2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tab;
    }

    public void displayGroup(GroupMsg groupMsg) {

        TabGroupNodeController tabNode = openedGroupTabNodes.get(groupMsg.getGroupName());

        if (tabNode != null) {  //if there's opened Tab

            tabNode.displayGroupMsgInOutputArea(groupMsg);

        } else {    //if there's no opened tab
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    if (!firstEnteryContacts) { //there's tabpane before
                        addToGroupTabPane(tabPane, groupMsg);

                    } else {    //there's no tabpane before
                        tabPane = new TabPane();
                        //3.format the tabpane so that tabs appears at the bottom
                        tabPane.setSide(Side.BOTTOM);
                        //5.add the tabpane to rootpane
                        rootPane.setCenter(tabPane);
                        addToGroupTabPane(tabPane, groupMsg);

                        firstEnteryContacts = false;
                    }
                }
            });
        }

    }

    public void sendGroupMsgToClientChat(GroupMsg msg) {
        System.out.println("groupmsg " + msg.getMsg());
        System.out.println(msg.getMsg() == null);
        clientChat.sendGroup(msg);
    }

    public Tab addNewTabGroupNode(TabPane tabPane, String newValue, ArrayList<String> groupUsers) {
        Tab tab = new Tab();

        FXMLLoader fxml = new FXMLLoader();
        Parent parent;
        try {
            parent = fxml.load((getClass().getResource("/View/TabGroupNode.fxml")).openStream());
            TabGroupNodeController tabNode = fxml.getController();
            tabNode.intializeHomeController(ClientHomeController_2.this);
            tabNode.setUsersInGroup(groupUsers);
            tabNode.setGroupName(newValue);

            openedGroupTabNodes.put(newValue, tabNode);
            tab.setContent(parent);
            tab.setText(newValue);
        } catch (IOException ex) {
            Logger.getLogger(ClientHomeController_2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tab;
    }

    public void addNewGroupAction(ActionEvent e) {
        System.out.println("addNewGroupAction");
        PopOver popOver = new PopOver();
        popOver.setDetachable(true);
        popOver.setAnimated(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);

        ArrayList<String> onlineFriends = clientChat.getOnlineFriendsFromController();
        System.out.println("onlinefr home " + onlineFriends.size());
        // create the data to show in the CheckListView 
        final ObservableList<String> strings = FXCollections.observableArrayList();
        for (int i = 0; i < onlineFriends.size(); i++) {
            strings.add(onlineFriends.get(i));
        }

        VBox popOverContent = new VBox();
        AnchorPane namePane = new AnchorPane();
        TextField nameTextField = new TextField();
        namePane.getChildren().add(nameTextField);

        // Create the CheckListView with the data 
        final CheckListView<String> checkListView = new CheckListView<>(strings);
        checkListView.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                System.out.println(checkListView.getCheckModel().getCheckedItems());

            }
        });

        AnchorPane savePane = new AnchorPane();
        Button saveButton = new Button("SAVE");

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ArrayList<String> usersInGroup = new ArrayList<>();
                for (int i = 0; i < checkListView.getCheckModel().getCheckedItems().size(); i++) {
                    usersInGroup.add(checkListView.getCheckModel().getCheckedItems().get(i));
                    System.out.println(checkListView.getCheckModel().getCheckedItems().get(i));
                }

                if (firstEnteryContacts) {
                    tabPane = new TabPane();
                    firstEnteryContacts = false;
                }
                if (openedTabNodes.get(nameTextField.getText()) == null) {
                    Tab tab = addNewTabGroupNode(tabPane, nameTextField.getText(), usersInGroup);
                    tabPane.getTabs().add(tab);
                    //3.format the tabpane so that tabs appears at the bottom
                    tabPane.setSide(Side.BOTTOM);
                    //4.make the tab in focus
                    tabPane.getSelectionModel().select(tab);
                    //5.add the tabpane to rootpane
                    rootPane.setCenter(tabPane);
                }
                usersInGroup.add(clientChat.getMyUserObjectFromController().getUserEmail());
                groups.put(nameTextField.getText(), usersInGroup);
                groupObservable.add(nameTextField.getText() + ">>" + usersInGroup.size());
                groupsList.setItems(groupObservable);
                groupsList.setCellFactory(new GroupListCallBack());
                //groupsList.getItems().add(nameTextField.getText());
                popOver.hide();
            }
        });
        savePane.getChildren().add(saveButton);

        popOverContent.getChildren().add(namePane);
        popOverContent.getChildren().add(checkListView);
        popOverContent.getChildren().add(savePane);

        popOver.setContentNode(popOverContent);
        popOver.show((Node) e.getSource());

    }

    /*====================== announcing with advertisement ========================*/
    /**
     * announcing with advertisement
     */
    void startAnnounceHome(byte[] data) {
        System.out.println("from home tamam");
        ByteArrayInputStream recivedImage = new ByteArrayInputStream(data);
        System.out.println("recivedImage" + recivedImage);
        announceImageView.setFitWidth(400);
        announceImageView.setFitHeight(176);
        announceImageView.setImage(new Image(recivedImage));
        System.out.println("tstststtsts");

        //contactsList.getChildrenUnmodifiable().add(announceImageView);
        System.out.println("tstststtsts");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //prepareContactList();


        /*
        toolBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = toolBar.getScene().getWindow().getX() - event.getScreenX();
                yOffset = toolBar.getScene().getWindow().getY() - event.getScreenY();
            }
        });
        toolBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                toolBar.getScene().getWindow().setX(event.getScreenX() + xOffset);
                toolBar.getScene().getWindow().setY(event.getScreenY() + yOffset);
            }
        });
         */
    }

}
