/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.fileManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.connectionManagement.ConnectionManager;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

/**
 *
 * @author Dulanjaya
 */
public class FileManager {

    ConnectionManager connectionManager;
    FileTransferManager ftm;

    public FileManager() {
        connectionManager = ConnectionManager.getConnectionManager();
        ftm = new FileTransferManager(connectionManager.getXMPPConnection());
    }

    public void sendFile(File file, String sender) throws FileNotFoundException {
        //enabling services for file sharing
        ServiceDiscoveryManager sdm = ServiceDiscoveryManager.getInstanceFor(connectionManager.getXMPPConnection());

        if (sdm == null) {
            sdm = new ServiceDiscoveryManager(connectionManager.getXMPPConnection());
            System.out.println(sdm.getFeatures());
        }

        sdm.addFeature("http://jabber.org/protocol/disco#info");
        sdm.addFeature("jabber:iq:privacy");
        FileTransferNegotiator.IBB_ONLY = true;
        FileTransferNegotiator.setServiceEnabled(connectionManager.getXMPPConnection(), true);

        ftm = new FileTransferManager(connectionManager.getXMPPConnection());
        String to = connectionManager.getRoster().getPresence(sender).getFrom();
        System.out.println(to);
        OutgoingFileTransfer transfer = ftm.createOutgoingFileTransfer(to);
        try {
            System.out.println(file.getName());
            transfer.sendFile(file, "Test123");
            Status s = transfer.getStatus();

        } catch (XMPPException e) {
            e.printStackTrace();
        }
        while (!transfer.isDone()) {
            if (transfer.getStatus().equals(Status.error)) {
                System.out.println("ERROR!!! " + transfer.getError());
            } else if (transfer.getStatus().equals(Status.cancelled)
                    || transfer.getStatus().equals(Status.refused)) {
                System.out.println("Cancelled!!! " + transfer.getError());
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        if (transfer.getStatus().equals(Status.refused) || transfer.getStatus().equals(Status.error)
                || transfer.getStatus().equals(Status.cancelled)) {
            System.out.println("refused cancelled error " + transfer.getError());
            System.out.println(transfer.getException());
        } else {
            System.out.println("Success");
        }
    }

    public FileTransferManager getFTM() {
        return ftm;
    }
}
