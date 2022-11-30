/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeCategorie;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author valen
 */
public class CreerCat extends GridPane{

    public CreerCat(Stage stage, Connection con) throws FileNotFoundException{
              
        //AFFICHAGE DU CONTENU DE LA FENETRE
        Label logo = recupererLogo();
        Label Lnouvcat = new Label("Entrez le nom d'une nouvelle catégorie");
        TextField Fnouvcat = new TextField();
        Label panneau = new Label();
        Button Bcreercat = new Button("Créer catégorie");
        Button Bretour = new Button("Retour à l'accueil");
        
        //AJOUT DES COMPOSANTS AU GRIDPANE
        this.add(logo, 0, 0);
        this.add(Lnouvcat,0,1);
        this.add(Fnouvcat,1,1);
        this.add(Bcreercat,2,1);
        this.add(panneau,0,2);
        this.add(Bretour,0,3);
        
        //action de l'appui sur le bouton inscription
        Bcreercat.setOnAction((t) ->{
            String nom = Fnouvcat.getText();
            
            
            //utilisation de la fonction creeUtilisateur
            try {
                creeCategorie(con,nom);
            } catch (SQLException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Fnouvcat.setText("");
            
            panneau.setText("Création de la nouvelle catégorie effectuée");
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
            }
           
            
        });
        
    }
 
}
