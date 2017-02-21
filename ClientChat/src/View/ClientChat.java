/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import DataTransferObject.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author toqae
 */
public class ClientChat extends Application {

    Controller controller;
    //Views references
    ClientHomeController_2 homeView;
    loginFXMLController loginView;
    signupFXMLController signupView;

    Stage primaryStage;

    public ClientChat() {
        controller = new Controller(this);
        //homeView = new ClientHomeController_2(this);

        //signupView = new signupFXMLController(this);
    }

    public void intializeHomeRef(ClientHomeController_2 homeView) {
        this.homeView = homeView;
    }

    public void intializeSignupRef(signupFXMLController signupView) {
        this.signupView = signupView;
    }

    public void intializeLoginRef(loginFXMLController loginView) {
        this.loginView = loginView;
    }

    public User getMyUserObjectFromController() {
        return controller.getMyUserObject();
    }

    public void setIsConnectedFlage(boolean isConnected) {
        loginView.setIsConnected(isConnected);

    }

    public void errorConnection() {
        loginView.errorConnection();
    }

    public void closeClient() {
        homeView.closeUrgently();
    }
//    public Stage getPrimaryStage(){}

    /*========================== SIGNUP / SIGNIN / SIGNOUT =============================*/
    public void signup(User tempUser) {
        controller.signUpUser(tempUser);
    }

    public User signin(User tempUser) {
        return controller.signInUser(tempUser);
    }

    public void signOut() {
        controller.signOut();
    }

    /*========================== CHAT =============================*/
    public void sendUserToController(User user) {
        controller.setUser(user);
    }

    /*==================== FRIEND REQUESTS ============================*/
    ///////////////////////////// add new friend

    public boolean addNewFriend(User user, String friendEmail) {
        System.out.println("### View.ClientChat.addNewFriend()");
        return controller.addNewFriend(user, friendEmail);
    }

    public void sendFriendReq(String userEmail, String friendEmail) {
        homeView.getFriendRequest(userEmail, friendEmail);

    }

    //////////////// add to my contacts GUI
    public void addToContactList(User friend) {
        homeView.addToContactList(friend);
    }
    //////////////// add to my request GUI

    public void addToRequestList(User friend) {
        homeView.addToRequestsList(friend);
    }

    public void acceptRequest(User user, User friend) {
        controller.acceptRequest(user, friend);
    }

    public void rejectRequest(String userEmail, String friendEmail) {
        System.out.println("View.ClientChat.rejectRequest()");
        controller.rejectRequest(userEmail, friendEmail);
    }

    public ArrayList<User> getFriendFromController() {
        ArrayList<User> friends = controller.getRequestsFromServer();
        return friends;
    }

    /*======================= STATUS ===============================*/
    public void changeMyStatus(String email, String status, List<User> friends) {
        System.out.println("View.ClientChat.changeMyStatus()");
        controller.changeMyStatus(email, status, friends);
    }

    public void changeFriendStatusIcon(User user) {
        System.out.println("View.ClientChat.changeFriendStatusIcon()");
        homeView.changeFriendStatusIcon(user);
    }

    /*==================== CONTACTLIST ================================*/
    public ArrayList<ContactList> getContactListFromController() {
        ArrayList<ContactList> friends = controller.getContactListFromServer();
        return friends;
    }

    /*======================= CHAT ==============================*/
    public void send(UserMsg userMsg) {
        controller.sendMsg(userMsg);
    }

    public void displayView(UserMsg userMsg) {
        homeView.display(userMsg);
    }

    /*====================== GROUP CHAT ==========================*/
    public ArrayList<String> getOnlineFriendsFromController() {
        return controller.getOnlineFriendsInContactList();
    }

    public void sendGroup(GroupMsg msg) {
        System.out.println("controller send " + msg.getMsg());
        System.out.println(msg.getMsg() == null);
        controller.sendGroupMsg(msg);
    }

    public void displayGroupView(GroupMsg groupMsg) {
        homeView.displayGroup(groupMsg);
    }

    /*========================== Send File ========================*/
    public void sendFileToController(String uEmail, FileInputStream in, byte[] mydata, String sentFile) {
        controller.sendFileToReceiver(uEmail, in, mydata, sentFile);
    }

    /*========================== Receive File =====================*/
    public File openReceivedFile(String email, String sentFile){
        return homeView.openReceivedFile(email, sentFile);
    }
    public void receiveFileClientChat(String uEmail, String fileName, byte[] data, int length, File f) {
        homeView.receiveFileHome(uEmail, fileName, data, length, f);
    }

    public ArrayList<String> getUsersFromController() {
        ArrayList<String> allUsers = controller.getAllUsers();
        System.out.println("arraylist size: " + allUsers.size());
        return allUsers;
    }
    public void announceReceivingFile(String uEmail){
        homeView.announceReceivingFile(uEmail);
    }
    /*======================== advertisement =======================*/
    public void startAnnounceClientChat(byte[] data) {
        System.out.println("View.ClientChat.startAnnounceClientChat()");
        homeView.startAnnounceHome(data);
    }

    /*=========================== notify new online friend =======================*/
    public void notifyFriends(User u) {
        System.out.println("I'm  " + u.getUserEmail() + " -- In ClientChat:: Coming From Controller, Going to HomeView ");
        System.out.println("homeRef");
        System.out.println(homeView == null);
        homeView.showNotification(u);
    }

    /*======================== START ===============================*/
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("loginFXML.fxml"));
        FXMLLoader fxml = new FXMLLoader();
        Parent root = fxml.load((getClass().getResource("loginFXML.fxml")).openStream());
        loginView = fxml.getController();
        loginView.setControllerRef(this);
        controller.lookupRegister();
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image("/img/logo7.png"));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        /*   List<String> params  = getParameters().getRaw();
        
        System.out.println("-------++++=====" + params.get(0));
        
         if ( params.get(0).equalsIgnoreCase("not_connected") )
        {
            loginView.setIsConnected(false);
            
        }
        else
        {
            loginView.setIsConnected(true);
            
        }
         */
        stage.show();
        this.primaryStage = stage;
    }
}
