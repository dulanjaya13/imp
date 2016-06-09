/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.account;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import models.connectionManagement.ConnectionManager;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

/**
 * FXML Controller class
 *
 * @author Dulanjaya
 */
public class AccountViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //models
    VCard myCard = new VCard();
    ConnectionManager connectionManager;

    //view components
    @FXML
    TextField fname;

    @FXML
    TextField lname;

    @FXML
    TextField email;

    @FXML
    TextField tele;

    @FXML
    ImageView pfpic;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connectionManager = ConnectionManager.getConnectionManager();
        try {
            myCard.load(connectionManager.getXMPPConnection());
            fname.setText(myCard.getFirstName());
            lname.setText(myCard.getLastName());
            email.setText(myCard.getEmailHome());
            tele.setText(myCard.getPhoneHome("CELL"));
            if (myCard.getAvatar() != null) {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(myCard.getAvatar()));
                Image image = SwingFXUtils.toFXImage(img, null);
                pfpic.setImage(image);
            }
        } catch (XMPPException ex) {
            Logger.getLogger(AccountViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccountViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            
            //Logger.getLogger(AccountViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void save(ActionEvent event) {
        try {
            myCard.setFirstName(fname.getText());
            myCard.setLastName(lname.getText());
            myCard.setEmailHome(email.getText());
            myCard.setPhoneHome("CELL", tele.getText());
            myCard.save(connectionManager.getXMPPConnection());
        } catch (XMPPException ex) {
            Logger.getLogger(AccountViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void changeProfilePicture() {
        try {
            //loads the image using the filechooser
            FileChooser fc = new FileChooser();
            fc.setTitle("Choose a Profile Picture");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );

            File file = fc.showOpenDialog(pfpic.getScene().getWindow());
            if(file == null) {
                return;
            }
            BufferedImage image = ImageIO.read(file);

            //set the image
            Image imageLoaded = SwingFXUtils.toFXImage(image, null);
            pfpic.setImage(imageLoaded);
            
            //convert to the byte array
            ByteArrayOutputStream convertToByte = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", convertToByte);
            convertToByte.flush();
            byte[] settableProfile = convertToByte.toByteArray();
            convertToByte.close();
            
            myCard.setAvatar(settableProfile);

        } catch (IOException ex) {
            Logger.getLogger(AccountViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
