/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Controller;
import DataTransferObject.GroupMsg;
import DataTransferObject.User;
import DataTransferObject.UserMsg;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author toqae
 */
public class Model extends UnicastRemoteObject implements ClientServices {

    private Controller controller;

    public Model(Controller controller) throws RemoteException {
        this.controller = controller;
    }
    @Override
    public void closeNow() throws RemoteException {    
        controller.closeClientNow();
    }
    /*==================== FRIEND REQUESTS =======================*/
      @Override
    public void addToGUIList (User friend) throws RemoteException
    {
        controller.addToContactList(friend);   
    }
    @Override
    public void showAddFriendRequest(String userEmail, User friend) throws RemoteException {
        controller.showFriendReq(friend);
    }

    @Override
    public void changeFriendStatusIcon(User user) throws RemoteException {
        System.out.println("Model.Model.changeFriendStatusIcon()");
        controller.changeFriendStatusIcon(user);   
    }

    @Override
    public void receiveMsg(UserMsg userMsg) throws RemoteException {
        System.out.println("receive");
        System.out.println("controller: " + controller);
        System.out.println("usermsg: " + userMsg.toString());
        controller.displayMsg(userMsg);
    }

    @Override
    public void receiveFile(String receiverEmail, String fileName, byte[] data, int length, File f) throws RemoteException {
        controller.receiveFileController(receiverEmail, fileName, data, length, f);
    }
    /*====================== GROUP CHAT ==========================*/
    @Override
    public void receiveGroupMsg(GroupMsg groupMsg){
        System.out.println("groupmsg display "+ groupMsg.getMsg());
        controller.displayGroupMsg(groupMsg);
    }
    @Override
    public void announceReceivingFinished(String uEmail) throws RemoteException {
        System.out.println("received");
        controller.announceReceivingFinished(uEmail);
    }
    @Override
    public File openReceivedFile(String email, String sentFile) throws RemoteException{
        return controller.openReceivedFile(email, sentFile);
    }
    ///////////////// Notification for new online friend

    @Override
    public void newNotification(User someFriend) throws RemoteException {
        System.out.println("I'm  " + someFriend.getUserEmail() + " -- In ClientModel :: Coming From Server, Going to ClientController ");

        controller.showNotification(someFriend);
    }
    /*======================== ADVERTISEMENT ========================*/
    @Override
    public void announceWithAdvertisement(byte[] data) throws RemoteException {
        System.out.println("Model.Model.announceWithAdvertisement()");
        controller.startAnnounceController(data);
    }
}
