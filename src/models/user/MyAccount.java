/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.user;

import models.connectionManagement.ConnectionManager;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.packet.Presence;

/**
 *
 * @author Dulanjaya
 */
public class MyAccount {
    private ConnectionManager connectionManager;
    private AccountManager am;
    private Presence presence;

    public MyAccount() {
        connectionManager = ConnectionManager.getConnectionManager();
        am = connectionManager.getXMPPConnection().getAccountManager();
        presence = connectionManager.getXMPPConnection().getRoster().getPresence("Me");
    }
    
    public void get() {
        System.out.println(am.getAccountInstructions());
    }
    
    public void setMyPresence() {
        presence.setType(Presence.Type.available);
    }
}
