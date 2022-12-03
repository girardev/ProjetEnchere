/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.defautConnect;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getRoleUtilisateur;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/**
 * 
 * @author arvar
 */    
public class GridPaneAuthentification extends GridPane {

    public GridPaneAuthentification(Stage stage,Connection con){
        //PARTIE INSCRIPTION
        //création des labels et champs de saisie
        Label titre = new Label("Inscription");
        titre.setStyle("-fx-max-width: 50");
        titre.setStyle("-fx-font-weight: bold");
        Label labelNom = new Label("Nom");
        TextField Fnom = new TextField();
        Label labelPrenom = new Label("Prénom");
        TextField Fprenom = new TextField();
        Label labelPass = new Label("Mot de passe");
        PasswordField Fpass = new PasswordField();
        Label labelEmail = new Label("Email");
        TextField Femail = new TextField();
        Label labelCodePostal = new Label("Code postal");
        TextField Fcodepostal = new TextField();
        Button B_Inscription = new Button("Inscription");
        Label panneau = new Label();
        
        
        //ajout des composants au GridPane (colonne,ligne)
        this.add(titre,0,0);
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
        titre2.setStyle("-fx-font-weight: bold");
        Label labelPass2 = new Label("password");
        PasswordField Fpass2 = new PasswordField();
        Label labelEmail2 = new Label("email");
        TextField Femail2 = new TextField();
        Button B_Connexion = new Button("Connexion");
        Label panneau2 = new Label();
        
        //ajout des composants au GridPane (colonne,ligne)
        this.add(titre2,2,0);
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
            try(Statement st1 = con.createStatement()){
                String query1 = "select pass from utilisateur where email like '"+email2+"' ";
                ResultSet resultat = st1.executeQuery(query1);
                String motdepasse = "";
                while(resultat.next()){
                motdepasse = resultat.getString("pass");
                }
                
                if(motdepasse.equals(pass2)){          
                    Femail2.setText("");
                    Fpass2.setText("");
                    
                    String role= getRoleUtilisateur(con,email2);
                    
                    try {
                        creeUtilisateurEnCours(con,email2,pass2,role);
                    } catch (SQLException ex) {
                        Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    Scene sc2 = new Scene(new Accueil(stage, con));
                    stage.setScene(sc2);
                }
                else{
                    panneau2.setText("Connexion impossible");
                    Femail2.setText("");
                    Fpass2.setText("");                    
                }
            
            } catch (SQLException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }
}

