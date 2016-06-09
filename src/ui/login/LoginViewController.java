/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.login;

import exception.InvalidCredentialsException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import models.connectionManagement.ConnectionManager;

/**
 *
 * @author Dulanjaya
 */
public class LoginViewController implements Initializable {

    //Models
    ConnectionManager connectionManager;

    //Preferences for User Passwords
    private static Preferences userCredentials = Preferences.userRoot();
    private static final String USER_KEY = "S315df6s!@#";
    private static final String PASS_KEY = "w#ny!m8654@";
    private static final String KEYEN = "DUL34654lud54360";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connectionManager = ConnectionManager.getConnectionManager();
        setCredentials();
    }

    //View Widgets
    @FXML
    public void handleRegisterAction(ActionEvent event) {
        try {
            //default configuration and initialization of the form
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/register/RegisterView.fxml"));

            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.getIcons().add(new Image("/resources/LogoTransparent.png"));

            stage.setScene(scene);
            stage.show();

            stage.setResizable(false);
            stage.setTitle("IMP");
        } catch (IOException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private CheckBox rememberMe;

    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        //action of the login button
        if (userName.getText().isEmpty() || password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("IMP");
            alert.setHeaderText("Username or Password fields are empty!");
            alert.setContentText("Username or Password fields cannot be leaved empty.");
            alert.showAndWait();
        } else {
            try {
                connectionManager.loginToServer(userName.getText(), password.getText());
                storeCredentials(userName.getText(), password.getText());
                showMainView();

            } catch (InvalidCredentialsException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("IMP");
                alert.setHeaderText("Username or Password Error!");
                alert.setContentText("Username or Password is incorrect. Please try again.");
                alert.showAndWait();
            }
        }
    }

    private void showMainView() {
        try {
            //default configuration and initialization of the form
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/main/MainView.fxml"));

            Scene scene = new Scene(root);

            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

            stage.setResizable(false);
            stage.setTitle("IMP");
            stage.getIcons().add(new Image("/resources/LogoTransparent.png"));

            final Stage loginStage = (Stage) anchorPane.getScene().getWindow();
            loginStage.hide();
            //anchorPane.getScene().getWindow().hide();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });
            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    loginStage.show();
                    stage.close();
                    
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setCredentials() {
        //this method get the user credentials from the registry file if such file exist
        if(!userCredentials.getBoolean("rem", false) ) {
            return;
        }
        try {
            Key aesKey = new SecretKeySpec(KEYEN.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String pass = new String(cipher.doFinal(userCredentials.getByteArray(PASS_KEY, null)));
            userName.setText(userCredentials.get(USER_KEY, ""));
            password.setText(pass);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void storeCredentials(String user, String password) {
        //this method store the user credentials in a registry file by hashing the password
        if(!rememberMe.isSelected()) {
            userCredentials.putBoolean("rem", false);
            return;
        } else {
            userCredentials.putBoolean("rem", true);
        }
        Key aesKey = new SecretKeySpec(KEYEN.getBytes(), "AES");
        try {
            userCredentials.put(USER_KEY, user);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] pass = cipher.doFinal(password.getBytes());
            userCredentials.putByteArray(PASS_KEY, pass);
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
