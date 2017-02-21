/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTransferObject;

import java.io.Serializable;

/**
 *
 * @author toqae
 */
public class UserMsg implements Serializable{
    private User sender;
    private User receiver;
    private String msg;
    private String msgDate;
    private String color;
    private String fontSize;
    private String fontFamily;

    @Override
    public String toString() {
        return "UserMsg{" + "sender=" + sender + ", receiver=" + receiver + ", msg=" + msg + ", msgDate=" + msgDate + ", color=" + color + ", fontSize=" + fontSize + ", fontFamily=" + fontFamily + '}';
    }


    public UserMsg() {
        sender = null;
        receiver = null;
        msg = null;
        msgDate = null;
    }
     public UserMsg(User sender, User receiver, String msg) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
    }
    
    public UserMsg(User sender, User receiver, String msg, String msgDate) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.msgDate = msgDate;
    }

    public UserMsg(User sender, User receiver, String msg, String msgDate, String color, String fontSize, String fontFamily) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.msgDate = msgDate;
        this.color = color;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
    }
    

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

}
