/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Controller;
import DataTransferObject.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Samir
 */
public class Model extends UnicastRemoteObject implements ServerServices {

    DatabaseHandler databaseHandler;
    Controller controller;
    ClientServices clientServices;
    ///////////////////////////////////////////
    ///////////////////////////////////////////

    static int countOfOnlineUsers = 0;

    public Model(Controller controller) throws RemoteException {
        this.controller = controller;
        System.out.println("Model Object is Created");
        databaseHandler = new DatabaseHandler();
    }

    ///////////////////////////GenderStatistics/////////////////
    public int getMalesDB() {
        return databaseHandler.getMaleUsers();
    }

    public int getFemalesDB() {
        return databaseHandler.getFemaleUsers();
    }

    ////////////////////////////////////////////////////////////
     /////////////////////////UserAvailabilityStatistics////////////////
     public int getOnlinesDB(){
         return databaseHandler.getOnlineUsers();
     }
     public  int getAllUsersNumDB(){
     
         return databaseHandler.getAllUsers();
     }
     public int getOfflinesDB(){
         return databaseHandler.getOfflineUsers();
     }
    
    //////////////////////////////////////////////////////////////////
    /*======================== SIGNUP / SIGNIN =====================================*/
    @Override
    public boolean signUp(ClientServices clientRef, User user) throws RemoteException {

        /* ////// 1. chek if a user exists with the same email
        boolean exists = databaseHandler.checkUserExistance(user.getUserEmail());
        if (exists) {
            //////// User Already Exists with this email  
            return false;
        } ///// 2. if no user exists >>> A. then add new user
        else {
            boolean inserted = databaseHandler.insertNewUser(user);
            if (inserted) {
                controller.addNewUser(clientRef, user.getUserEmail());
                return true;    /////// user inserted successfully
            } else {
                return false;   ///////// db internal error
            }
        }*/
        ////// 1. chek if a user exists with the same email
        boolean exists = databaseHandler.checkUserExistance(user.getUserEmail());
        if (exists) {
            System.out.println("user Already exists");
            //////// User Already Exists with this email  
            return false;
        } ///// 2. if no user exists >>> A. then add new user
        else {
            user.setUserStatus("Online");
            boolean inserted = databaseHandler.insertNewUser(user);
            System.out.println("inserted: " + inserted);
            if (inserted) {

                //onlineUsers.put(user.getUserEmail(), clientRef);
                controller.addNewUser(clientRef, user.getUserEmail());
                controller.updateGenderPieChart();
                //countOfOnlineUsers++;
                return true;    /////// user inserted successfully

            } else {
                return false;   ///////// db internal error
            }

        }

    }

    @Override
    public User signIn(ClientServices clientRef, User user) throws RemoteException {
        
        User u = null;
        ///////////// 0. see if he is already in Online Vector
        //ClientServices temp = onlineUsers.get(user.getUserEmail());
        ClientServices temp = controller.getAllUsersRef().get(user.getUserEmail());
        if (temp != null) { //already logged in 
            User dummy = new User();
            dummy.setUserEmail("Already Loggedin");
            System.out.println("---- Attempt to RE-Login by :: " + user.getUserEmail());
            return dummy;
        } else {    //not in online users

            ///////// 1. check user existence
            boolean exists = databaseHandler.checkUserExistance(user.getUserEmail());
            if (!exists) {  //user doesn't exist
                return null;
            } else {  //////// 2. verify password and email
                User x = databaseHandler.verifyPassword(user.getUserEmail(), user.getUserPassword());
                if (x != null) {    //valid user //matched password
                    //u.setUserStatus("Online");
                    
                    u = databaseHandler.getUserInfo(user.getUserEmail());
                    u.setUserStatus("Online");
                    databaseHandler.updateStatus(u);
                    System.out.println("myUser model");
                    System.out.println(u.getUserEmail());
                    System.out.println(u.getUserNickName());
                    System.out.println(u.getUserStatus());
                    controller.addNewUser(clientRef, u.getUserEmail());

                    countOfOnlineUsers++;

                    ///////////////// NOTIFY friends that I'm Online NOW////////////////////////////////////
                    /////// 1. get them from db
                    ArrayList<ContactList> friends = databaseHandler.getFriends(user.getUserEmail());

                    /////// 2. get their ClientService Obj from onlineMap
                    ArrayList<ClientServices> onlineFriends = new ArrayList<>();

                    for (int i = 0; i < friends.size(); i++) {
                        ///////// 1. check if friend is online 
                        String friendEmail = friends.get(i).getFriend().getUserEmail();
                        //ClientServices currFriend = onlineUsers.get(friendEmail);
                        ClientServices currFriend = controller.getUserRef(friendEmail);
                        if (currFriend != null) {
                            System.out.println("Your friend " + friendEmail + "is Online ");
                            onlineFriends.add(currFriend);
                        }
                    }

                    ///// 3. send them your Notification  
                    for (int i = 0; i < onlineFriends.size(); i++) {
                        System.out.println(onlineFriends.get(i));
                        onlineFriends.get(i).newNotification(user);
                    }
                    return u;

                } else {    //not valid user //unmatched password
                    return null;
                }
            }
        }
    }

    @Override
    public ArrayList<String> getUsers() {
        ArrayList<String> allUsers = new ArrayList<String>(controller.getAllUsersRef().keySet());
        System.out.println("server size get users check: " + allUsers == null);
        return allUsers;
    }

    /*============================= FRIEND REQUEST ==========================*/
    @Override
    public boolean checkUserExistence(String userEmail) throws RemoteException {

        return databaseHandler.checkUserExistance(userEmail);

    }

    @Override
    public ClientServices checkIfOnline(String userEmail) throws RemoteException {

        System.out.println("Model.Model.checkIfOnline()");

        ClientServices client = controller.checkIfOnline(userEmail);;
        System.out.println(client == null);

        return client;
    }

    @Override
    public void deleteFriendRequest(String userEmail, String friendEmail) throws RemoteException {
        System.out.println("Model.Model.deleteFriendRequest()");
        databaseHandler.deleteFriendRequest(userEmail, friendEmail);

    }

    @Override
    public int addFriend(ContactList contact) throws RemoteException {
        /////////// 1. check friend existence in s7s

        boolean exists = databaseHandler.checkUserExistance(contact.getFriend().getUserEmail());
        if (!exists) {
            //return "This user is NOT a member is s7s";
            return -1;
        } ////////// 2. check friendShip between them
        else {
            boolean isFriend = databaseHandler.checkingFriendship(contact.getUser().getUserEmail(), contact.getFriend().getUserEmail());
            if (isFriend) {
                //return "You're Already Friends!";
                return 0;
            } ////////// 3. add new friend in db
            else {
                User user = new User(contact.getUser().getUserEmail(), "", "", "", "", "");
                User friend = new User(contact.getFriend().getUserEmail(), "", "", "", "", "");

                boolean inserted = databaseHandler.insertFriend(new ContactList(user, friend, "Friends", "Friends", "No"));
                if (inserted) //return "You are Now Friends";
                {
                    return 1;
                } else //return "DB_ERROR";
                {
                    return -2;
                }
            }

        }

    }
    ////////////////////// updates in friendRequests

    @Override
    public boolean addFriendRequest(String userEmail, String friendEmail) throws RemoteException {
        return databaseHandler.addFriendRequest(userEmail, friendEmail);
    }

    @Override
    public ArrayList<User> getFriendRequests(String userEmail) throws RemoteException {
        return databaseHandler.getFriendRequests(userEmail);
    }

    @Override
    public ArrayList<UserMsg> getOfflineRequests(User user) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registerFriendInContactList(ContactList list) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*============================= CHANGE USER STATUS ============================*/
    @Override
    public void changeUserStatus(String userEmail, String status, List<User> contacts) throws RemoteException {

        System.out.println("Model.Model.changeUserStatus()");

        ////// 1. change MyStatus in db
        User user = new User();
        user.setUserEmail(userEmail);
        String nickName = databaseHandler.getUserNickNAme(userEmail);
        user.setUserStatus(status);
        user.setUserNickName(nickName);
        
        System.out.println("before DBHandler call");
        databaseHandler.updateStatus(user);
        System.out.println("After DBHandler call");
        ///// 2. change my icon in friends' contact list

        System.out.println("-----------------------------");
        for (int i = 0; i < contacts.size(); i++) {
            //// a. get the client RMI Obj
            ClientServices client = controller.checkIfOnline(contacts.get(i).getUserEmail());
            System.out.println(client == null);
            /// b. call his function to change icon in his contactlist
            if (client != null) {
                client.changeFriendStatusIcon(user);
            }
        }
        System.out.println("-----------------------------------");
        System.out.println("END OF :: Model.Model.changeUserStatus()");
    }

    /////////////////////---- get user's status ----- ////////
    @Override
    public String getUserStatus(String email) throws RemoteException {

        return databaseHandler.getUserStatus(email);

    }

    /*============================ CHAT =====================================*/
    @Override
    public void tellClient(UserMsg msg) throws RemoteException {
        //1.get the reference of receiver from vector  ///check to be added if user is in vector     
        HashMap<String, ClientServices> users = controller.getAllUsersRef();
        ClientServices clientRef = users.get(msg.getReceiver().getUserEmail());
        if (clientRef != null) {
            clientRef.receiveMsg(msg);
        } else {
            clientRef = users.get(msg.getSender().getUserEmail());
            //clientRef.
        }
    }

    @Override
    public void tellGroup(GroupMsg msg) throws RemoteException {
        //1.get the reference of receiver from vector  ///check to be added if user is in vector
        HashMap<String, ClientServices> users = controller.getAllUsersRef();
        for (String groupusers : msg.getUsersInGroup()) {
            for (String u : users.keySet()) {
                if (u.equals(groupusers) && !u.equals(msg.getSender().getUserEmail())) {
                    ClientServices clientRef = users.get(groupusers);
                    if (clientRef != null) {
                        clientRef.receiveGroupMsg(msg);
                    }
                }
            }
        }
    }

    /*For File chat*/
    @Override
    public ClientServices getUserRef(String uEmail) throws RemoteException {
        return controller.getUserRef(uEmail);
    }

    @Override
    public ArrayList<ContactList> getContactList(User user) throws RemoteException {
        ArrayList<ContactList> friends = databaseHandler.getFriends(user.getUserEmail());
        return friends;
    }

    @Override
    public void signOut(String userEmail) throws RemoteException {

        //change status in DB
        User user = new User();
        user.setUserEmail(userEmail);
        user.setUserStatus("Offline");
        databaseHandler.updateStatus(user);
        //remove from online users HashMap
        controller.removeFromOnlineUsers(user.getUserEmail());
        //reduce the number of online users
        countOfOnlineUsers--;

    }

    /*=========================== Group Chat ===============================*/
    @Override
    public ArrayList<String> getOnlineFriends(ArrayList<ContactList> friendsList) throws RemoteException {
        System.out.println("friends size" + friendsList.size());
        HashMap<String, ClientServices> onlineUsers = controller.getAllUsersRef();
        System.out.println("online users size" + onlineUsers.size());
        ArrayList<String> onlineFriends = new ArrayList<>();
        Set<String> onlinUsersEmail = onlineUsers.keySet();
        System.out.println("online users size" + onlinUsersEmail.size());
        for (ContactList user : friendsList) {
            System.out.println(user.getFriend().getUserEmail());
            for (String uemail : onlinUsersEmail) {
                System.out.println(uemail);
                if (uemail.equals(user.getFriend().getUserEmail())) {
                    onlineFriends.add(uemail);
                    System.out.println("added friend onlline");
                }
            }
        }
        System.out.println("onlinefr model " + onlineFriends.size());
        return onlineFriends;
    }

    @Override
    public void test(String msg) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
