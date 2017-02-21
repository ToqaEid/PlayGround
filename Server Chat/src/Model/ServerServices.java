/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import DataTransferObject.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 *
 * @author Samir
 */
public interface ServerServices extends Remote{
    void test(String msg) throws RemoteException;
    boolean signUp(ClientServices clientRef, User newUser) throws RemoteException;
    User signIn(ClientServices clientRef,User user) throws RemoteException;
    ArrayList<ContactList> getContactList(User user) throws RemoteException;
    ArrayList<UserMsg> getOfflineRequests(User user) throws RemoteException;
    int addFriend(ContactList list) throws RemoteException;
    void registerFriendInContactList(ContactList list) throws RemoteException;
    void signOut(String userEmail) throws RemoteException;
    public void changeUserStatus(String userEmail, String status, List<User> contacts) throws RemoteException;
    void tellClient(UserMsg msg) throws RemoteException; //for testing changes 
    ArrayList<String> getUsers() throws RemoteException;  //for Testing
    /*For File Transfer*/
    ClientServices getUserRef(String uEmail) throws RemoteException;
    /*for group chat*/
    ArrayList<String> getOnlineFriends(ArrayList<ContactList> friendsList) throws RemoteException ;
    void tellGroup(GroupMsg msg) throws RemoteException;
    /*====Client Requests ===*/
    boolean checkUserExistence (String userEmail) throws RemoteException;
    ClientServices checkIfOnline (String userEmail) throws RemoteException;
    boolean addFriendRequest (String userEmail, String friendEmail) throws RemoteException;
    public ArrayList<User> getFriendRequests(String userEmail) throws RemoteException;
    void deleteFriendRequest ( String userEmail, String friendEmail ) throws RemoteException;



    String getUserStatus (String email) throws RemoteException;


}
