/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.contactManagement;

import java.util.Collection;
import models.connectionManagement.ConnectionManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smackx.jingle.JingleManager;

/**
 *
 * @author Dulanjaya
 */
public class ContactsManager {

    ConnectionManager connectionManager;
    Roster roster;

    public ContactsManager() {
        connectionManager = ConnectionManager.getConnectionManager();
        createRoster();
    }

    private void createRoster() {
        roster = connectionManager.getRoster();
    }

    public Collection<RosterEntry> getContacts() {
        return roster.getEntries();
    }

    public String getPresence(String user) {
        return roster.getPresence(user).toString();
    }

    public boolean getAvailabilityforSharing(String user) {
        return roster.getPresence(user).isAvailable();
    }

    public boolean getAvailabilityforCalling(String user) {
        if (roster.getPresence(user).isAvailable() && JingleManager.isServiceEnabled(connectionManager.getXMPPConnection(), user)) {
            return true;
        } else {
            return false;
        }

    }

    public void addContact(String userName) {
        //sending a friend request
        Presence request = new Presence(Presence.Type.subscribe);
        request.setTo(userName);
        connectionManager.getXMPPConnection().sendPacket(request);
    }

    public void removeFriend(String userName) {
        RosterPacket packet = new RosterPacket();
        packet.setType(IQ.Type.SET);
        RosterPacket.Item item = new RosterPacket.Item(userName, null);
        item.setItemType(RosterPacket.ItemType.remove);
        packet.addRosterItem(item);
        connectionManager.getXMPPConnection().sendPacket(packet);
    }

    public void getFriendRequest() {

    }

    public Roster getRoster() {
        return roster;
    }

    public RosterPacket.ItemType getUserType(String user) {
        return roster.getEntry(user).getType();
    }

    public void acceptFriend(String user) {
        Presence subscribed = new Presence(Presence.Type.subscribed);
        subscribed.setTo(user);
        connectionManager.getXMPPConnection().sendPacket(subscribed);
        addContact(user);
        roster = connectionManager.getRoster();
    }
    

}
