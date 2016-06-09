/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.main;

import appdata.AppData;
import database.access.DBSingleChat;
import exception.NonAuthorizedException;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import models.callManagement.Call;
import models.chatManagement.ChatsManager;
import models.chatManagement.GroupChatManager;
import models.connectionManagement.ConnectionManager;
import models.contactManagement.ContactsManager;
import models.fileManagement.FileManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.jingle.JingleManager;
import org.jivesoftware.smackx.jingle.JingleSession;
import org.jivesoftware.smackx.jingle.JingleSessionRequest;
import org.jivesoftware.smackx.jingle.listeners.JingleSessionRequestListener;
import org.jivesoftware.smackx.jingle.media.JingleMediaManager;
import org.jivesoftware.smackx.jingle.mediaimpl.jmf.JmfMediaManager;
import org.jivesoftware.smackx.jingle.nat.BasicTransportManager;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.VCard;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ui.call.CallViewController;
import ui.enhancement.BubbleSpec;
import ui.enhancement.BubbledLabel;
import ui.enhancement.CellRenderer;
import ui.enhancement.GroupCellRenderer;

/**
 * FXML Controller class
 *
 * @author Dulanjaya
 */
public class MainViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //Models
    private ConnectionManager connectionManager;
    private ContactsManager contactsManager;
    private ObservableList<RosterEntry> contactList = FXCollections.observableArrayList();
    private ChatsManager chatManager;
    private Chat currentChat;
    private FileManager fileManager;
    private Image defaultAvatar;
    private MultiUserChat currentmuc;

    //common configuration variables
    private boolean isChatUpdated = false;
    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
    private final DateTimeFormatter dtfT = DateTimeFormat.forPattern("hh:mm aa");
    private boolean isSingleChat = true;
    private LinkedList<GroupChatManager> loggedMucs = new LinkedList<>();

    //ViewComponents
    @FXML
    private Label presence;

    @FXML
    private Label name;

    @FXML
    private ListView contacts;

    @FXML
    private Label chatterName;

    @FXML
    private Label chatterPresence;

    @FXML
    private TextField message;

    @FXML
    private ImageView myAvatar;

    @FXML
    private ImageView chatterAvatar;

    @FXML
    private VBox chatList;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button friendRequest;

    @FXML
    private CheckMenuItem history;

    @FXML
    private ListView conversations;

    @FXML
    private ToolBar extraComButtons;

    @FXML
    private CheckMenuItem isnotifiable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialiing the main
        connectionManager = ConnectionManager.getConnectionManager();
        contactsManager = new ContactsManager();
        chatManager = new ChatsManager();
        fileManager = new FileManager();
        //myAccount = new MyAccount();

        //loading the default avatar image
        defaultAvatar = new Image("resources/defaultAvatar.png", 100, 120, true, true);

        showContacts();
        showAccountInfo();
        addContactListener();
        addFileRecieverListener();
        addMessageListener();
        showConversations();
        addRosterListener();
        addConversationListener();
        addCallListener();
        addMessageLimiter();
        setUp();
    }

    //initializing setting
    private void setUp() {
        scrollPane.setFitToWidth(true);
        friendRequest.setVisible(false);
        history.setSelected(true);
        hideExtraComButtons();
        isnotifiable.setSelected(true);
    }

    @FXML
    public void handleExitMenuCall(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void handleFileSendButtonAction() {
        sendFile();
    }

    @FXML
    public void handleSendButtonAction(ActionEvent event) {
        if (isSingleChat) {
            sendMessageSC();
        } else {
            sendMessageGC();
        }
    }

    @FXML
    public void handleTemporyButtonAction(ActionEvent event) {
        Task<Void> presenceWatchTask = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    updateMessage(connectionManager.getMyPresence());
                    Thread.sleep(1000);
                }
                //return null;
            }
        };

        presenceWatchTask.messageProperty().addListener((obs, oldMessage, newMessage) -> presence.setText(newMessage));
        new Thread(presenceWatchTask).start();
    }

    @FXML
    public void handleLogOutMenuCall(ActionEvent event) {
        connectionManager.logOutServer();
        presence.getScene().getWindow().hide();
    }

    @FXML
    public void handleEditMyAccountMenuCall(ActionEvent event) {
        try {
            //default configuration and initialization of the form
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/account/AccountView.fxml"));

            Scene scene = new Scene(root);

            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            stage.setResizable(false);
            stage.setTitle("IMP");
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleCallButtonAction(ActionEvent event) {
        takeCall();
    }

    @FXML
    public void handleContactMenuCall(ActionEvent event) {
        //this method displays the contact view
        try {
            //default configuration and initialization of the form
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/contacts/ContactsView.fxml"));

            Scene scene = new Scene(root);

            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            stage.setResizable(false);
            stage.setTitle("IMP : Contacts");
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleClearHistoryButtonCall(ActionEvent event) {
        clearHistory();
    }

    @FXML
    public void handleNewConvesationMenuCall(ActionEvent event) {
        createNewConversation();
    }

    @FXML
    public void handleSendImageButtonAction(ActionEvent event) {
        sendImage();
    }

    @FXML
    public void handleFriendRequestButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Accept Friend Request!");
        alert.setContentText("Are you sure to add the this user as a friend?");

        ButtonType buttonTpeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("Not Now");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTpeYes, buttonTypeNo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTpeYes) {
            contactsManager.acceptFriend(currentChat.getParticipant());
            friendRequest.setVisible(false);
        } else if (result.get() == buttonTypeNo) {
            // ... no thing to do
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    @FXML
    public void handleAboutMenuCall(ActionEvent event) {
        //this method displays the about
        try {
            //default configuration and initialization of the form
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/about/AboutView.fxml"));

            Scene scene = new Scene(root);

            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            stage.setResizable(false);
            stage.setTitle("About");
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Controller Logic
    private void showAccountInfo() {
        //this method shows the account name and presence
        //create a thread to check the user connected online or not
        Task<Void> presenceWatchTask = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    updateMessage(connectionManager.getMyPresence());
                    Thread.sleep(1000);
                }
                //return null;
            }
        };

        presenceWatchTask.messageProperty().addListener((obs, oldMessage, newMessage) -> presence.setText(newMessage));
        new Thread(presenceWatchTask).start();
        name.setText(connectionManager.getMyUsername());
        try {
            if (connectionManager.getMyProfileAvatar() != null) {
                myAvatar.setImage(connectionManager.getMyProfileAvatar());

            } else {
                myAvatar.setImage(defaultAvatar);
            }
        } catch (IOException e) {
            myAvatar.setImage(defaultAvatar);
        } catch (XMPPException e) {
            myAvatar.setImage(defaultAvatar);
        } catch (NullPointerException e) {
            myAvatar.setImage(defaultAvatar);
        } finally {
            myAvatar.setFitHeight(120);
            myAvatar.setFitWidth(100);
        }
    }

    private void sendMessageSC() {
        if (currentChat == null) {
            return;
        }
        try {
            ItemType type = contactsManager.getUserType(currentChat.getParticipant());
            if (type.equals(ItemType.none)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Attention!");
                alert.setHeaderText("The user is not available for you!");
                alert.setContentText("The user has not accepted the Friend Request."
                        + " The user is not available for you.");
                alert.showAndWait();
                return;
            } else if (type.equals(ItemType.from)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Attention!");
                alert.setHeaderText("An unaccepted user!");
                alert.setContentText("You have not accepted the Friend Request."
                        + " You can not send any messages until you accept the friend request.");
                alert.showAndWait();
                return;
            } else if (type.equals(ItemType.to)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Attention!");
                alert.setHeaderText("The user has not accepted!");
                alert.setContentText("The user has not accepted the Friend Request."
                        + " You can not send any messages until the user accepts the friend request.");
                alert.showAndWait();
                return;
            }
            if (message.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IMP");
                alert.setHeaderText("Message is empty!");
                alert.setContentText("You can not send empty messages.");
                alert.showAndWait();
                return;
            }
            try {
                currentChat.sendMessage(message.getText()); //sends message
                if (history.isSelected()) {
                    DBSingleChat.saveTextMessage(currentChat.getParticipant(), false, message.getText(), new Timestamp(System.currentTimeMillis()));
                }

                paintSendMessage(message.getText().trim(), dtfT.print(DateTime.now())); //paints the sent item
                message.setText("");                        //clears the sent text
                Task tt = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        Thread.sleep(500);
                        return new Object();
                    }
                };

                tt.setOnSucceeded(value -> scrollPane.setVvalue(scrollPane.getHmax()));

                Thread thread1 = new Thread(tt);
                thread1.start();
            } catch (XMPPException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("IMP");
            alert.setHeaderText("User is unavailable!");
            alert.setContentText("The user is unavailable for you!.");
            alert.showAndWait();
            return;
        }

    }

    private void sendFile() {
        if (!contactsManager.getAvailabilityforSharing(currentChat.getParticipant())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("The user is not available!");
            alert.setContentText("The file receiving user should be available online to receive the file.");
            alert.showAndWait();
            return;
        }
        ItemType type = contactsManager.getUserType(currentChat.getParticipant());

        if (type.equals(ItemType.none)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("This user is not available for you!");
            alert.setContentText("The user has not accepted the Friend Request."
                    + " The user is not available for you.");
            alert.showAndWait();
            return;
        } else if (type.equals(ItemType.from)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("An unaccepted user!");
            alert.setContentText("You have not accepted the Friend Request."
                    + " You can not send any messages until you accept the friend request.");
            alert.showAndWait();
            return;
        } else if (type.equals(ItemType.to)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("The user has not accepted!");
            alert.setContentText("The user has not accepted the Friend Request."
                    + " You can not send any messages until the user accepts the friend request.");
            alert.showAndWait();
            return;
        }
        if (!contactsManager.getAvailabilityforSharing(currentChat.getParticipant())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("The user is not available!");
            alert.setContentText("The file receiving user should be available online to receive the file.");
            alert.showAndWait();
            return;
        }
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(presence.getScene().getWindow());
        if (file == null) {
            return;
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fileManager.sendFile(file, currentChat.getParticipant());
                    if (history.isSelected()) {
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        DBSingleChat.saveFileMessage(currentChat.getParticipant(), false, file.getPath(), time);
                    }

                    paintSentFile(currentChat, file.getAbsolutePath(), dtfT.print(DateTime.now()));
                    Task tt = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            Thread.sleep(500);
                            return new Object();
                        }
                    };

                    tt.setOnSucceeded(value -> scrollPane.setVvalue(scrollPane.getHmax()));

                    Thread thread1 = new Thread(tt);
                    thread1.start();
                } catch (FileNotFoundException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("IMP");
                    alert.setHeaderText("File is not Found!");
                    alert.setContentText("File you selected does not exist.");
                    alert.showAndWait();
                    return;
                }
            }
        });
        t.start();
    }

    private void sendImage() {
        if (!contactsManager.getAvailabilityforSharing(currentChat.getParticipant())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("The user is not available!");
            alert.setContentText("The file receiving user should be available online to receive the file.");
            alert.showAndWait();
            return;
        }
        ItemType type = contactsManager.getUserType(currentChat.getParticipant());
        if (type.equals(ItemType.none)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("The is not available for you!");
            alert.setContentText("The user has not accepted the Friend Request."
                    + " The user is not available for you.");
            alert.showAndWait();
            return;
        } else if (type.equals(ItemType.from)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("The user has not accepted!");
            alert.setContentText("The user has not accepted the Friend Request."
                    + " You can not send any messages until the user accepts the friend request.");
            alert.showAndWait();
            return;
        } else if (type.equals(ItemType.to)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention!");
            alert.setHeaderText("The user has not accepted!");
            alert.setContentText("The user has not accepted the Friend Request."
                    + " You can not send any messages until the user accepts the friend request.");
            alert.showAndWait();
            return;
        }
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choose an image to send");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.jpg*", "*.png", "*.bmp"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("BMP", "*.bmp")
            );

            File file = fc.showOpenDialog(presence.getScene().getWindow());
            if (file == null) {
                return;
            }
            BufferedImage image = ImageIO.read(file);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        fileManager.sendFile(file, currentChat.getParticipant());
                        if (history.isSelected()) {
                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            DBSingleChat.savePictureMessage(currentChat.getParticipant(), false, file.getPath(), time);
                        }

                        paintSentPhoto(currentChat, file, dtfT.print(DateTime.now()));

                        Task tt = new Task() {
                            @Override
                            protected Object call() throws Exception {
                                Thread.sleep(500);
                                return new Object();
                            }
                        };

                        tt.setOnSucceeded(value -> scrollPane.setVvalue(scrollPane.getHmax()));

                        Thread thread1 = new Thread(tt);
                        thread1.start();
                    } catch (FileNotFoundException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("IMP");
                        alert.setHeaderText("File is not Found!");
                        alert.setContentText("File you selected does not exist.");
                        alert.showAndWait();
                        return;
                    }
                }
            });
            t.start();
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void takeCall() {
        if (contactsManager.getAvailabilityforCalling(currentChat.getParticipant())) {
            try {
                //default configuration and initialization of the form
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/call/CallView.fxml"));

                Scene scene = new Scene(root);

                Stage stage = new Stage();

                stage.setScene(scene);
                stage.show();

                stage.setResizable(false);
                stage.setTitle("IMP : Voice");
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("IMP");
            alert.setHeaderText("Username is unavailable for calling!");
            alert.setContentText("The user is not available for calling. "
                    + "Please contact the user or the server administrator");
            alert.showAndWait();
        }
    }

    private void showContacts() {
        //display contacts in the contact list view
        List<RosterEntry> col = new ArrayList<RosterEntry>(contactsManager.getContacts());
        Collections.sort(col, (left, right) -> left.getUser().compareTo(right.getUser()));
        contactList = FXCollections.observableArrayList(col);
        contacts.setItems(contactList);
        contacts.setCellFactory(new CellRenderer());
    }

    private void addContactListener() {
        //this method adds a contacts list listener
        System.gc();
        contacts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue == null) {
                    return;
                }
                conversations.getSelectionModel().clearSelection();
                chatList.getChildren().clear();
                isSingleChat = true;
                if (((RosterEntry) newValue).getType().equals(ItemType.from)) {
                    friendRequest.setVisible(true);
                } else {
                    friendRequest.setVisible(false);
                }
                Call.dialUser = ((RosterEntry) newValue).getUser();
                String chatterNameTem = ((RosterEntry) newValue).getUser(); //get reciever
                String chatterNameTem2 = chatterNameTem;

                ResultSet rs = DBSingleChat.readMessages(chatterNameTem);  //load old messages

                chatterNameTem = chatterNameTem.substring(0, chatterNameTem.indexOf("@"));
                chatterNameTem = Character.toUpperCase(chatterNameTem.charAt(0)) + chatterNameTem.substring(1);

                chatterName.setText(chatterNameTem);    //set chat box to recievers contacts
                currentChat = chatManager.createChat(((RosterEntry) newValue).getUser());   //set the current chat
                chatterPresence.setText(contactsManager.getPresence(((RosterEntry) newValue).getUser()));

                VCard vcard = new VCard();      //vcard to load presence
                try {
                    vcard.load(connectionManager.getXMPPConnection(), currentChat.getParticipant());
                    if (vcard.getAvatar() != null) {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(vcard.getAvatar()));
                        Image image = SwingFXUtils.toFXImage(img, null);
                        chatterAvatar.setImage(image);
                    } else {
                        chatterAvatar.setImage(defaultAvatar);
                    }
                } catch (XMPPException ex) {
                    chatterAvatar.setImage(defaultAvatar);
                } catch (IOException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException ex) {
                    chatterAvatar.setImage(defaultAvatar);
                } finally {
                    chatterAvatar.setFitHeight(120);
                    chatterAvatar.setFitWidth(100);
                }

                //display old messages -> current chat is required to be set as a prerequisit
                loadChat(chatterNameTem2);
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        Thread.sleep(500);
                        return new Object();
                    }
                };

                t.setOnSucceeded(value -> scrollPane.setVvalue(scrollPane.getHmax()));

                Thread thread1 = new Thread(t);
                thread1.start();

                //show extra options
                showExtraComButtons();
            }
        });

    }

    private void addFileRecieverListener() {
        //adds a file recieve listener to the application
        FileTransferManager ftm = fileManager.getFTM();
        ftm.addFileTransferListener((FileTransferRequest ftr) -> {
            IncomingFileTransfer transfer = ftr.accept();   //accepts the request

            try {
                //creates file recievedFile with a particular file name
                //stores on the predefined location
                File recivedFile = new File(appdata.AppData.downloadPath + RandomStringUtils.randomAlphanumeric(10).toUpperCase() + ftr.getFileName());
                if (!recivedFile.exists()) {
                    File newFile = new File(appdata.AppData.downloadPath);
                    if (!newFile.exists()) {
                        newFile.mkdir();
                    }
                }

                transfer.recieveFile(recivedFile);  //get the data stream of the message to file

                //shows the file received notification
                showNotifications(ftr.getRequestor(), "You have received a new File");

                if (ftr.getMimeType() == null) {
                    //if the recieved file is not an image then triggers here
                    //file path will be saved in the database by this
                    String participant = ftr.getRequestor();
                    if (participant.contains("/")) {
                        participant = participant.substring(0, participant.indexOf("/"));   //removing the requester's service
                    }
                    Timestamp time = new Timestamp(System.currentTimeMillis()); //time of the recieve is added
                    if (history.isSelected()) {
                        DBSingleChat.saveFileMessage(participant, true, recivedFile.getPath(), time);
                    }
                    DateTime _time = new DateTime(time.getTime());
                    paintReceivedFile(currentChat, recivedFile.getPath(), dtfT.print(_time));
                }
                if (ftr.getMimeType().contains("image")) {
                    //saves the picture as a path in the database
                    String participant = ftr.getRequestor();
                    if (participant.contains("/")) {
                        participant = participant.substring(0, participant.indexOf("/"));   //removing the requester's service
                    }
                    Timestamp time = new Timestamp(System.currentTimeMillis()); //time of the recieve is added
                    if (history.isSelected()) {
                        DBSingleChat.savePictureMessage(participant, true, recivedFile.getPath(), time);    //ask to save
                    }

                    // if the file is an image, displays the image in the chat view
                    if (ftr.getRequestor().contains(currentChat.getParticipant())) {
                        paintReceivedPhotoOnTransmit(currentChat, recivedFile, dtfT.print(DateTime.now()));

                    }

                } else {
                    //if the recieved file is not an image then triggers here
                    //file path will be saved in the database by this
                    String participant = ftr.getRequestor();
                    if (participant.contains("/")) {
                        participant = participant.substring(0, participant.indexOf("/"));   //removing the requester's service
                    }
                    Timestamp time = new Timestamp(System.currentTimeMillis()); //time of the recieve is added
                    if (history.isSelected()) {
                        DBSingleChat.saveFileMessage(participant, true, recivedFile.getPath(), time);
                    }

                    DateTime _time = new DateTime(time.getTime());
                    paintReceivedFile(currentChat, recivedFile.getPath(), dtfT.print(_time));
                }

            } catch (XMPPException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void addMessageListener() {
        //adds a message listener for the chat application

        //remove the offlinemessagelistener to avoid dual listening
        connectionManager.removeOfflinePacketListener();

        //adds the listener
        chatManager.getChatManager().addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean bln) {
                chat.addMessageListener(new MessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message msg) {
                        if (!msg.getBody().equals("")) {
                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            String participant;
                            if (chat.getParticipant().contains("/")) {
                                participant = chat.getParticipant().substring(0, chat.getParticipant().indexOf("/"));
                            } else {
                                participant = chat.getParticipant();
                            }
                            if (history.isSelected()) {
                                DBSingleChat.saveTextMessage(participant, true, msg.getBody(), time);
                            }
                        }
                        if (currentChat != null) {
                            if (chat.getParticipant().contains(currentChat.getParticipant())) {
                                if (!msg.getBody().equals("")) {
                                    DateTime time = DateTime.now();
                                    paintReceivedMessage(chat, msg.getBody(), dtfT.print(time));
                                    Task t = new Task() {
                                        @Override
                                        protected Object call() throws Exception {
                                            Thread.sleep(500);
                                            return new Object();
                                        }
                                    };

                                    t.setOnSucceeded(value -> scrollPane.setVvalue(scrollPane.getHmax()));

                                    Thread thread1 = new Thread(t);
                                    thread1.start();
                                }
                            } else {
                                //shows the file received notification
                                showNotifications(msg.getFrom(), msg.getBody());

                            }
                        } else {
                            //if the current chat is not selected
                            showNotifications(msg.getFrom(), msg.getBody());
                        }
                    }
                });

            }
        });
    }

    private void addConversationListener() {
        conversations.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue == null) {
                    chatterName.setText("");
                    return;
                }
                contacts.getSelectionModel().clearSelection();
                chatList.getChildren().clear();
                chatterName.setText(((HostedRoom) newValue).getName());
                chatterPresence.setText("Group Chat");
                chatterAvatar.setImage(new Image("resources/guest.png", 100, 100, true, true));

                Iterator<GroupChatManager> i = loggedMucs.iterator();
                while (i.hasNext()) {
                    GroupChatManager gcm = i.next();
                    if (gcm.getRoom().equals(((HostedRoom) newValue).getJid())) {
                        currentmuc = gcm.getMuc();
                        //getConversationHistory();
                        reloadConversationMessages(gcm.getMsgList());
                        return;
                    }
                }

                try {
                    currentmuc = chatManager.joinRoom(((HostedRoom) newValue).getJid());
                    loggedMucs.add(new GroupChatManager(currentmuc, getConversationHistory()));
                } catch (NonAuthorizedException e) {

                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Closed Conference Login");
                    dialog.setHeaderText("Password Required!");
                    dialog.setContentText("Password :");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        try {
                            currentmuc = chatManager.joinPrivateRoom(((HostedRoom) newValue).getJid(), result.get());
                            loggedMucs.add(new GroupChatManager(currentmuc, getConversationHistory()));
                        } catch (NonAuthorizedException ex) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Invalid Password!");
                            alert.setHeaderText("Conversation cannot be displayed!");
                            alert.setContentText("The password for the conversation is incorrect. Please try again!");
                            alert.showAndWait();
                            return;
                        }
                    }
                }

                ChatsManager.setSelectedConversation(newValue.toString());
                isSingleChat = false;

                hideExtraComButtons();
                addConversationMessageListener();

            }
        });
    }

    private synchronized void paintDate(Chat chat, String day) {
        //this method draws the recievied text message
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {

                VBox vbox = new VBox(); //to add text

                //chat message
                Label l = new Label(day);
                l.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
                        null, null)));

                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER);
                hbox.getChildren().addAll(l);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());

        });

        if (chat.getParticipant().contains(currentChat.getParticipant())) {
            Thread t = new Thread(recievedMessages);
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //t.setDaemon(true);
            t.start();
            scrollPane.setVvalue(scrollPane.getHmax());
        }
    }

    private synchronized void paintReceivedMessage(Chat chat, String msg, String time) {
        //this method draws the recievied text message
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {

                VBox vbox = new VBox(); //to add text

                ImageView imageRec = new ImageView(chatterAvatar.getImage());   //image
                imageRec.setFitHeight(60);
                imageRec.setFitWidth(50);

                String strChatterName = chat.getParticipant();  //add name of the chatter with light color
                strChatterName = Character.toUpperCase(strChatterName.charAt(0)) + strChatterName.substring(1, strChatterName.indexOf("@"));
                Label chatterName = new Label(strChatterName);
                chatterName.setDisable(true);

                Label timeL = new Label(time);
                timeL.setDisable(true);
                timeL.setFont(new Font("Arial", 10));

                //chat message
                BubbledLabel bbl = new BubbledLabel();
                bbl.setText(msg);
                bbl.setBackground(new Background(new BackgroundFill(Color.GAINSBORO,
                        null, null)));

                vbox.getChildren().addAll(chatterName, bbl, timeL);
                vbox.setAlignment(Pos.CENTER_RIGHT);
                HBox hbox = new HBox();
                bbl.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                hbox.getChildren().addAll(imageRec, vbox);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());

        });

        if (chat.getParticipant().contains(currentChat.getParticipant())) {
            Thread t = new Thread(recievedMessages);
            //t.setDaemon(true);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            scrollPane.setVvalue(scrollPane.getHmax());
        }
    }

    private synchronized void paintWarningInRecieve(Chat chat, String Warning, String time) {
        //this method draws the recievied text message
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {

                VBox vbox = new VBox(); //to add text

                ImageView imageRec = new ImageView(chatterAvatar.getImage());   //image
                imageRec.setFitHeight(60);
                imageRec.setFitWidth(50);

                String strChatterName = chat.getParticipant();  //add name of the chatter with light color
                strChatterName = Character.toUpperCase(strChatterName.charAt(0)) + strChatterName.substring(1, strChatterName.indexOf("@"));
                Label chatterName = new Label(strChatterName);
                chatterName.setDisable(true);

                Label timeL = new Label(time);
                timeL.setDisable(true);
                timeL.setFont(new Font("Arial", 10));

                //chat message
                BubbledLabel bbl = new BubbledLabel();
                bbl.setText(Warning);
                bbl.setBackground(new Background(new BackgroundFill(Color.RED,
                        null, null)));

                vbox.getChildren().addAll(chatterName, bbl, timeL);
                vbox.setAlignment(Pos.CENTER_RIGHT);
                HBox hbox = new HBox();
                bbl.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                hbox.getChildren().addAll(imageRec, vbox);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());
            scrollPane.setVvalue(scrollPane.getHmax());
            scrollPane.setVvalue(scrollPane.getHmax());
        });

        if (chat.getParticipant().contains(currentChat.getParticipant())) {
            Thread t = new Thread(recievedMessages);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //t.setDaemon(true);

        }
    }

    private synchronized void paintReceivedPhotoLoad(Chat chat, File recievedFile, String time) {
        //this method paints the recieved picture message
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {
                Thread.sleep(100);
                VBox vbox = new VBox(); //to add text

                ImageView imageRec = new ImageView(chatterAvatar.getImage());   //image
                imageRec.setFitHeight(60);
                imageRec.setFitWidth(50);

                String strChatterName = chat.getParticipant();  //add name of the chatter with light color
                strChatterName = Character.toUpperCase(strChatterName.charAt(0)) + strChatterName.substring(1, strChatterName.indexOf("@"));
                Label chatterName = new Label(strChatterName);
                chatterName.setDisable(true);

                Label timeL = new Label(time);
                timeL.setDisable(true);
                timeL.setFont(new Font("Arial", 10));

                try {
                    Image recievedImage = SwingFXUtils.toFXImage(ImageIO.read(recievedFile), null);
                    ImageView receivedImageView = new ImageView(recievedImage);
                    receivedImageView.setFitHeight(300);
                    receivedImageView.setFitWidth(300);
                    receivedImageView.setPreserveRatio(true);

                    receivedImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                Desktop dt = Desktop.getDesktop();
                                try {
                                    dt.open(recievedFile);
                                } catch (IOException ex) {
                                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    });

                    receivedImageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            chatList.getScene().setCursor(Cursor.HAND);
                        }
                    });

                    receivedImageView.setOnMouseExited(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            chatList.getScene().setCursor(Cursor.DEFAULT);
                        }
                    });

                    vbox.getChildren().addAll(chatterName, receivedImageView, timeL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vbox.setAlignment(Pos.CENTER_RIGHT);
                HBox hbox = new HBox();
                //bbl.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                hbox.getChildren().addAll(imageRec, vbox);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());
        });

        if (chat.getParticipant().contains(currentChat.getParticipant())) {
            Thread t = new Thread(recievedMessages);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private synchronized void paintReceivedPhotoOnTransmit(Chat chat, File recievedFile, String time) {
        //this method paints the recieved picture message
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {
                VBox vbox = new VBox(); //to add text
                Thread.sleep(2000);
                ImageView imageRec = new ImageView(chatterAvatar.getImage());   //image
                imageRec.setFitHeight(60);
                imageRec.setFitWidth(50);

                String strChatterName = chat.getParticipant();  //add name of the chatter with light color
                strChatterName = Character.toUpperCase(strChatterName.charAt(0)) + strChatterName.substring(1, strChatterName.indexOf("@"));
                Label chatterName = new Label(strChatterName);
                chatterName.setDisable(true);

                Label timeL = new Label(time);
                timeL.setDisable(true);
                timeL.setFont(new Font("Arial", 10));

                try {
                    Image recievedImage = SwingFXUtils.toFXImage(ImageIO.read(recievedFile), null);
                    ImageView receivedImageView = new ImageView(recievedImage);
                    receivedImageView.setFitHeight(300);
                    receivedImageView.setFitWidth(300);
                    receivedImageView.setPreserveRatio(true);

                    receivedImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                Desktop dt = Desktop.getDesktop();
                                try {
                                    dt.open(recievedFile);
                                } catch (IOException ex) {
                                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    });

                    receivedImageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            chatList.getScene().setCursor(Cursor.HAND);
                        }
                    });

                    receivedImageView.setOnMouseExited(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            chatList.getScene().setCursor(Cursor.DEFAULT);
                        }
                    });

                    vbox.getChildren().addAll(chatterName, receivedImageView, timeL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vbox.setAlignment(Pos.CENTER_RIGHT);
                HBox hbox = new HBox();
                hbox.getChildren().addAll(imageRec, vbox);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());
        });
        //set the received chat message appear on the screen
        if (chat.getParticipant().contains(currentChat.getParticipant())) {
            Thread t = new Thread(recievedMessages);
            t.start();
        }
        Task t = new Task() {
            @Override
            protected Object call() throws Exception {
                Thread.sleep(2500);
                return new Object();
            }
        };

        t.setOnSucceeded(value -> scrollPane.setVvalue(scrollPane.getHmax()));

        Thread thread1 = new Thread(t);
        thread1.start();
    }

    private synchronized void paintReceivedFile(Chat chat, String path, String time) {
        //this method paints the recieved picture message
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {
                Thread.sleep(100);
                VBox vbox = new VBox(); //to add text

                ImageView imageRec = new ImageView(chatterAvatar.getImage());   //image
                imageRec.setFitHeight(60);
                imageRec.setFitWidth(50);

                String strChatterName = chat.getParticipant();  //add name of the chatter with light color
                strChatterName = Character.toUpperCase(strChatterName.charAt(0)) + strChatterName.substring(1, strChatterName.indexOf("@"));
                Label chatterName = new Label(strChatterName);
                chatterName.setDisable(true);

                Label timeL = new Label(time);
                timeL.setDisable(true);
                timeL.setFont(new Font("Arial", 10));

                Hyperlink link = new Hyperlink("File Recieved: " + path);
                link.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
                    @Override
                    public void handle(javafx.scene.input.MouseEvent event) {
                        try {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                Runtime.getRuntime().exec("explorer.exe /select," + path);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                vbox.getChildren().addAll(chatterName, link, timeL);
                vbox.setAlignment(Pos.CENTER_RIGHT);
                HBox hbox = new HBox();
                //bbl.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                hbox.getChildren().addAll(imageRec, vbox);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());
        });

        if (chat.getParticipant().contains(currentChat.getParticipant())) {
            Thread t = new Thread(recievedMessages);
            //t.setDaemon(true);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //set the received chat message appear on the screen
        if (chat.getParticipant().contains(currentChat.getParticipant())) {
            Thread t = new Thread(recievedMessages);
            t.start();
        }
        Task t = new Task() {
            @Override
            protected Object call() throws Exception {
                Thread.sleep(2500);
                return new Object();
            }
        };

        t.setOnSucceeded(value -> scrollPane.setVvalue(scrollPane.getHmax()));

        Thread thread1 = new Thread(t);
        thread1.start();

    }

    private synchronized void paintSentPhoto(Chat chat, File file, String time) {
        //this method paints the recieved picture message
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {
//                Thread.sleep(100);
                VBox vbox = new VBox(); //to add text

                ImageView imageRec = new ImageView(myAvatar.getImage());   //image
                imageRec.setFitHeight(60);
                imageRec.setFitWidth(50);

                Label chatterName = new Label(name.getText());
                chatterName.setDisable(true);

                Label timeL = new Label(time);
                timeL.setDisable(true);
                timeL.setFont(new Font("Arial", 10));

                try {
                    Image recievedImage = SwingFXUtils.toFXImage(ImageIO.read(file), null);
                    ImageView receivedImageView = new ImageView(recievedImage);
                    receivedImageView.setFitHeight(300);
                    receivedImageView.setFitWidth(300);
                    receivedImageView.setPreserveRatio(true);

                    receivedImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                Desktop dt = Desktop.getDesktop();
                                try {
                                    dt.open(file);
                                } catch (IOException ex) {
                                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    });

                    receivedImageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            chatList.getScene().setCursor(Cursor.HAND);
                        }
                    });

                    receivedImageView.setOnMouseExited(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            chatList.getScene().setCursor(Cursor.DEFAULT);
                        }
                    });

                    vbox.getChildren().addAll(chatterName, receivedImageView, timeL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vbox.setAlignment(Pos.CENTER_LEFT);
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_RIGHT);
                //bbl.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                hbox.getChildren().addAll(vbox, imageRec);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());
        });

        if (chat.getParticipant().contains(currentChat.getParticipant())) {
            Thread t = new Thread(recievedMessages);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private synchronized void paintSendMessage(String msg, String time) {
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {
                VBox vbox = new VBox(); //to add text

                ImageView imageRec = new ImageView(myAvatar.getImage());
                imageRec.setFitHeight(60);
                imageRec.setFitWidth(50);

                Label myName = new Label(name.getText());
                myName.setDisable(true);

                Label timeL = new Label(time);
                timeL.setDisable(true);
                timeL.setFont(new Font("Arial", 10));

                BubbledLabel bbl = new BubbledLabel();
                bbl.setText(msg);
                bbl.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,
                        null, null)));
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.TOP_RIGHT);
                bbl.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);

                vbox.getChildren().addAll(myName, bbl, timeL);

                hbox.getChildren().addAll(vbox, imageRec);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());
        });
        Thread t = new Thread(recievedMessages);
        t.setDaemon(true);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void paintSentFile(Chat chat, String path, String time) {
        //this method paints the recieved picture message
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {
                Thread.sleep(100);
                VBox vbox = new VBox(); //to add text

                ImageView imageRec = new ImageView(myAvatar.getImage());   //image
                imageRec.setFitHeight(60);
                imageRec.setFitWidth(50);

                Label timeL = new Label(time);
                timeL.setDisable(true);
                timeL.setFont(new Font("Arial", 10));

                Label chatterName = new Label(name.getText());
                chatterName.setDisable(true);

                Hyperlink link = new Hyperlink("File Sent: " + path);
                link.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
                    @Override
                    public void handle(javafx.scene.input.MouseEvent event) {
                        try {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                Runtime.getRuntime().exec("explorer.exe /select," + path);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                vbox.getChildren().addAll(chatterName, link, timeL);
                vbox.setAlignment(Pos.CENTER_LEFT);
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_RIGHT);
                //bbl.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                hbox.getChildren().addAll(vbox, imageRec);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());
        });

        if (chat.getParticipant().contains(currentChat.getParticipant())) {
            Thread t = new Thread(recievedMessages);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void showConversations() {
        //show conversations in the conversaation list view
        try {
            ObservableList<HostedRoom> convs = FXCollections.observableArrayList();
            Collection<HostedRoom> c = MultiUserChat.getHostedRooms(connectionManager.getXMPPConnection(),
                    "conference.".concat(connectionManager.getXMPPConnection().getServiceName()));
            if (c == null) {

            } else if (c.isEmpty()) {

            } else {
                Iterator<HostedRoom> i = c.iterator();
                while (i.hasNext()) {
                    convs.add(i.next());
                }
                conversations.setItems(convs);
                conversations.setCellFactory(new GroupCellRenderer());
            }
        } catch (XMPPException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearHistory() {
        //clears the chat history
        if (currentChat == null) {
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm to Clear History");
            alert.setContentText("Are you sure to clear the current history?");

            ButtonType buttonTpeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTpeYes, buttonTypeNo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTpeYes) {
                DBSingleChat.clearHistory(currentChat.getParticipant());
                chatList.getChildren().clear();
            } else if (result.get() == buttonTypeNo) {
                // ... no thing to do
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }

    private void loadChat(String chatterName) {
        if (chatterName.contains("/")) {
            chatterName = chatterName.substring(0, chatterName.indexOf("/"));   //removing the requester's service
        }
        ResultSet rs = DBSingleChat.readMessages(chatterName);  //load old messages
        //display old messages -> current chat is required to be set as a prerequisit
        String messageContent;
        boolean isRecieved;
        String type;
        Timestamp time;
        DateTime pre = null;
        DateTime cur;
        try {
            if (rs == null) {
                return;
            }
            while (rs.next()) {
                messageContent = rs.getString("messagecontent");
                isRecieved = rs.getBoolean("sent");
                type = rs.getString("type");

                //time of each message
                time = rs.getTimestamp("time_");

                cur = new DateTime(time.getTime());
                if (null == pre) {

                    pre = cur;
                    paintDate(currentChat, dtf.print(cur));
                } else if (pre.toLocalDate().compareTo(cur.toLocalDate()) != 0) {
                    //creates a new date to print
                    pre = cur;
                    paintDate(currentChat, dtf.print(cur));

                }
                if (type.equals("T")) {
                    if (isRecieved) {
                        paintReceivedMessage(currentChat, messageContent, dtfT.print(cur));
                    } else {
                        paintSendMessage(messageContent, dtfT.print(cur));
                    }
                } else if (type.equals("P")) {
                    if (isRecieved) {
                        File file = new File(messageContent);
                        if (!file.exists()) {
                            paintWarningInRecieve(currentChat, "Photo Cannot be Loaded!", dtfT.print(cur));
                        } else {
                            paintReceivedPhotoLoad(currentChat, file, dtfT.print(cur));
                        }
                    } else {
                        File file = new File(messageContent);
                        if (!file.exists()) {
                            paintWarningInRecieve(currentChat, "Photo Cannot be Loaded!", dtfT.print(cur));
                        } else {
                            paintSentPhoto(currentChat, file, dtfT.print(cur));
                        }
                    }
                } else if (type.equals("F")) {
                    if (isRecieved) {
                        File file = new File(messageContent);
                        if (!file.exists()) {
                            paintWarningInRecieve(currentChat, "File (" + messageContent + ") does not exist!", dtfT.print(cur));
                        } else {
                            paintReceivedFile(currentChat, messageContent, dtfT.print(cur));
                        }
                    } else {
                        File file = new File(messageContent);
                        if (!file.exists()) {
                            paintWarningInRecieve(currentChat, "File (" + messageContent + ") does not exist!", dtfT.print(cur));
                        } else {
                            paintReceivedFile(currentChat, messageContent, dtfT.print(cur));
                        }
                        paintSentFile(currentChat, file.getPath(), dtfT.print(cur));
                    }
                }
            }
            if (null == pre) {
                pre = new DateTime(System.currentTimeMillis());
                paintDate(currentChat, dtf.print(pre));
            } else if (pre.toLocalDate().compareTo((new DateTime(System.currentTimeMillis())).toLocalDate()) != 0) {
                pre = new DateTime(System.currentTimeMillis());
                paintDate(currentChat, dtf.print(pre));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createNewConversation() {
        //this method displays the new convesation create view
        try {
            //default configuration and initialization of the form
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/conversation/ConversationView.fxml"));

            Scene scene = new Scene(root);

            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            stage.setResizable(false);
            stage.setTitle("IMP : Conversation Creation");

            final Stage mainStage = (Stage) presence.getScene().getWindow();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    showConversations();
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addRosterListener() {
        //when roster changes occur, the changes are tracked in here
        contactsManager.getRoster().addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<String> clctn) {
                java.util.Iterator<String> i = clctn.iterator();

                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return new Object();
                    }
                };

                t.setOnSucceeded(value -> {
                    showContacts();
                    while (i.hasNext()) {
                        String user = i.next();
                        if (contactsManager.getUserType(user).equals(ItemType.from)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Attention!");
                            alert.setHeaderText("Friend Request!");
                            alert.setContentText("You have a new friend Request from "
                                    + user + ". Please check Contacts.");
                            alert.showAndWait();
                        }
                    }
                });
                Thread thread1 = new Thread(t);
                thread1.start();
            }

            @Override
            public void entriesUpdated(Collection<String> clctn) {
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return new Object();
                    }
                };

                t.setOnSucceeded(value -> showContacts());
                Thread thread1 = new Thread(t);
                thread1.start();
            }

            @Override
            public void entriesDeleted(Collection<String> clctn) {
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return new Object();
                    }
                };

                t.setOnSucceeded(value -> showContacts());
                Thread thread1 = new Thread(t);
                thread1.start();
            }

            @Override
            public void presenceChanged(Presence prsnc) {
                if (currentChat != null) {
                    if (prsnc.getFrom().contains(currentChat.getParticipant())) {
                        Task<String> t = new Task<String>() {
                            @Override
                            protected String call() throws Exception {
                                return prsnc.toString();
                            }
                        };
                        t.setOnSucceeded(value -> chatterPresence.setText(t.getValue()));

                        Thread thread = new Thread(t);
                        thread.start();
                    }

                }
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return new Object();
                    }
                };

                t.setOnSucceeded(value -> showContacts());
                Thread thread1 = new Thread(t);
                thread1.start();
            }
        });
    }

    private void sendMessageGC() {
        //this method sends messages to the group chat
        if (currentmuc == null) {
            return;
        }
        if (message.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("IMP");
            alert.setHeaderText("Message is empty!");
            alert.setContentText("You can not send empty messages.");
            alert.showAndWait();
            return;
        }
        try {
            currentmuc.sendMessage(message.getText());
            message.setText("");
        } catch (XMPPException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Message> getConversationHistory() {
        //get the group chat hostory from the server
        List<Message> msgList = new LinkedList<>();
        int i = 0;
        while (i < 100) {
            i++;
            try {
                Message msg = currentmuc.nextMessage(100);
                msgList.add(msg);
                paintConversationMessage(msg);
                if (msg == null) {
                    throw new NullPointerException();
                }
            } catch (NullPointerException e) {
                break;
            }

        }
        return msgList;
    }

    private void paintConversationMessage(Message msg) {
        //this method draws the recievied text message
        Task<HBox> recievedMessages = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {

                VBox vbox = new VBox(); //to add text
                String user = msg.getFrom();
                user = user.substring(user.indexOf("/") + 1, user.length());
                ImageView imageView = new ImageView();   //image
                imageView.setFitHeight(60);
                imageView.setFitWidth(50);
                VCard vcard = new VCard();
                try {
                    vcard.load(ConnectionManager.getConnectionManager().getXMPPConnection(), user.concat(AppData.serviceNameAt));
                    if (vcard.getAvatar() != null) {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(vcard.getAvatar()));
                        Image image = SwingFXUtils.toFXImage(img, null);
                        imageView.setImage(image);
                    } else {
                        Image defaultAvatar = new Image("resources/defaultAvatar.png", 50, 60, true, true);
                        imageView.setImage(defaultAvatar);
                    }
                } catch (XMPPException e) {
                    Image defaultAvatar = new Image("resources/defaultAvatar.png", 50, 60, true, true);
                    imageView.setImage(defaultAvatar);
                    System.out.println(e);
                }

                Label chatterName = new Label(user);
                chatterName.setDisable(true);

                //chat message
                BubbledLabel bbl = new BubbledLabel();
                bbl.setText(msg.getBody());
                bbl.setBackground(new Background(new BackgroundFill(Color.GAINSBORO,
                        null, null)));

                vbox.getChildren().addAll(chatterName, bbl);
                vbox.setAlignment(Pos.CENTER_RIGHT);
                HBox hbox = new HBox();
                bbl.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                hbox.getChildren().addAll(imageView, vbox);
                return hbox;
            }
        };

        recievedMessages.setOnSucceeded(event -> {
            chatList.getChildren().add(recievedMessages.getValue());

        });

        Thread t = new Thread(recievedMessages);
        //t.setDaemon(true);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Task t1 = new Task() {
            @Override
            protected Object call() throws Exception {
                Thread.sleep(500);
                return new Object();
            }
        };

        t1.setOnSucceeded(value -> scrollPane.setVvalue(scrollPane.getHmax()));

        Thread thread1 = new Thread(t1);
        thread1.start();
    }

    private void showExtraComButtons() {
        //shows the single chat only features
        extraComButtons.setVisible(true);
    }

    private void hideExtraComButtons() {
        //hide single chat only features in group chat
        extraComButtons.setVisible(false);
    }

    private void addConversationMessageListener() {
        //when conversation messages are gaththered, the messages are tracked in here
        if (currentmuc == null) {
            return;
        } else {
            currentmuc.addMessageListener(new PacketListener() {
                @Override
                public void processPacket(Packet packet) {

                    Message message = (Message) packet;

                    if (message.getFrom()
                            .contains(currentmuc.getRoom())) {
                        paintConversationMessage(message);
                    } else {
                        String conv = message.getFrom();
                        conv = conv.substring(0, conv.indexOf("@"));
                        String ms = message.getFrom();
                        ms = ms.substring(ms.indexOf("/") + 1, ms.length());
                        showNotifications(conv, ms + ": " + message.getBody());
                    }

                }
            });
        }
    }

    private void reloadConversationMessages(List<Message> msgList) {
        //reload the chat history from the second time
        //the chat is stored in the application and reload from the application
        Iterator i = msgList.iterator();
        while (i.hasNext()) {
            paintConversationMessage((Message) i.next());
        }
    }

    private void showNotifications(String chat, String message) {
        // a general method for view notifications -> windows notification
        if (!isnotifiable.isSelected()) {
            return;
        }
        URL url = System.class.getResource("/resources/LogoTransparent.png");
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(url);
        TrayIcon trayIcon = new TrayIcon(image, "Instant Messenger Plus");
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);

            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }

            trayIcon.displayMessage("IMP: " + chat, message, TrayIcon.MessageType.INFO);
        }
    }

    private void addCallListener() {
        //system notifies the incomming calls
        List<JingleMediaManager> mediaManagers1 = new ArrayList<JingleMediaManager>();
        mediaManagers1.add(new JmfMediaManager(new BasicTransportManager()));
        final JingleManager jm1 = new JingleManager(connectionManager.getXMPPConnection(), mediaManagers1);

        jm1.addJingleSessionRequestListener(new JingleSessionRequestListener() {
            @Override
            public void sessionRequested(JingleSessionRequest jsr) {
                try {

                    System.out.println("Got one");
                    JingleSession session = jsr.accept();
                    session.startIncoming();

                    Task t = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            return new Object();
                        }
                    };

                    t.setOnSucceeded(value -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Incoming Call");
                        alert.setHeaderText("You have an incomming call from " + session.getInitiator());

                        ButtonType buttonTpeYes = new ButtonType("Answer");
                        ButtonType buttonTypeNo = new ButtonType("Reject");
                        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                        alert.getButtonTypes().setAll(buttonTpeYes, buttonTypeNo, buttonTypeCancel);

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonTpeYes) {
                            session.startIncoming();    //accept the call and answer the call

                        } else if (result.get() == buttonTypeNo) {
                            try {
                                session.terminate();
                            } catch (XMPPException ex) {
                                Alert alertError = new Alert(Alert.AlertType.ERROR);
                                alertError.setTitle("Error Occured");
                                alertError.setHeaderText("Call failure occured with " + ex.getMessage());
                            }
                        } else {
                            return;
                        }
                    });

                    Thread thread = new Thread(t);
                    thread.start();

                    Task t1 = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            return new Object();
                        }
                    };

                    t1.setOnSucceeded(value -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Incoming Call");
                        alert.setHeaderText("Your Call  " + session.getInitiator());

                        ButtonType buttonTypeDisconnect = new ButtonType("Disconnect");

                        alert.getButtonTypes().setAll(buttonTypeDisconnect);

                        Optional<ButtonType> result = alert.showAndWait();
                        try {
                            session.terminate();
                        } catch (XMPPException ex) {
                        }
                    });

                    if (session.isFullyEstablished()) {
                        Thread thread1 = new Thread(t1);
                        thread.start();
                    }
                } catch (XMPPException ex) {
                    Logger.getLogger(CallViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void addMessageLimiter() {
        //this method limits the message that can be send over
        message.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (message.getText().length() > 60) {
                    String s = message.getText().substring(0, 60);
                    message.setText(s);
                }
            }
        });
    }
}
