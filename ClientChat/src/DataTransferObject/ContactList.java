/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTransferObject;

import java.io.Serializable;

/**
 *
 * @author mohamed
 */
public class ContactList implements Serializable{

    private User user;
    private User friend;
    private String userCategory;
    private String friendCategory;
    private String isBlocked;
    
    //// Constructors
    
    public ContactList ()
    {
        user = null;
        friend = null;
        userCategory = "Friends";
        friendCategory = "Friends";
        isBlocked = "None";
    }

    public ContactList ( User user, User friend, String userCategory, String friendCategory, String isBlocked )
    {
        this.user = user;
        this.friend = friend;
        this.userCategory = userCategory;
        this.friendCategory = friendCategory;
        this.isBlocked = isBlocked;
    }

    
    ///// Getters and Setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getFriendCategory() {
        return friendCategory;
    }

    public void setFriendCategory(String friendCategory) {
        this.friendCategory = friendCategory;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }
    
    
    
    
}

