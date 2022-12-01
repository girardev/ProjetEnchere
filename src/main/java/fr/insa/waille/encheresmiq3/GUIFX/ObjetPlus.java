/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import fr.insa.encheresmiq3.modele.Objet;
import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeEnchere;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getNomCategorie;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getUtilisateur;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author valen
 */
public class ObjetPlus extends GridPane{

    public ObjetPlus(Stage stage, Connection con, Objet obj) throws FileNotFoundException, SQLException{
        
    Label logo = recupererLogo();
        
    String titre = obj.getTitre();
    String description = obj.getDescription();
    String debut = obj.getDebut();
    String fin = obj.getFin();
    int prix_base = obj.getPrix_base();
    int categorie = obj.getCategorie();
    int propose_par = obj.getPropose_par();
    
    Label Ltitre = new Label("Titre : ");
    Ltitre.setStyle("-fx-max-width: 50");
    Ltitre.setStyle("-fx-font-weight: bold");
    Label Ldescription = new Label("Description : ");
    Ldescription.setStyle("-fx-max-width: 50");
    Ldescription.setStyle("-fx-font-weight: bold");
    Label Ldebut = new Label("Debut : ");
    Ldebut.setStyle("-fx-max-width: 50");
    Ldebut.setStyle("-fx-font-weight: bold");
    Label Lfin = new Label("Fin : ");
    Lfin.setStyle("-fx-max-width: 50");
    Lfin.setStyle("-fx-font-weight: bold");
    Label Lprix_base = new Label("Prix de base : ");
    Lprix_base.setStyle("-fx-max-width: 50");
    Lprix_base.setStyle("-fx-font-weight: bold");
    Label Lcategorie = new Label("Catégorie : ");
    Lcategorie.setStyle("-fx-max-width: 50");
    Lcategorie.setStyle("-fx-font-weight: bold");
    Label Lpropose_par = new Label("Proposé par : ");
    Lpropose_par.setStyle("-fx-max-width: 50");
    Lpropose_par.setStyle("-fx-font-weight: bold");
    Button Bprop = new Button("Proposer une enchère");
    Label panneau = new Label();
    TextField TnouvPrix = new TextField();
    Button Bencherir = new Button("Enchérir");
    
    Label ShowTitre = new Label(titre);
    Label ShowDescription = new Label(description);
    Label ShowDebut = new Label(debut);
    Label ShowFin = new Label(fin);
    String Sprix_base = Integer.toString(prix_base);
    Label ShowPrix_base = new Label(Sprix_base+" €");
    String Scategorie = getNomCategorie(con,categorie);
    Label ShowCategorie = new Label(Scategorie);
    String Spropose_par = getUtilisateur(con,propose_par);
    Label ShowPropose_par = new Label(Spropose_par);
    
    this.add(logo,0,0);
    this.add(Ltitre,0,1);
    this.add(ShowTitre,0,2);
    this.add(Ldescription,0,3);
    this.add(ShowDescription,0,4);
    this.add(Ldebut,0,5);
    this.add(ShowDebut,1,5);
    this.add(Lfin,0,6);
    this.add(ShowFin,1,6);
    this.add(Lprix_base,0,7);
    this.add(ShowPrix_base,1,7);
    this.add(Lcategorie,0,8);
    this.add(ShowCategorie,1,8);
    this.add(Lpropose_par,0,9);
    this.add(ShowPropose_par,1,9);
    this.add(Bprop,3,1);
    this.add(panneau,3,2);
    
    //action de l'appuie sur le bouton enchere
    Bprop.setOnAction((t) ->{

        panneau.setText("Proposer un nouveau prix d'enchère");
        this.add(TnouvPrix,3,3);
        this.add(Bencherir,3,4);

    });
    
    //action de l'appuie sur le bouton enchere
    Bencherir.setOnAction((t) ->{

        String quand = "aujourd'hui";
        int nouvprix = Integer.parseInt(TnouvPrix.getText());
        String email;
        int idUser=0;
        try {
            email = getEmailUtilisateurEnCours(con);
            idUser = getIdUtilisateur(con,email);
        } catch (SQLException ex) {
            Logger.getLogger(ObjetPlus.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int idObj = obj.getId();
        
        try {
            creeEnchere(con, quand, nouvprix, idUser, idObj);
        } catch (SQLException ex) {
            Logger.getLogger(ObjetPlus.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        panneau.setText("Proposition d'enchère effectuée");
        TnouvPrix.setText("");
        this.getChildren().remove(Bencherir);
        this.getChildren().remove(TnouvPrix);
    });
    
    
    
    }
}
