/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.conversation;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.chatManagement.ChatsManager;
import models.connectionManagement.ConnectionManager;
import models.contactManagement.ContactsManager;
import org.jivesoftware.smack.XMPPException;

/**
 * FXML Controller class
 *
 * @author Dulanjaya
 */
public class ConversationViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //models
    ConnectionManager connectionManager;
    ChatsManager chatsManager;
    ContactsManager contactsManager;

    @FXML
    TextField cname;

    @FXML
    CheckBox isclosed;

    @FXML
    PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connectionManager = ConnectionManager.getConnectionManager();
        chatsManager = new ChatsManager();
        contactsManager = new ContactsManager();

        addCheckBoxListener();
    }

    @FXML
    public void handleCreateButtonAction(ActionEvent event) {
        createConversation();
    }

    private void addCheckBoxListener() {
        password.setDisable(true);
        isclosed.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == true) {
                    password.setDisable(false);
                } else {
                    password.setDisable(true);
                }
            }
        });
    }

    private void createConversation() {
        if (!isclosed.isSelected()) {
            if (cname.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("IMP : Conversation Create");
                alert.setHeaderText("Conversation name is empty!");
                alert.setContentText("Please type a unique name for you conversation.");
                alert.showAndWait();
                return;
            }
            try {
                chatsManager.createChatRoomOpen(cname.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("IMP : Conversation Created");
                    alert.setHeaderText("You have successfully created the conversation!");
                    alert.setContentText("Please check the conversations list to access your conversation!");
                    alert.showAndWait();
            } catch (XMPPException ex) {
                if (ex.getMessage().contains("Creation failed")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("IMP : Conversation Create");
                    alert.setHeaderText("Conversation name is already taken!");
                    alert.setContentText("Please type a unique name for you conversation. Try again!");
                    alert.showAndWait();
                } else if (ex.getMessage().contains("401")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("IMP : Conversation Create");
                    alert.setHeaderText("Conversation name is already taken!");
                    alert.setContentText("Please type a unique name for you conversation. Try again!");
                    alert.showAndWait();
                } else if (ex.getMessage().contains("400")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("IMP : Conversation Create");
                    alert.setHeaderText("Conversation name is not valid!");
                    alert.setContentText("Please do not include /,@ or spaces in your name!");
                    alert.showAndWait();
                }
                System.out.println(ex);
            }
        } else {
            if (cname.getText().isEmpty() || password.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("IMP : Conversation Create");
                alert.setHeaderText("Conversation name or password is empty!");
                alert.setContentText("Please type a unique name and a strong password for you conversation.");
                alert.showAndWait();
                return;
            }
            try {
                chatsManager.createChatRoomClosed(cname.getText(), password.getText());
            } catch (XMPPException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("IMP : Conversation Create");
                alert.setHeaderText("Conversation name is already taken!");
                alert.setContentText("Please type a unique name for you conversation. Try again!");
                alert.showAndWait();
            }
        }

    }

}
