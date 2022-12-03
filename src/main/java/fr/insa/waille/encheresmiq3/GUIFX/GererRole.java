/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.ModifierRoleUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.ModifierRoleUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getAllUsers;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getCategories;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getRoleUtilisateur;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author valen
 */
public class GererRole extends GridPane{

    public GererRole(Stage stage, Connection con) throws FileNotFoundException{
              
        //AFFICHAGE DU CONTENU DE LA FENETRE
        Label logo = recupererLogo();
        Label Lutilisateurs = new Label("Liste des utilisateurs: ");
        Label Lrole = new Label("Liste des rôles");
        //AFFICHAGE DE LA LISTE DES CATEGORIES
        ComboBox listeUsers = new ComboBox();
        ArrayList<String> Utilisateurs = new ArrayList<String>();
        try {
            Utilisateurs = getAllUsers(con);
        }
        catch (SQLException ex) {
            Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            }       
        listeUsers.getItems().setAll(Utilisateurs);
        
        ComboBox listeRoles = new ComboBox();
        ArrayList<String> Roles = new ArrayList<String>();
        Roles.add("Lambda");
        Roles.add("Categorie");
        Roles.add("Admin");
        listeRoles.getItems().setAll(Roles);
        
        Label panneau = new Label();
        Button Brole = new Button("Attribuer rôle");
        Button Bretour = new Button("Retour à l'accueil");
        
        //AJOUT DES COMPOSANTS AU GRIDPANE
        this.add(logo, 1, 0);
        this.add(Lutilisateurs,0,1);
        this.add(listeUsers,0,2);
        this.add(Lrole,1,1);
        this.add(listeRoles,1,2);
        this.add(panneau,0,3);
        this.add(Brole,1,4);
        this.add(Bretour,0,4);
        
        
        //action de l'appui sur le bouton retour
        Brole.setOnAction((var t) ->{
            
            String email = (String) listeUsers.getSelectionModel().getSelectedItem();
            String role = (String) listeRoles.getSelectionModel().getSelectedItem();
            try {
                ModifierRoleUtilisateur(con,email,role);
            } catch (SQLException ex) {
                Logger.getLogger(GererRole.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (email.equals(getEmailUtilisateurEnCours(con))){
                    ModifierRoleUtilisateurEnCours(con,email,role); 
                }
                    } catch (SQLException ex) {
                Logger.getLogger(GererRole.class.getName()).log(Level.SEVERE, null, ex);
            }
            panneau.setText("Modification effectuée");
            
        });
        
        //action de l'appui sur le bouton retour
        Bretour.setOnAction((var t) ->{
            
            try {
                Scene sc2 = new Scene(new Accueil(stage, con));
                stage.setScene(sc2);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CreerCat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CreerCat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GererRole.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GererRole.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
        });
        
    }
 
}
