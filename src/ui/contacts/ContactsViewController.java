/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.contacts;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.connectionManagement.ConnectionManager;
import models.contactManagement.ContactsManager;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import ui.enhancement.CellRenderer;

/**
 * FXML Controller class
 *
 * @author Dulanjaya
 */
public class ContactsViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //Models
    private ContactsManager contactsManager;
    private ConnectionManager connectionManager;
    private ObservableList<RosterEntry> contactList = FXCollections.observableArrayList();
    private ObservableList<String> searchList = FXCollections.observableArrayList();

    //others
    private String selectedNewContact = "";
    private String selectedUnfriendContact = "";
    private ChangeListener contactChangeListner;

    //View Controllers
    @FXML
    ListView contacts;

    @FXML
    TextField searchField;

    @FXML
    ListView findings;

    @FXML
    public void handleSearchButtonAction(ActionEvent event) {
        searchUser();
    }

    @FXML
    public void handleAddButtonAction(ActionEvent event) {
        addContact();
    }

    @FXML
    public void handleRemoveButtonAction(ActionEvent event) {
        removeUser();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connectionManager = ConnectionManager.getConnectionManager();
        contactsManager = new ContactsManager();
        showContacts();
        addSearchsListListener();
        addContactListListener();
    }

    private void showContacts() {
        //shows the existing contacts
        List<RosterEntry> col = new ArrayList<RosterEntry>(contactsManager.getContacts());
        Collections.sort(col, (left, right) -> left.getUser().compareTo(right.getUser()));
        contactList = FXCollections.observableArrayList(col);
        contacts.setItems(contactList);
        contacts.setCellFactory(new CellRenderer());
        contacts.refresh();
    }

    private void searchUser() {
        //searches the user by requesting the service from the server
        if (searchField.getText().length() < 5) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("IMP");
            alert.setHeaderText("Search is not Sufficient!");
            alert.setContentText("You should enter a search string with atleast 5 characters.");
            alert.showAndWait();
            return;
        }
        try {
            UserSearchManager search = new UserSearchManager(connectionManager.getXMPPConnection());
            Form searchForm = search
                    .getSearchForm("search." + connectionManager.getXMPPConnection().getServiceName());

            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", this.searchField.getText());
            ReportedData data = search
                    .getSearchResults(answerForm, "search." + connectionManager.getXMPPConnection().getServiceName());
            searchList.clear();
            if (data.getRows() != null) {
                Iterator i = data.getRows();
                while (i.hasNext()) {
                    ReportedData.Row row = (ReportedData.Row) i.next();
                    Iterator j = row.getValues("jid");
                    while (j.hasNext()) {
                        //System.out.println(j.next());
                        searchList.add(j.next().toString());
                    }

                }
            }
            findings.setItems(searchList);
        } catch (XMPPException ex) {
            Logger.getLogger(ContactsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addSearchsListListener() {
        findings.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selectedNewContact = (String) newValue;
            }
        });
    }

    private void addContactListListener() {
        contactChangeListner = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue == null) {
                    return;
                }
                selectedUnfriendContact = ((RosterEntry) newValue).getUser();
            }
        };
        contacts.getSelectionModel().selectedItemProperty().addListener(contactChangeListner);
    }

    private void removeContactListListener() {
        if (contactChangeListner != null) {
            contacts.getSelectionModel().selectedItemProperty().removeListener(contactChangeListner);
        }
    }

    private void addContact() {
        if (selectedNewContact.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("IMP : Contacts");
            alert.setHeaderText("No Contact Selected!");
            alert.setContentText("Please select a contact to add.");
            alert.showAndWait();
            return;
        }
        try {
            RosterPacket.ItemType type = contactsManager.getUserType(selectedNewContact);

            if (type.equals(RosterPacket.ItemType.none)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IMP : Contacts");
                alert.setHeaderText("You have already sent a friend request!");
                alert.setContentText("You have already sent a friend request to this user. Please wait for"
                        + " his acceptance!");
                alert.showAndWait();
                return;

            } else if (type.equals(RosterPacket.ItemType.from)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IMP : Contacts");
                alert.setHeaderText("Already Received the Friend Invitation!");
                alert.setContentText("This user has already sent the friend request. Accept the user!");
                alert.showAndWait();
                return;
            } else if (type.equals(RosterPacket.ItemType.to)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IMP : Contacts");
                alert.setHeaderText("You have already sent a friend request!");
                alert.setContentText("You have already sent a friend request to this user. Please wait for"
                        + " his acceptance!");
                alert.showAndWait();
                return;
            } else if (type.equals(RosterPacket.ItemType.both)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IMP : Contacts");
                alert.setHeaderText("Already a Friend!");
                alert.setContentText("This user is already in your contact list!");
                alert.showAndWait();
                return;
            }
                
        } catch (NullPointerException e) {
            contactsManager.addContact(selectedNewContact);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("IMP : Contacts");
            alert.setHeaderText("Friend Request has been sent!");
            alert.setContentText("Wait until the friend accepts your request.");
            alert.showAndWait();

            removeContactListListener();
            showContacts();
            addContactListListener();
        }

    }

    private void removeUser() {
        //removes the user
        if (selectedUnfriendContact.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("IMP");
            alert.setHeaderText("No Contact Selected!");
            alert.setContentText("Please select a contact to unfriend.");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm to unfriend");
        alert.setContentText("Are you sure to unfriend?");

        ButtonType buttonTpeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTpeYes, buttonTypeNo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTpeYes) {
            if (selectedUnfriendContact.isEmpty()) {
                return;
            } else {
                contactsManager.removeFriend(selectedUnfriendContact);

            }

        } else if (result.get() == buttonTypeNo) {
            // ... no thing to do
        } else {
            // ... user chose CANCEL or closed the dialog
        }
        removeContactListListener();
        Task t = new Task() {
            @Override
            protected Object call() throws Exception {
                Thread.sleep(100);
                return new Object();
            }
        };
        
        t.setOnSucceeded(value -> showContacts());
        
        Thread thread = new Thread(t);
        thread.start();
        showContacts();
        addContactListListener();
    }

}
