/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import fr.insa.encheresmiq3.modele.Objet;
import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getCodePostalUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getNomCategorie;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getPassUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getRoleUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getUtilisateur;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author valen
 */
public class Profil extends GridPane{
      
    public Profil(Stage stage, Connection con) throws FileNotFoundException, SQLException, IOException{
        
    Label logo = recupererLogo();
    
    Button Bretour = new Button("Retour à l'accueil");
    
    String email = getEmailUtilisateurEnCours(con);
    int id = getIdUtilisateur(con,email);
    String nomprenom = getUtilisateur(con,id);   
    String motdepasse = getPassUtilisateur(con,email);
    String code_postal = getCodePostalUtilisateur(con,email);
    String role = getRoleUtilisateurEnCours(con);
    
    Label Lnom = new Label("Nom/Prénom: ");
    Lnom.setStyle("-fx-max-width: 50");
    Lnom.setStyle("-fx-font-weight: bold");
    Label Lemail = new Label("Email : ");
    Lemail.setStyle("-fx-max-width: 50");
    Lemail.setStyle("-fx-font-weight: bold");
    Label Lpass = new Label("Mot de passe : ");
    Lpass.setStyle("-fx-max-width: 50");
    Lpass.setStyle("-fx-font-weight: bold");
    Label LcodeP = new Label("Code postal : ");
    LcodeP.setStyle("-fx-max-width: 50");
    LcodeP.setStyle("-fx-font-weight: bold");
    Label Lrole = new Label("Role : ");
    Lrole.setStyle("-fx-max-width: 50");
    Lrole.setStyle("-fx-font-weight: bold");
    
    Label ShowNom = new Label(nomprenom);
    Label ShowEmail = new Label(email);
    
    
    
    Label ShowCodePostal = new Label(code_postal);
    Label ShowRole = new Label(role);
    
    this.add(logo,1,0);
    this.add(Lnom,0,1);
    this.add(ShowNom,1,1);
    this.add(Lemail,0,2);
    this.add(ShowEmail,1,2);
    this.add(Lpass,0,3);
    
    PasswordField pass = new PasswordField();
    pass.setText(motdepasse);
    TextField Tpass = new TextField();
    Tpass.setText(motdepasse);
    this.add(pass,1,3);
    CheckBox checkBox1 = new CheckBox("Montrer");
    this.add(checkBox1,2,3);    
    
    this.add(LcodeP, 0, 5);
    this.add(ShowCodePostal,1,5);
    this.add(Lrole,0,6);
    this.add(ShowRole,1,6);
    this.add(Bretour,0,7);
    
    
    
    
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
        
        checkBox1.setOnAction((var t) ->{
            if(checkBox1.isSelected()){
                this.getChildren().remove(pass);
                this.add(Tpass, 1, 3);
            }
            else{
                this.getChildren().remove(Tpass);
                this.add(pass,1,3);
            }
            
        });
    }
    
}
