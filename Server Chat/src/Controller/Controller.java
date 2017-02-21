/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ClientServices;
import Model.Model;
import Model.ServerServices;
import View.View;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samir
 */
public class Controller {

    Model theModel;
    View theView;

    HashMap<String, ClientServices> clientMap = new HashMap<>();

    /**
     * constructor
     */
    public Controller(View theView) {
        this.theView = theView;
        try {
            theModel = new Model(this);
//            System.out.println("server ready");
//            System.out.println(theModel);
          Registry registry = LocateRegistry.createRegistry(9000);
          //  Registry registry = LocateRegistry.getRegistry();
           registry.rebind("chatServices", theModel);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Controller() {
         try {
            theModel = new Model(this);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
      ///////////////////////////GenderStatistics/////////////////
    public int getMales() {
        return theModel.getMalesDB();
    }
    
     public int getFemales() {
        return theModel.getFemalesDB();
    }
   
     

    ////////////////////////////////////////////////////////////
     
       /////////////////////////UserAvailabilityStatistics////////////////
     public int getOnlines(){
         return theModel.getOnlinesDB();
     }
     public  int getAllUsersNum(){
     
         return theModel.getAllUsersNumDB();
     }
     public int getOfflines(){
         return theModel.getOfflinesDB();
     }
    
    //////////////////////////////////////////////////////////////////


    public void startserver() {
        try {

            theModel = new Model(this);

            Registry registry = LocateRegistry.getRegistry();
            //Registry registry = LocateRegistry.createRegistry(8007);
            registry.rebind("chatServices", theModel);

            System.out.println("Services are Binded Successfully");

        } catch (RemoteException ex) {

            System.err.println("ERROR while binding services " + ex.toString());

        }
    }

    /*============================ Server Closes ===========================*/
    ////////// tell online users that server is going to turn off
    public void closeOnlineUsers() {

//        System.out.println("Model.Model.closeOnlineUsers()");
//        if (onlineUsers == null)
//            System.out.println("==== OnlineUsersHashMAp is NULL yabni  ");
//        System.out.println("------------------------------");
//        
//        onlineUsers.keySet().forEach((key) -> {
//            
//            System.out.println(key + " :: " + onlineUsers.get(key));
//        
//        });
//        
//        System.out.println("-----------------------------------");
//        
    }

    public void stopServer() {
        try {

            Registry registry = LocateRegistry.getRegistry();

            registry.unbind("chatServices");

            System.out.println("server stoped");

        } catch (RemoteException ex) {

            System.out.println("unable to unbin server services");
            ex.printStackTrace();

        } catch (NotBoundException ex) {

            System.out.println("unable to unbin server services");
            ex.printStackTrace();
        }
    }
   
    public void removeFromOnlineUsers(String userEmail){
         clientMap.remove(userEmail);
    }
    /*
        public Set<String> getAllUsers(){
            return clientMap.keySet();
        }
     */
    public void addNewUser(ClientServices clientRef, String uemail) {
        clientMap.put(uemail, clientRef);
        System.out.println("addNewUSer, size: " + clientMap.size());
    }

    public ClientServices getUserRef(String uemail) {
        return clientMap.get(uemail);
    }

    public HashMap<String, ClientServices> getAllUsersRef() {
        return clientMap;
    }

    /*======================== FRIEND REQUESTS =============================*/
    public ClientServices checkIfOnline(String friendEmail) {
        ClientServices clientServices = clientMap.get(friendEmail);
        return clientServices;
    }

    //================================== advertisment===========================
    public void startAnnounceController(byte[] image) {
        System.out.println("hashmap");
        System.out.println(clientMap.size());
        System.out.println(clientMap.keySet().size());
        clientMap.keySet().forEach((String key) -> {
            try {
                System.out.println("Controller.Controller.startAnnounceController()");
                System.out.println(image.toString());
                clientMap.get(key).announceWithAdvertisement(image);
                System.out.println("Controller.Controller.startAnnounceController()");
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }
    /*========================= Statistics ================================*/
    public void updateGenderPieChart(){
        theView.updatePieChart();
    }

    /*========================= MAIN =====================================*/
    public static void main(String[] args) {

        View.launch(View.class, args);

    }

}
