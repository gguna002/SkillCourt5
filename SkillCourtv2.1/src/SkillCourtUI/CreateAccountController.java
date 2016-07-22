/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SkillCourtUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import skillcourt5.User;

/**
 * FXML Controller class
 *
 * @author Sean
 */
public class CreateAccountController implements Initializable
{
    ObservableList<String> positionBoxList = FXCollections.observableArrayList("ST", "CF", "LW", "RW", "LW", "CAM", "LM", "RM", "CM", "CDM", "LWB", "RWB", "LB", "RB", "CB", "GK");
    ObservableList<String> footBoxList = FXCollections.observableArrayList("Right", "Left");
    
    @FXML
    private Label titleLabel;
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private TextField firstNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private TextField emailText;
    @FXML
    private ChoiceBox position;
    @FXML
    private TextField DoBText;
    @FXML
    private ChoiceBox preferredFootText;
    @FXML
    private TextField teamText;
    
    @FXML
    private void submitBtnAction(ActionEvent event) throws Exception{
        User.createAccount(usernameText.getText(), passwordText.getText(), firstNameText.getText(), 
                lastNameText.getText(), emailText.getText(), (String)position.getValue(), DoBText.getText(), 
                (String)preferredFootText.getValue(), teamText.getText());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        position.setValue("ST");
        position.setItems(positionBoxList);
        preferredFootText.setValue("Right");
        preferredFootText.setItems(footBoxList);
    }    
    
}