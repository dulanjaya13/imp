/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.connectionManagement;

import database.access.DBSingleChat;
import exception.InvalidCredentialsException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;

/**
 *
 * @author Dulanjaya
 */
public class ConnectionManager {

    /*
    Objective of this class is manage the connection with
    the Openfire Server. and provide connection services 
    for the rest of the application services.
     */
    private static ConnectionManager connectionManager;
    private String userName = "test123";
    private String password = "test123";
    private final String serviceName = appdata.AppData.serviceName;    //Server Address
    private XMPPConnection connection;
    private PacketListener offlineMessageListener;

    private ConnectionManager() {

    }

    public static ConnectionManager getConnectionManager() {
        //getter of the singleton instance
        if (connectionManager == null) {
            synchronized (ConnectionManager.class) {
                if (connectionManager == null) {
                    connectionManager = new ConnectionManager();
                }
            }
        }
        return connectionManager;
    }

    public synchronized void connectToServer() {
        //this method establishes the connection
        if (connection == null) {
            connection = new XMPPConnection(serviceName);
        }

        try {
            connection.connect();
        } catch (XMPPException ex) {
            System.out.println(ex);
        }

    }

    public void loginToServer(String userName, String password) throws InvalidCredentialsException {
        this.userName = userName;
        this.password = password;

        if (connection == null) {
            connectToServer();
        } else {
            connection.disconnect();
            connectToServer();
        }
        addOfflineMessageListener();
        //This method is used to Login with user credentials
        try {
            connection.login(userName, password);
            setMyPresence();
        } catch (IllegalStateException e) {
            //Ignore the error
        } catch (XMPPException eXMPPExpException) {
            if (eXMPPExpException.toString().trim().contains("SASL authentication failed using mechanism DIGEST-MD5:")) {
                throw new InvalidCredentialsException();
            }
            eXMPPExpException.printStackTrace();
            //handles the 502 error when cant establish the connection due to no server
        }
    }

    public void logOutServer() {
        if (connection != null) {
            setMyAbsence();
            connection.disconnect();
        }
    }

    public void registerWithServer(String username, String password) throws XMPPException{
        //new user registration
        if (connection == null) {
            connectToServer();
        }

        try {
            connection.connect();
            //account manager creates the XMPP new regeistration service
            AccountManager ac = new AccountManager(connection);
            ac.createAccount(username, password);
        } catch (XMPPException e) {
            throw e;
        }
    }

    public String getMyUsername() {
        return userName;
    }

    public String getMyPresence() {
        return connection.getRoster().getPresence(connection.getUser()).toString();

    }

    public void setMyPresence() {
        Presence presence = new Presence(Presence.Type.available);
        connection.sendPacket(presence);
    }

    public void setMyAbsence() {
        Presence presence = new Presence(Presence.Type.unavailable);
        connection.sendPacket(presence);
    }

    public Image getMyProfileAvatar() throws XMPPException, IOException {
        VCard myCard = new VCard();
        myCard.load(connection);
        if (myCard.getAvatar() != null) {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(myCard.getAvatar()));
            Image image = SwingFXUtils.toFXImage(img, null);

            return image;
        }
        return null;
    }

    public Roster getRoster() {
        if (connection == null) {
            connectToServer();
        }
        return connection.getRoster();
    }

    public ChatManager getChatManager() {
        return connection.getChatManager();
    }

    public XMPPConnection getXMPPConnection() {
        return connection;
    }

    public void addOfflineMessageListener() {
        //this method get all the offline messages
        PacketFilter pk = new MessageTypeFilter(Message.Type.chat);
        offlineMessageListener = new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message receivedMessage = (Message) packet;
                if (!receivedMessage.getBody().equals("")) {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String participant;
                    if (receivedMessage.getFrom().contains("/")) {
                        participant = receivedMessage.getFrom().substring(0, receivedMessage.getFrom().indexOf("/"));
                    } else {
                        participant = receivedMessage.getFrom();
                    }
                    DBSingleChat.saveTextMessage(participant, true, receivedMessage.getBody(), time);
                }
            }
        };
        getXMPPConnection().addPacketListener(offlineMessageListener, pk);
    }
    
    public void removeOfflinePacketListener() {
        try {
            getXMPPConnection().removePacketListener(offlineMessageListener);
        }catch(Exception e) {
            System.out.println(e);
        }
    }
}
