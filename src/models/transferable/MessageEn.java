/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.transferable;

import java.sql.Timestamp;

/**
 *
 * @author Dulanjaya
 */
public class MessageEn {
    private String messageContent;
    private Boolean isReceived;
    private Timestamp time;
    private String type;

    public MessageEn(String messageContent, Boolean isReceived, Timestamp time, String type) {
        //the class message is for the sql return of data to models
        this.messageContent = messageContent;
        this.isReceived = isReceived;
        this.time = time;
        this.type = type;
    }

    /**
     * @return the messageContent
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * @return the isReceived
     */
    public Boolean getIsReceived() {
        return isReceived;
    }

    /**
     * @return the time
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    
}
