/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeObjet;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getCategories;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdCategorie;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdUtilisateur;
import java.io.FileNotFoundException;
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
public class CreerObjet extends GridPane{

    public CreerObjet(Stage stage, Connection con) throws FileNotFoundException{
              
        //AFFICHAGE DU CONTENU DE LA FENETRE
        Label logo = recupererLogo();
        Label Lajout = new Label("Ajouter un nouvel objet");
        Lajout.setStyle("-fx-max-width: 50");
        Lajout.setStyle("-fx-font-weight: bold");
        Label Ltitre = new Label("Titre");
        TextField Ftitre = new TextField();
        Label Ldesc = new Label("Description");
        TextField Fdesc = new TextField();
        Label Ldebut = new Label("Début");
        TextField Fdebut = new TextField();
        Label Lfin = new Label("Fin");
        TextField Ffin = new TextField();
        Label Lprix = new Label("Prix");
        TextField Fprix = new TextField();
        
        //AFFICHAGE DE LA LISTE DES CATEGORIES
        ComboBox listeCategorie = new ComboBox();
        ArrayList<String> categories = new ArrayList<String>();
        try {
            categories = getCategories(con);
        }
        catch (SQLException ex) {
            Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            }       
        listeCategorie.getItems().setAll(categories);
        
        Label panneau = new Label();
        Button Bcreerobj = new Button("Créer l'objet");
        Button Bretour = new Button("Retour à l'accueil");
        
        //AJOUT DES COMPOSANTS AU GRIDPANE
        this.add(logo, 0, 0);
        this.add(Lajout,0,1);
        this.add(Ltitre,0,2);
        this.add(Ftitre,1,2);
        this.add(Ldesc,0,3);
        this.add(Fdesc,1,3);
        this.add(Ldebut,0,4);
        this.add(Fdebut,1,4);
        this.add(Lfin,0,5);
        this.add(Ffin,1,5);
        this.add(Lprix,0,6);
        this.add(Fprix,1,6);
        this.add(listeCategorie,1,7);
        this.add(panneau,1,8);
        this.add(Bcreerobj,2,1);
        this.add(Bretour,2,2);
        
        //action de l'appuie sur le bouton inscription
        Bcreerobj.setOnAction((t) ->{
            String titre = Ftitre.getText();
            String description = Fdesc.getText();
            String debut = Fdebut.getText();
            String fin = Ffin.getText();
            int prix = Integer.parseInt(Fprix.getText());
            String categorie = (String) listeCategorie.getSelectionModel().getSelectedItem();
            
            int categorieInt = -1;
            try {
                categorieInt = getIdCategorie(con,categorie);
            } catch (SQLException ex) {
                Logger.getLogger(CreerObjet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            int propose_par = -1;
            String email = null;
            
            try {
                email = getEmailUtilisateurEnCours(con);
                propose_par = getIdUtilisateur(con,email);
            } catch (SQLException ex) {
                Logger.getLogger(CreerObjet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(!(categorieInt==-1||propose_par==-1)){
            
                //utilisation de la fonction creeUtilisateur
                try {
                    creeObjet(con,titre,description,debut,fin,prix,categorieInt,propose_par);
                } catch (SQLException ex) {
                    Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
                }

                Ftitre.setText("");
                Fdesc.setText("");
                Fdebut.setText("");
                Ffin.setText("");
                Fprix.setText("");

                panneau.setText("Ajout de l'objet effectué");
            }
            else{
                Ftitre.setText("");
                Fdesc.setText("");
                Fdebut.setText("");
                Ffin.setText("");
                Fprix.setText("");

                panneau.setText("Ajout de l'objet impossible à effectuer"); 
            }
            
            
            
        });
        
        
        //action de l'appui sur le bouton retour
        Bretour.setOnAction((var t) ->{
            
            try {
                Scene sc2 = new Scene(new Accueil(stage, con));
                stage.setScene(sc2);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CreerCat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(CreerObjet.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
        });
        
        
        
    }
}
