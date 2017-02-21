/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.*;
import DataTransferObject.*;
import Model.ClientServices;
import Model.Model;
import Model.ServerServices;
import View.ClientChat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.control.TextArea;

/**
 *
 * @author toqae
 */
public class Controller {

    private User user;
    private ArrayList<ContactList> friends;
    //Views & model references 
    private ClientChat clientChat;
    private Model model;
    //Services references    
    private ClientServices service;
    private static Registry registry;
    private static ServerServices servicesRef;
    //for send file 
    boolean firstCall = true;

    /**
     * *
     * Default Constructor will be used once to lookup for server services in
     * registry *
     */
    public Controller(ClientChat clientChat) {
        this.clientChat = clientChat;
        try {
            this.model = new Model(this);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public Controller() {
//        try {

            //lookup on registry for server services 
////            registry = LocateRegistry.getRegistry(1099);
//           
//             registry = LocateRegistry.getRegistry("10.118.49.56",8007);
//            
//            
//            servicesRef = (ServerServices) registry.lookup("chatServices");
//            if (servicesRef != null) {
//                System.out.println("register");
//                //System.out.println(servicesRef.tellClient(new UserMsg(new User("s"), new User("r"), "d")));
//            } else {
//                System.out.println("unregister");
//            }
//
//        } catch (RemoteException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NotBoundException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //homeView.initControllerRef(this);
    }

    public void lookupRegister() {
        try {

            //lookup on registry for server services 
        //registry = LocateRegistry.getRegistry("10.118.49.56",5500);
        //this.model = new Model(this);
        //registry = LocateRegistry.getRegistry();
          
             registry = LocateRegistry.getRegistry("10.118.48.241",9000);
          
          
          servicesRef = (ServerServices) registry.lookup("chatServices");
            if (servicesRef != null) {
                System.out.println("register");
                //System.out.println(servicesRef.tellClient(new UserMsg(new User("s"), new User("r"), "d")));
            } else {
                System.out.println("unregister");
            }

        } catch (RemoteException ex) {
            clientChat.errorConnection();
            //Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            clientChat.errorConnection();
            //Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getMyUserObject() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*======================= Server Terminated Upnormally ===========================*/
    public void closeClientNow() {
        clientChat.closeClient();
    }

    /*======================= SIGNUP / SIGNIN ========================================*/
    public void signUpUser(User userr) {
        this.user = userr;
        try {
            System.out.println("model in controller in signup");
            System.out.println(model == null);
            boolean signupCheck = servicesRef.signUp(model, userr);
            System.out.println("signupCheck: " + signupCheck);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User signInUser(User userObject) {

        User result = null;
        try {

            if (servicesRef == null) {
                clientChat.setIsConnectedFlage(false);

            } else {
                clientChat.setIsConnectedFlage(true);
                
                System.out.println("I'm in ClientController Login() , Comming from ClientChat, going to Server");
                System.out.println("Model = " + model.toString());
                System.out.println("ServiceRef = " + servicesRef.toString());
                
                result = servicesRef.signIn(model, userObject);
                
                this.user = result;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<String> getAllUsers() {
        try {
            ArrayList<String> allUsers = servicesRef.getUsers();
            System.out.println("allusers" + allUsers == null);
            return allUsers;
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     * signOut
     */
    public void signOut() {
        try {
            servicesRef.signOut(user.getUserEmail());
            //tell other (online)friends that this user became offline
            //servicesRef.changeUserStatus(user, "Offline", friends); >> yo may need some changes here 
        } catch (RemoteException ex) {
            System.out.println("Controller.Controller.signOut()");
            ex.printStackTrace();
        }
    }

    /*======================= CONTACTLIST ============================================*/
    public ArrayList<ContactList> getContactListFromServer() {
        try {

            System.out.println("servRef Cont " + servicesRef == null);
            System.out.println("user Cont: " + user == null);
            this.friends = servicesRef.getContactList(user);
            return friends;
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*============================ STATUS ==================================*/
    public void changeMyStatus(String email, String status, List<User> friends) {

        try {

            System.out.println("Controller.Controller.changeMyStatus()");

            if (servicesRef == null) {
                System.out.println("NULL Service");
            } else {
                System.out.println("NOT NULL services");
            }

            servicesRef.changeUserStatus(email, status, friends);
            System.out.println("Controller.Controller.changeMyStatus()");

        } catch (RemoteException exception) {
            exception.printStackTrace();
        }

    }

    public void changeFriendStatusIcon(User user) {
        System.out.println("Controller.Controller.changeFriendStatusIcon()");
        clientChat.changeFriendStatusIcon(user);
    }

    /*========================= FRIEND REQUESTS ===============================*/
    public boolean addNewFriend(User user, String friendEmail) {

        System.out.println("### Controller.Controller.addNewFriend()");

        try {
            ///////////1. check if he is in Online HashMAp
            if (servicesRef == null) {
                System.out.println("ServiceRef is NULL ya 7omaaar ");
            } else {
                System.out.println(servicesRef.toString());
            }

            ClientServices client = servicesRef.checkIfOnline(friendEmail);

            System.out.println(client == null);

            if (client == null) {
                ///// target is not online, check db existence
                boolean exists = servicesRef.checkUserExistence(friendEmail);
                if (!exists) {
                    ////// User doesn't exist 
                    System.out.println("#### Requested User does NOT exists. ");
                    return false;
                } else {
                    /////// he have account but Offline
                    System.out.println("###@@ We left him an Offline friend request");

                    boolean sent = servicesRef.addFriendRequest(user.getUserEmail(), friendEmail);

                    if (sent) {
                        System.out.println("Sent Successfully");
                    } else {
                        System.out.println("Sent before");
                    }

                    return true; /////////// for test
                }
            } else {
                System.out.println("### friend request is sent successfully");

                ///// 1. Wait for friend ACCEPTANCE
                client.showAddFriendRequest(friendEmail, user);

                return true;
            }
        } catch (RemoteException exception) {
            System.out.println("Controller.Controller.addNewFriend()" + exception.toString());
        }
        return false;
    }

    /////////////////  Accepting request and adding to ContactList  ///////////
    public void acceptRequest(User user, User friend) {

        try {

            // 2. If Accepted , Add New Row to DB >> ContactListTable
            int result = servicesRef.addFriend(new ContactList(user, friend, "Friend", "Friend", "NotBlocked"));
            /*
            System.out.println("@@@@ Result of Add in  Db " + result);
            
            /// 3. Append both users in both ContactListGUIs
            
             String friendStatus = servicesRef.getUserStatus(friendEmail);
            String myStatus = servicesRef.getUserStatus(userEmail);
            
            User friendObj = new User(friendEmail, friendStatus);
            User myObj = new User(userEmail, userEmail);
             */
            clientChat.addToContactList(friend); /// to My ListGUI

            ClientServices friendRMI = servicesRef.checkIfOnline(friend.getUserEmail());

            friendRMI.addToGUIList(user);  /// to my friend's List

            servicesRef.deleteFriendRequest(user.getUserEmail(), friend.getUserEmail());

        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void rejectRequest(String userEmail, String friendEmail) {
        try {
            System.out.println("Controller.Controller.rejectRequest()");
            servicesRef.deleteFriendRequest(userEmail, friendEmail);

        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<User> getRequestsFromServer() {
        try {
            System.out.println("servRef Cont " + servicesRef == null);
            System.out.println("user Cont: " + user == null);
            ArrayList<User> friends = servicesRef.getFriendRequests(user.getUserEmail());
            return friends;
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    ////////////////// send friend request to another user
    public void showFriendReq(User friend) {
        clientChat.addToRequestList(friend);
    }

    //////////////// adding friend to my GUI list
    public void addToContactList(User user) {
        clientChat.addToContactList(user);
    }

    /*======================= CHAT ============================================*/
    public void sendMsg(UserMsg userMsg) {

        //1.form the object to send to server
        //UserMsg userMsg = new UserMsg(user ,new User(uemail) , msg, new Date());
        //System.out.println(userMsg.toString());
        //2.call tellClient in server that will send msg to other client
        try {
            //servicesRef.test("demo");
            userMsg.setSender(user);
            servicesRef.tellClient(userMsg);
            System.out.println(servicesRef);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayMsg(UserMsg userMsg) {
        System.out.println("clienChat1: " + clientChat);
        System.out.println("userMsg view: " + userMsg);
        clientChat.displayView(userMsg);
        //homeView.displayView(userMsg.getSender().getUserEmail(), userMsg.getMsg());
    }

    /*=================== Group Chat =====================*/
    public void sendGroupMsg(GroupMsg msg) {

        //1.form the object to send to server
        //UserMsg userMsg = new UserMsg(user ,new User(uemail) , msg, new Date());
        //System.out.println(userMsg.toString());
        //2.call tellClient in server that will send msg to other client
        try {
            System.out.println("cont msg " + msg.getMsg());
            System.out.println(servicesRef);
            servicesRef.tellGroup(msg);

        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayGroupMsg(GroupMsg groupMsg) {
        System.out.println("clienChat1: " + clientChat);
        System.out.println("groupMsg view: " + groupMsg);
        clientChat.displayGroupView(groupMsg);
        //homeView.displayView(userMsg.getSender().getUserEmail(), userMsg.getMsg());
    }

    public ArrayList<String> getOnlineFriendsInContactList() {
        ArrayList<String> onlineFriends = new ArrayList<>();
        try {
            onlineFriends = servicesRef.getOnlineFriends(friends);
            System.out.println("onlinefre cont " + onlineFriends.size());
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return onlineFriends;
    }

    /*=================== Send File=======================*/
    public void sendFileToReceiver(String uEmail, FileInputStream in, byte[] mydata, String sentFile) {
        /*try {
            ClientServices receiverRef = servicesRef.getUserRef(uEmail);
            System.out.println("sendFileToReceiver");
            System.out.println(receiverRef == null);
            System.out.println(uEmail);
            System.out.println(in == null);
            System.out.println(mydata.length);
            System.out.println(sentFile == null);
            
            File receivedFile = receiverRef.openReceivedFile(user.getUserEmail(),sentFile);
            int myLength = in.read(mydata);
            while (myLength > 0) {
                if (receiverRef != null) {
                    receiverRef.receiveFile(user.getUserEmail(), sentFile, mydata, myLength, receivedFile);
                    myLength = in.read(mydata);
                } else {
                    System.out.println("ERROR : your are not connect with any friend");
                    break;
                }
            }
            receiverRef.announceReceivingFinished(user.getUserEmail());
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    */
    }

    /*===================== Receive File =========================*/
    public File openReceivedFile(String email, String sentFile){
        return clientChat.openReceivedFile(email, sentFile);
    }
    public void receiveFileController(String uEmail, String fileName, byte[] data, int length, File f) {
        clientChat.receiveFileClientChat(uEmail, fileName, data, length, f);
    }
    public void announceReceivingFinished(String uEmail){
        clientChat.announceReceivingFile(uEmail);
    }
    /*============================== main ========================================*/
    public static void main(String args[]) {
        //Controller controller = new Controller();
        Application.launch(ClientChat.class, args);
    }

    /*================== NOTIFICATIONS ============================*/
    public void showNotification(User u) {
        System.out.println("I'm  " + u.getUserEmail() + " -- In ClientControler :: Coming From Model, Going to ClientChat ");
        clientChat.notifyFriends(u);
    }

    /*==================== ADVERTISEMENT ============================*/
    public void startAnnounceController(byte[] data) {
        System.out.println("Controller.Controller.startAnnounceController()");
        clientChat.startAnnounceClientChat(data);
    }

}
