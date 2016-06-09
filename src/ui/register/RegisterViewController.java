/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.register;

import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.connectionManagement.ConnectionManager;
import org.jivesoftware.smack.XMPPException;

/**
 * FXML Controller class
 *
 * @author Dulanjaya
 */
public class RegisterViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //Models
    ConnectionManager connectionManager;

    //View Objects
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 
        connectionManager = ConnectionManager.getConnectionManager();
        addMessageLimiter();
    }

    @FXML
    public void handleRegisterButtonAction(ActionEvent event) {
        //registers the user in the server
        connectionManager.connectToServer();
        if (username.getText().isEmpty() || password.getText().isEmpty()
                || confirmPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("IMP");
            alert.setHeaderText("Username or Password or Confirm Password field is empty!");
            alert.setContentText("Username or Password or Confirm Password fields cannot be leaved empty.");
            alert.showAndWait();
        } else if (username.getText().length() < 5) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("IMP");
            alert.setHeaderText("Username is too short!");
            alert.setContentText("Username should contain atleast 5 characters.");
            alert.showAndWait();
        } else if (!password.getText().equals(confirmPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("IMP");
            alert.setHeaderText("Confirm Password Mismatched!");
            alert.setContentText("Password and Confirm Password fields must be equal.");
            alert.showAndWait();
        } else {
            try {
                connectionManager.registerWithServer(username.getText(), password.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IMP");
                alert.setHeaderText("Congratulations, " + username.getText() + "!");
                alert.setContentText("You are registered upto the system. Please enter the "
                        + "username and password in login dialog to login to the system.");
                alert.showAndWait();
                Stage thisStage = (Stage) username.getScene().getWindow();
                thisStage.close();
            } catch (XMPPException e) {
                if (e.getMessage().contains("409")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("IMP");
                    alert.setHeaderText("Username is not available!");
                    alert.setContentText("Please try a different username.");
                    alert.showAndWait();
                } else if (e.getMessage().contains("400")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("IMP");
                    alert.setHeaderText("Invalid Username or Password!");
                    alert.setContentText("Please do not user @,/ or spaces in your username or password.");
                    alert.showAndWait();
                } else {
                    System.out.println(e);
                }

            }
        }
    }

    private void addMessageLimiter() {
        //this method limits the username length
        username.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (username.getText().length() > 20) {
                    String s = username.getText().substring(0, 20);
                    username.setText(s);
                    try {
                        final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
                        if (runnable != null) {
                            runnable.run();
                        }
                    } catch(Exception e) {
                        
                    }
                }
            }
        });

        //this method limits the username length
        password.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (password.getText().length() > 30) {
                    String s = password.getText().substring(0, 30);
                    password.setText(s);
                    try {
                        final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
                        if (runnable != null) {
                            runnable.run();
                        }
                    } catch(Exception e) {
                        
                    }
                }
            }
        });
    }
}
