/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.chatManagement;

import database.access.DBGroupChat;
import exception.NonAuthorizedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.connectionManagement.ConnectionManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 *
 * @author Dulanjaya
 */
public class ChatsManager {

    private ConnectionManager connectionManager;
    private ChatManager chatManager;
    private static String selectedConversation;

    //muc
    private String servicename;
    private static MultiUserChat muc;

    public ChatsManager() {
        connectionManager = ConnectionManager.getConnectionManager();
        chatManager = connectionManager.getChatManager();
    }

    public Chat createChat(String userID) {
        return chatManager.createChat(userID, new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message msg) {

            }
        });
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public void addFriends(String friend) {
        getMuc().invite(friend.concat("/Smack"), "Join Us!");
//        try {
//            // User2 joins the room
//            muc = new MultiUserChat(connectionManager.getXMPPConnection(), conversationname.concat("@conference.dulanjaya-pc"));
//            getMuc().join(friend);
//
//            // User2 listens for invitation rejections
//            getMuc().addInvitationRejectionListener(new InvitationRejectionListener() {
//                public void invitationDeclined(String invitee, String reason) {
//
//                }
//            });
//
//            // User2 invites user3 to join to the room
//            getMuc().invite("user3@host.org/Smack", "Meet me in this excellent room");
//        } catch (XMPPException ex) {
//            Logger.getLogger(ChatsManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void createChatRoomOpen(String conversationname) throws XMPPException{
        try {
            // Create a MultiUserChat using an XMPPConnection for a room
            muc = new MultiUserChat(connectionManager.getXMPPConnection(), conversationname.concat(appdata.AppData.groupConf));
            // Create the room
            getMuc().create(connectionManager.getMyUsername());

            // Get the the room's configuration form
            Form form = getMuc().getConfigurationForm();
            // Create a new form to submit based on the original form
            Form submitForm = form.createAnswerForm();
            // Add default answers to the form to submit
            for (Iterator fields = form.getFields(); fields.hasNext();) {
                FormField field = (FormField) fields.next();
                if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
                    // Sets the default value as the answer
                    submitForm.setDefaultAnswer(field.getVariable());
                }
            }
            // Sets the new owner of the room
            List owners = new ArrayList();
            owners.add(connectionManager.getXMPPConnection().getUser());
            submitForm.setAnswer("muc#roomconfig_publicroom", true);
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);

            // Send the completed form (with default values) to the server to configure the room
            getMuc().sendConfigurationForm(submitForm);
            DBGroupChat.createGroup(getMuc().getRoom());
        } catch (XMPPException ex) {
            throw ex;
        }
    }

    public void createChatRoomClosed(String conversationname, String password) throws XMPPException{
        try {
            // Create a MultiUserChat using an XMPPConnection for a room
            muc = new MultiUserChat(connectionManager.getXMPPConnection(), conversationname.concat(appdata.AppData.groupConf));
            // Create the room
            getMuc().create(connectionManager.getMyUsername());

            // Get the the room's configuration form
            Form form = getMuc().getConfigurationForm();
            // Create a new form to submit based on the original form
            Form submitForm = form.createAnswerForm();
            // Add default answers to the form to submit
            for (Iterator fields = form.getFields(); fields.hasNext();) {
                FormField field = (FormField) fields.next();
                if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
                    // Sets the default value as the answer
                    submitForm.setDefaultAnswer(field.getVariable());
                }
            }
            // Sets the new owner of the room
            List owners = new ArrayList();
            owners.add(connectionManager.getXMPPConnection().getUser());
            submitForm.setAnswer("muc#roomconfig_publicroom", true);
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
            submitForm.setAnswer("muc#roomconfig_roomsecret", password);
            // Send the completed form (with default values) to the server to configure the room
            getMuc().sendConfigurationForm(submitForm);
            DBGroupChat.createGroup(getMuc().getRoom());
        } catch (XMPPException ex) {
            throw ex;
        }
    }

    public MultiUserChat joinRoom(String conversationname) throws NonAuthorizedException{
        try {
            // Create a MultiUserChat using an XMPPConnection for a room
            muc = new MultiUserChat(connectionManager.getXMPPConnection(), conversationname);

            // User2 joins the new room
            // The room service will decide the amount of history to send
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(100);
            muc.join(connectionManager.getMyUsername(), "", history, SmackConfiguration.getPacketReplyTimeout());

            return muc;
        } catch (XMPPException ex) {
            if(ex.getMessage().contains("401")) {
                throw new NonAuthorizedException();
            }
            Logger.getLogger(ChatsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public MultiUserChat joinPrivateRoom(String conversationname, String password) throws NonAuthorizedException{
        try {
            // Create a MultiUserChat using an XMPPConnection for a room
            muc = new MultiUserChat(connectionManager.getXMPPConnection(), conversationname);

            // User2 joins the new room
            // The room service will decide the amount of history to send
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(100);
            muc.join(connectionManager.getMyUsername(), password, history, SmackConfiguration.getPacketReplyTimeout());

            return muc;
        } catch (XMPPException ex) {
            if(ex.getMessage().contains("401")) {
                throw new NonAuthorizedException();
            }
            Logger.getLogger(ChatsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * @return the selectedConversation
     */
    public static String getSelectedConversation() {
        return selectedConversation;
    }

    /**
     * @param selectedConversation the selectedConversation to set
     */
    public static void setSelectedConversation(String selectedConversation) {
        ChatsManager.selectedConversation = selectedConversation;
    }

    /**
     * @return the muc
     */
    public MultiUserChat getMuc() {
        return muc;
    }

}
