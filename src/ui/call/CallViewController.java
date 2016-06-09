/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.call;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import models.callManagement.Call;
import models.connectionManagement.ConnectionManager;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.jingle.JingleManager;
import org.jivesoftware.smackx.jingle.JingleSession;
import org.jivesoftware.smackx.jingle.JingleSessionRequest;
import org.jivesoftware.smackx.jingle.listeners.JingleSessionRequestListener;
import org.jivesoftware.smackx.jingle.media.JingleMediaManager;
import org.jivesoftware.smackx.jingle.mediaimpl.jmf.JmfMediaManager;
import org.jivesoftware.smackx.jingle.mediaimpl.jspeex.SpeexMediaManager;
import org.jivesoftware.smackx.jingle.mediaimpl.sshare.ScreenShareMediaManager;
import org.jivesoftware.smackx.jingle.nat.BasicTransportManager;
import org.jivesoftware.smackx.jingle.nat.ICETransportManager;
import org.jivesoftware.smackx.provider.JingleProvider;

/**
 * FXML Controller class
 *
 * @author Dulanjaya
 */
public class CallViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private ConnectionManager connectionManager;
    private JingleManager jingle;
    private ICETransportManager ice;

    boolean callStatus = false; //no outgoing call
    List<JingleMediaManager> mediaManagers0 = new ArrayList<JingleMediaManager>();
    JingleManager jm0;
    JingleSession js0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connectionManager = ConnectionManager.getConnectionManager();
        JingleManager.setJingleServiceEnabled();
        addCallRecieveListener();

        // Create a JingleManager using a BasicTransportManager  
        mediaManagers0.add(new JmfMediaManager(new BasicTransportManager()));
        jm0 = new JingleManager(connectionManager.getXMPPConnection(), mediaManagers0);
    }

    public void handleCallButtonAction(ActionEvent event) throws InterruptedException {
        if (Call.dialUser == null || jm0 == null) {
            return;
        }
        if (js0 == null) {
            // Create a new Jingle Call with a full JID  
            try {
                js0 = jm0.createOutgoingJingleSession(Call.dialUser.concat("/Smack"));

                // Start the call  
                js0.startOutgoing();
                Thread.sleep(10000);
                js0.terminate();
                Thread.sleep(3000);
            } catch (XMPPException ex) {
                Logger.getLogger(CallViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (js0.isFullyEstablished()) {
            try {
                js0.terminate();
            } catch (XMPPException e) {

            }
        } else if (js0.isClosed()) {
            // Create a new Jingle Call with a full JID  
            try {
                js0 = jm0.createOutgoingJingleSession(Call.dialUser.concat("/Smack"));

                // Start the call  
                js0.startOutgoing();
                Thread.sleep(10000);
                js0.terminate();
                Thread.sleep(3000);
            } catch (XMPPException ex) {
                Logger.getLogger(CallViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void addCallRecieveListener() {
//        List<JingleMediaManager> mediaManagers1 = new ArrayList<JingleMediaManager>();
//        mediaManagers1.add(new JmfMediaManager(new BasicTransportManager()));
//        final JingleManager jm1 = new JingleManager(connectionManager.getXMPPConnection(), mediaManagers1);
//
//        jm1.addJingleSessionRequestListener(new JingleSessionRequestListener() {
//            @Override
//            public void sessionRequested(JingleSessionRequest jsr) {
//                try {
//                    System.out.println("Got one");
//                    JingleSession session = jsr.accept();
//                    session.startIncoming();
//
//                } catch (XMPPException ex) {
//                    Logger.getLogger(CallViewController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
        //addCallListener();
    }

    private void addCallListener() {
        //when the server is implemented
        ICETransportManager icetm0 = new ICETransportManager(connectionManager.getXMPPConnection(), "10.101.101.1", 3478);
        List<JingleMediaManager> mediaManagers = new ArrayList<JingleMediaManager>();
        //mediaManagers.add(new JmfMediaManager(icetm0));

        mediaManagers.add(new SpeexMediaManager(icetm0));
        mediaManagers.add(new ScreenShareMediaManager(icetm0));
        JingleManager jm = new JingleManager(connectionManager.getXMPPConnection(), mediaManagers);

        jm.addCreationListener(icetm0);

        jm.addJingleSessionRequestListener(new JingleSessionRequestListener() {

            public void sessionRequested(JingleSessionRequest request) {

//                if (incoming != null)
//                    return;
                JingleSession incoming = null;
                try {
                    // Accept the call
                    incoming = request.accept();

                    // Start the call
                    incoming.startIncoming();
                } catch (XMPPException e) {

                }

            }
        }
        );
    }

}
