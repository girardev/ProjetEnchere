/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.ModifierPassUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getCodePostalUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getPassUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getRoleUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getUtilisateur;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author valen
 */
public class ChangerMDP extends GridPane{
      
    public ChangerMDP(Stage stage, Connection con) throws FileNotFoundException, SQLException, IOException{
        
    Label logo = recupererLogo();
    
    Button Bretour = new Button("Retour à l'accueil");
    Button Bmodifier = new Button("Modifier votre mot de passe");
    Label panneau = new Label("");
    
    String email = getEmailUtilisateurEnCours(con);  
    String motdepasse = getPassUtilisateur(con,email);
    
    Label Lpass = new Label("Mot de passe actuel : ");
    Lpass.setStyle("-fx-max-width: 50");
    Lpass.setStyle("-fx-font-weight: bold");
    Label Lnouvpass = new Label("Entrez un nouveau mot de passe : ");
    Lnouvpass.setStyle("-fx-max-width: 50");
    Lnouvpass.setStyle("-fx-font-weight: bold");
    Label Lnouvpass2 = new Label("Confirmez votre nouveau mot de passe: ");
    Lnouvpass2.setStyle("-fx-max-width: 50");
    Lnouvpass2.setStyle("-fx-font-weight: bold");
    
    this.add(logo,1,0);
    this.add(Lpass,0,1);
    this.add(Lnouvpass,0,2);
    this.add(Lnouvpass2,0,3);
    
    PasswordField pass = new PasswordField();
    pass.setText(motdepasse);
    TextField Tpass = new TextField();
    Tpass.setText(motdepasse);
    this.add(pass,1,1);
    CheckBox checkBox1 = new CheckBox("Montrer");
    this.add(checkBox1,2,1); 
    
    this.add(panneau,1,4);
    this.add(Bretour,0,5);
    this.add(Bmodifier,1,5);
    
    PasswordField Ppass = new PasswordField();
    PasswordField Ppass2 = new PasswordField();
    this.add(Ppass,1,2);
    this.add(Ppass2,1,3);
    
    
    
    //action de l'appui sur le bouton retour
        Bretour.setOnAction((var t) ->{
            
            try {
                Scene sc2 = new Scene(new Accueil(stage, con));
                stage.setScene(sc2);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CreerCat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CreerObjet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CreerObjet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CreerObjet.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
        });
        
        Bmodifier.setOnAction((var t) ->{
           
            if(Ppass.getText().equals(Ppass2.getText())){
                String nouvpass = Ppass.getText();
                try {
                    ModifierPassUtilisateur(con,email,nouvpass);
                } catch (SQLException ex) {
                    Logger.getLogger(ChangerMDP.class.getName()).log(Level.SEVERE, null, ex);
                }
                panneau.setText("Mot de passe modifié");
                Ppass.setText("");
                Ppass2.setText("");
                
            }
            else{
                panneau.setText("Veuillez entrer 2 fois le même mot de passe");
                Ppass.setText("");
                Ppass2.setText("");
            }
           
            
        });
        
        checkBox1.setOnAction((var t) ->{
            if(checkBox1.isSelected()){
                this.getChildren().remove(pass);
                this.add(Tpass, 1, 1);
            }
            else{
                this.getChildren().remove(Tpass);
                this.add(pass,1,1);
            }
            
        });
    }
    
}
