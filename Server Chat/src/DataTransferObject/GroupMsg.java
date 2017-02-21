/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTransferObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author toqae
 */
public class GroupMsg implements Serializable{
    private User sender;
    private String GroupName;
    private ArrayList<String> usersInGroup;
    private String msg;
    private String msgDate;
    private String color;
    private String fontSize;
    private String fontFamily;

    public GroupMsg(User sender, String GroupName, ArrayList<String> usersInGroup, String msg) {
        this.sender = sender;
        this.GroupName = GroupName;
        this.usersInGroup = usersInGroup;
        this.msg = msg;
    }

    public GroupMsg(User sender, String GroupName, ArrayList<String> usersInGroup, String msg, String msgDate, String color, String fontSize, String fontFamily) {
        this.sender = sender;
        this.GroupName = GroupName;
        this.usersInGroup = usersInGroup;
        this.msg = msg;
        this.msgDate = msgDate;
        this.color = color;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public ArrayList<String> getUsersInGroup() {
        return usersInGroup;
    }

    public void setUsersInGroup(ArrayList<String> usersInGroup) {
        this.usersInGroup = usersInGroup;
    }
    
    
    
    
}
