/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.defautConnect;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getPassUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getRoleUtilisateur;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * 
 * @author arvar
 */    
public class GridPaneAuthentification extends GridPane {

    public GridPaneAuthentification(Stage stage,Connection con){
        this.setStyle("-fx-background-color : #FEC98F");
        
        //PARTIE INSCRIPTION
        //création des labels et champs de saisie
        Label titre = new Label("Inscription");
        titre.setFont(new Font(20));
        titre.setStyle("-fx-max-width: 50");
        titre.setStyle("-fx-font-weight: bold");
        Label labelNom = new Label("Nom");
        TextField Fnom = new TextField();
        Label labelPrenom = new Label("Prénom");
        TextField Fprenom = new TextField();
        Label labelPass = new Label("Password");
        PasswordField Fpass = new PasswordField();
        Label labelEmail = new Label("Email");
        TextField Femail = new TextField();
        Label labelCodePostal = new Label("Code postal ");
        TextField Fcodepostal = new TextField();
        Button B_Inscription = new Button("Inscription");
        Label panneau = new Label();
        
        //ajout des composants au GridPane (colonne,ligne)
        this.add(titre,1,0);
        this.add(labelNom, 0, 1);
        this.add(Fnom, 1, 1);
        this.add(labelPrenom, 0, 2);
        this.add(Fprenom, 1, 2);
        this.add(labelEmail, 0, 3);
        this.add(Femail, 1, 3);
        this.add(labelPass, 0, 4);
        this.add(Fpass, 1, 4);
        this.add(labelCodePostal, 0, 5);
        this.add(Fcodepostal, 1, 5);
        this.add(B_Inscription, 1, 6);  
        this.add(panneau,1,7);
        
        //ajout de marges autour pour centrer
        this.setPadding(new Insets(150-this.getHeight(), 100+this.getWidth(), 150+this.getHeight(), 100-this.getWidth()));
        
//action de l'appuie sur le bouton inscription
        B_Inscription.setOnAction((t) ->{
            String nom = Fnom.getText();
            String prenom = Fprenom.getText();
            String pass = Fpass.getText();
            String email = Femail.getText();
            String codepostal = Fcodepostal.getText();
            
            
            //utilisation de la fonction creeUtilisateur
            try {
                creeUtilisateur(con,nom,prenom,pass,email,codepostal,"Lambda");
            } catch (SQLException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Fnom.setText("");
            Fprenom.setText("");
            Fpass.setText("");
            Femail.setText("");
            Fcodepostal.setText("");
            
            panneau.setText("Inscription effectuée");
        });
        
        
        
        
        
        //PARTIE CONNEXION
        //création des labels et champs de saisie
        Label titre2 = new Label("Connexion");
        titre2.setStyle("-fx-max-width: 50");
        titre2.setFont(new Font(20));
        titre2.setStyle("-fx-font-weight: bold");
        Label labelPass2 = new Label(" Password ");
        PasswordField Fpass2 = new PasswordField();
        Label labelEmail2 = new Label(" Email");
        TextField Femail2 = new TextField();
        Button B_Connexion = new Button("Connexion");
        Label panneau2 = new Label();
        
        //ajout des composants au GridPane (colonne,ligne)
        this.add(titre2,3,0);
        this.add(labelEmail2, 2, 1);
        this.add(Femail2, 3, 1);
        this.add(labelPass2, 2, 2);
        this.add(Fpass2, 3, 2);
        this.add(B_Connexion, 3, 6);
        this.add(panneau2,3,7);
        
        
        //action de l'appuie sur le bouton connexion
        B_Connexion.setOnAction((var t) ->{
            String pass2 = Fpass2.getText();
            String email2 = Femail2.getText();
            
            
            //utilisation recherche du mot de passe d'un email
            
                String motdepasse = null;
            try {
                motdepasse = getPassUtilisateur(con,email2);
            } catch (SQLException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                if(motdepasse.equals(pass2)){          
                    Femail2.setText("");
                    Fpass2.setText("");
                    
                    String role = null;
                try {
                    role = getRoleUtilisateur(con,email2);
                } catch (SQLException ex) {
                    Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
                    try {
                        creeUtilisateurEnCours(con,email2,pass2,role);
                    } catch (SQLException ex) {
                        Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    Scene sc2 = null;
                try {
                    sc2 = new Scene(new Accueil(stage, con));
                } catch (SQLException ex) {
                    Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
                }
                    stage.setScene(sc2);
                }
                else{
                    panneau2.setText("Connexion impossible");
                    Femail2.setText("");
                    Fpass2.setText("");                    
                }
            
        });

    }
}

