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
import java.util.logging.Level;
import java.util.logging.Logger;
import models.connectionManagement.ConnectionManager;

/**
 *
 * @author Dulanjaya
 */
public class DBGroupChat {

    //this string set will save text message
    private static int user_id = 0;

    private static final String getUserIdQuery = "SELECT user_id FROM user WHERE username = ?";

    private static final String createUserIdQuery = "INSERT INTO USER (username) VALUES (?)";
    
    private static final String createGroupQuery = "INSERT INTO groupchat (user_id, group_id) VALUES (?, ?)";
    
    private static final String getAllGroupsQuery = "SELECT group_id FROM groupchat WHERE user_id = ?";

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

    public static void createGroup(String groupID) {
        if (user_id == 0) {
            getUserId();
        }
        
        Object[] values = {user_id, groupID};
        try {
            DBHandler.setData(DBConnector.getDbCon().getConnection(), createGroupQuery, values);
        } catch (SQLException ex) {
            Logger.getLogger(DBSingleChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ResultSet getGroups() {
        if (user_id == 0) {
            getUserId();
        }
        
        Object[] values = {user_id };

        ResultSet rs;
        try {
            rs = DBHandler.getData(DBConnector.getDbCon().getConnection(), getAllGroupsQuery, values);
            return rs;

        } catch (SQLException ex) {
            Logger.getLogger(DBSingleChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
