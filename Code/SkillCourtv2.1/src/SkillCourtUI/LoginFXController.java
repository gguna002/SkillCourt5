/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SkillCourtUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import skillcourt.SkillCourt;

/**
 *
 * @author Sean
 */
public class LoginFXController implements Initializable
{
    SkillCourt sc = new SkillCourt();
    
    @FXML
    private Label titleLabel;    
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    @FXML 
    private Button loginBtn;
    @FXML
    private Button createAccountBtn;
    @FXML
    private Hyperlink forgotPassword;
    
    @FXML
    private void loginBtnAction(ActionEvent event) throws Exception {
        if(sc.login(usernameText.getText(), passwordText.getText()) == true) {
            System.out.println("Successfully logged in.");
            sc.mainMenu();
        }          
        else
            System.out.println("Login failed.");            
            
    }
    
    @FXML
    private void createBtnAction(ActionEvent event) {
        
    }
    
    @FXML
    private void forgotPasswordAction(ActionEvent event) throws Exception {
        sc.recoverPassword();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    
}
