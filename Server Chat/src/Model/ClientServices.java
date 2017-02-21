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
/**
 *
 * @author toqae
 */
public interface ClientServices extends Remote{
    public void showAddFriendRequest(String userEmail, User friend) throws RemoteException;
    public void changeFriendStatusIcon(User user) throws RemoteException;
    public void receiveMsg(UserMsg userMsg)throws RemoteException;
    public void receiveFile(String uEmail, String FileName, byte[] data, int Length) throws RemoteException;
    public void announceReceivingFinished() throws RemoteException;
    public void newNotification (User someFriend)throws RemoteException;
    /*====Group Chat===*/
    public void receiveGroupMsg(GroupMsg groupMsg) throws RemoteException;
    /*====SEVER CLOSES ===*/
    public void closeNow () throws RemoteException;
    /*===FRIEND REQUEST===*/
 public void addToGUIList (User friend) throws RemoteException;
       /*===Advertisment===*/
    public void announceWithAdvertisement(byte[] image) throws RemoteException;
    
    
    
    
}
