/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.chatManagement;

import java.util.List;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 *
 * @author Dulanjaya
 */
public class GroupChatManager {
    //this class keeps GroupChat objects
    private MultiUserChat muc;
    private List<Message> msgList;

    public GroupChatManager(MultiUserChat muc, List<Message> oldMessages) {
        this.muc = muc;
        this.msgList = oldMessages;
        
        muc.addMessageListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                oldMessages.add(message);
            }
        });
    }
    
    public String getRoom() {
        return getMuc().getRoom();
    }

    /**
     * @return the muc
     */
    public MultiUserChat getMuc() {
        return muc;
    }

    /**
     * @return the msgList
     */
    public List<Message> getMsgList() {
        return msgList;
    }
    
    
}
