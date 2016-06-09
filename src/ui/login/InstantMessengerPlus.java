/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Dulanjaya
 */
public class InstantMessengerPlus extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //default configuration and initialization of the form
        Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image("/resources/LogoTransparent.png"));
        
        //calling methods for the initialization
        initComponents(scene, stage);
    }
    
    private void initComponents(Scene scene, Stage stage) {
        //sets up component features
        stage.setResizable(false);
        stage.setTitle("IMP");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
