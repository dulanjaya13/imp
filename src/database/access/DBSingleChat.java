/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.access;

import database.connection.DBConnector;
import database.handler.DBHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.connectionManagement.ConnectionManager;
import models.transferable.MessageEn;

/**
 *
 * @author Dulanjaya
 */
public class DBSingleChat {

    //this string set will save text message
    private static int user_id = 0;

    private static final String saveTextMessageQuery = "INSERT INTO singlechat (contact_id, sent, type, messagecontent, time_) VALUES (?,?,?,?,?)";

    private static final String getContactIdQuery = "SELECT contact_id FROM contacts WHERE user_id = ? AND contact_name = ?";

    private static final String createContactQuery = "INSERT INTO contacts (user_id, contact_name) VALUES (?,?)";

    private static final String getUserIdQuery = "SELECT user_id FROM user WHERE username = ?";

    private static final String createUserIdQuery = "INSERT INTO USER (username) VALUES (?)";

    private static final String getMessageQuery = "SELECT sent, type, messagecontent, time_ FROM singlechat WHERE contact_id = ? ORDER BY time_ ASC";

    private static final String clearHistoryQuery = "DELETE FROM singlechat WHERE contact_id = ?";

    public static void saveTextMessage(String contactname, boolean isRecieved, String message, Timestamp time) {
        int contact_id = getContactId(contactname);
        if (contact_id == 0) {
            return;
        } else {
            Object[] values = {contact_id, isRecieved, "T", message, time};
            try {
                DBHandler.setData(DBConnector.getDbCon().getConnection(), saveTextMessageQuery, values);
            } catch (SQLException ex) {
                Logger.getLogger(DBSingleChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void savePictureMessage(String contactname, boolean isRecieved, String path, Timestamp time) {
        int contact_id = getContactId(contactname);
        if (contact_id == 0) {
            return;
        } else {
            Object[] values = {contact_id, isRecieved, "P", path, time};
            try {
                DBHandler.setData(DBConnector.getDbCon().getConnection(), saveTextMessageQuery, values);
            } catch (SQLException ex) {
                Logger.getLogger(DBSingleChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void saveFileMessage(String contactname, boolean isRecieved, String path, Timestamp time) {
        int contact_id = getContactId(contactname);
        if (contact_id == 0) {
            return;
        } else {
            Object[] values = {contact_id, isRecieved, "F", path, time};
            try {
                DBHandler.setData(DBConnector.getDbCon().getConnection(), saveTextMessageQuery, values);
            } catch (SQLException ex) {
                Logger.getLogger(DBSingleChat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static ResultSet readMessages(String contactname) {
        int contact_id = getContactId(contactname);
        LinkedList<MessageEn> msgs = new LinkedList<>();

        if (contact_id == 0) {
            return null;
        }

        Object[] values = {contact_id};

        ResultSet rs;
        try {
            rs = DBHandler.getData(DBConnector.getDbCon().getConnection(), getMessageQuery, values);
            return rs;
//            while(rs.next()) {
//                String messageContent = rs.getString("messagecontent");
//                boolean isRecieved = rs.getBoolean("sent");
//                String type = rs.getString("type");
//                Timestamp time = rs.getTimestamp("time_");
//                MessageEn m = new MessageEn(messageContent, isRecieved, time, type);
//                msgs.add(m);
//            }
        } catch (SQLException ex) {
            Logger.getLogger(DBSingleChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void clearHistory(String contactname) {
        //clear the chat history
        int contact_id = getContactId(contactname);

        if (contact_id == 0) {
            return;
        }

        Object[] values = {contact_id};

        try {
            DBHandler.setData(DBConnector.getDbCon().getConnection(), clearHistoryQuery, values);
        } catch (SQLException ex) {
            Logger.getLogger(DBSingleChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int getContactId(String contactName) {
        //get the contact id of the chat
        int contact_id = 0;
        if (user_id == 0) {
            getUserId();
        }
        Object[] values = {user_id, contactName};
        try {
            ResultSet rs = DBHandler.getData(DBConnector.getDbCon().getConnection(), getContactIdQuery, values);
            if (rs.last()) {
                contact_id = rs.getInt("contact_id");
            } else {
                DBHandler.setData(DBConnector.getDbCon().getConnection(), createContactQuery, values);
                rs = DBHandler.getData(DBConnector.getDbCon().getConnection(), getContactIdQuery, values);
                if (rs.last()) {
                    contact_id = rs.getInt("contact_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBSingleChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contact_id;
    }

    private static void getUserId() {
        //get the userId of the current User
        Object[] values = {ConnectionManager.getConnectionManager().getMyUsername()};
        try {
            ResultSet rs = DBHandler.getData(DBConnector.getDbCon().getConnection(), getUserIdQuery, values);
            if (rs.last()) {
                user_id = rs.getInt("user_id");
            } else {
                DBHandler.setData(DBConnector.getDbCon().getConnection(), createUserIdQuery, values);
                rs = DBHandler.getData(DBConnector.getDbCon().getConnection(), getUserIdQuery, values);
                if (rs.last()) {
                    user_id = rs.getInt("user_id");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBSingleChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
