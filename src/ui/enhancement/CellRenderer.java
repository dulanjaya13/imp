/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.enhancement;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import models.connectionManagement.ConnectionManager;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smackx.packet.VCard;
import ui.main.MainViewController;

/**
 *
 * @author Dulanjaya
 */
public class CellRenderer implements Callback<ListView<RosterEntry>, ListCell<RosterEntry>> {
    @Override
    public ListCell<RosterEntry> call(ListView<RosterEntry> param) {
        ListCell<RosterEntry> cell = new ListCell<RosterEntry>() {

            @Override
            protected void updateItem(RosterEntry user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    setText(user.getUser().substring(0, user.getUser().indexOf("@")).toUpperCase());
                    ImageView imageView = new ImageView();
                    ImageView availability = new ImageView();
                    VCard vcard = new VCard();
                    HBox hbox = new HBox();
                    ImageView newRequest = new ImageView();
                    try {
                        vcard.load(ConnectionManager.getConnectionManager().getXMPPConnection(), user.getUser());
                        if (vcard.getAvatar() != null) {
                            BufferedImage img = ImageIO.read(new ByteArrayInputStream(vcard.getAvatar()));
                            Image image = SwingFXUtils.toFXImage(img, null);
                            imageView.setImage(image);
                        } else {
                            Image defaultAvatar = new Image("resources/defaultAvatar.png", 50, 60, true, true);
                            imageView.setImage(defaultAvatar);
                        }
                        if(ConnectionManager.getConnectionManager().getRoster().getPresence(user.getUser()).isAvailable()) {
                            availability.setImage(new Image("resources/green.png",10,10,true,true));
                        } else {
                            availability.setImage(new Image("resources/red.png",10,10,true,true));
                        }
                        
                        newRequest.setImage(new Image("resources/users-icon.png",20,20,true,true));
                    } catch (XMPPException ex) {
                        Image defaultAvatar = new Image("resources/defaultAvatar.png", 50, 60, true, true);
                        imageView.setImage(defaultAvatar);
                    } catch (IOException ex) {
                        Image defaultAvatar = new Image("resources/defaultAvatar.png", 50, 60, true, true);
                        imageView.setImage(defaultAvatar);
                    } catch (NullPointerException ex) {
                        Image defaultAvatar = new Image("resources/defaultAvatar.png", 50, 60, true, true);
                        imageView.setImage(defaultAvatar);
                    } finally {
                        imageView.setFitHeight(60);
                        imageView.setFitWidth(50);
                        if(user.getType().equals(RosterPacket.ItemType.from)) {
                            hbox.getChildren().addAll(imageView, availability, newRequest);
                        }else {
                            hbox.getChildren().addAll(imageView, availability);
                        }
                        
                    }
                    setGraphic(hbox);
                }
            }
        };
        return cell;
    }

}
