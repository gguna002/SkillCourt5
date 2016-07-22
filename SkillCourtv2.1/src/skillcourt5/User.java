/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skillcourt5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Gajen
 */
public class User {
    
    private String username;
    private String password;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getName() {
        return username;
    }
    
    public String getPass() {
        return password;
    }
    
    public static Boolean login (String loginName, String loginPass) throws Exception {    
        Boolean loginSuccess = false;
        try {         
            String host = "jdbc:derby://localhost:1527/SkillCourtUser";
            String userName = "Username";
            String password = "password";
            Connection con = DriverManager.getConnection(host, userName, password);
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM USERNAME.USERS";
            ResultSet rs = stmt.executeQuery(SQL);
            
            while(rs.next())
            {
                if(rs.getString("USERNAME").equals(loginName) && rs.getString("PASSWORD").equals(loginPass))
                {
                    JOptionPane.showMessageDialog(null, "Succefully Logged in!");
                    loginSuccess = true;
                    break;                  
                }            
            }          
        } 
        catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loginSuccess;
    }
    
    public static void createAccount(String username, String password, String fName, String lName, String eMail, 
                                                    String position, String DoB, String pFoot, String team) throws Exception {
       try {         
            String host = "jdbc:derby://localhost:1527/SkillCourtUser";
            String userName = "Username";
            String passWord = "password";
            Connection con = DriverManager.getConnection(host, userName, passWord);
            Statement stmt = con.createStatement();
            String SQL = "INSERT INTO USERNAME.USERS (USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, POSITION, DOB, PREFERREDFOOT, TEAM)\nVALUES (" 
                   + "'" + username + "'" + "," + "'" + password + "'" + "," +
                   "'" + fName + "'" + "," + "'" + lName + "'" + "," + "'" + eMail + "'" + "," + "'" + position + "'" + "," +
                   "'" + DoB + "'" + "," + "'" + pFoot + "'" + "," + "'" + team + "'" + ")";
            stmt.executeUpdate(SQL);
            JOptionPane.showMessageDialog(null, "Account succefully added using query:\n" + SQL);
       } 
       catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } 
              
    }
    
    public static void recoverPassword() throws Exception{
         try {         
            String host = "jdbc:derby://localhost:1527/SkillCourtUser";
            String userName = "Username";
            String password = "password";
            String recoveryAccount = JOptionPane.showInputDialog("Enter username of account for recovery: ");
            Connection con = DriverManager.getConnection(host, userName, password);
            Statement stmt = con.createStatement();
            String SQL = "SELECT USERS.PASSWORD FROM USERNAME.USERS WHERE USERS.USERNAME = " + "'" + recoveryAccount + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            JOptionPane.showMessageDialog(null, "Your password is: " + rs.getString("PASSWORD"));
       } 
       catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void uploadScore(int score, int accuracy) throws Exception {
        try {         
            String host = "jdbc:derby://localhost:1527/SkillCourtUser";
            String userName = "Username";
            String passWord = "password";
            Connection con = DriverManager.getConnection(host, userName, passWord);
            Statement stmt = con.createStatement();
            String SQL = "UPDATE USERNAME.USERS\nSET USERS.SCORE=" +score + ", " + "USERS.ACCURACY=" + accuracy+"\n" + "WHERE USERS.USERNAME=" + "'"+username+"'";
            stmt.executeUpdate(SQL);
            JOptionPane.showMessageDialog(null, "Succesfully uploaded score for username: " + username);
       } 
       catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void viewScore() {
        try {         
            String host = "jdbc:derby://localhost:1527/SkillCourtUser";
            String userName = "Username";
            String password = "password";
            Connection con = DriverManager.getConnection(host, userName, password);
            Statement stmt = con.createStatement();
            String SQL = "SELECT USERS.SCORE, USERS.ACCURACY FROM USERNAME.USERS WHERE USERS.USERNAME = " + "'" + username + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            JOptionPane.showMessageDialog(null, "Previous results for username: " + username + "\nScore: " + rs.getString("SCORE") + "\n" + "Accuracy: " + rs.getString("ACCURACY"));
       } 
       catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
