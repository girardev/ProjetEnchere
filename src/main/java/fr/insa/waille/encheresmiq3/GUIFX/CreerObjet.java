/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeObjet;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.creeObjetImage;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getCategories;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdCategorie;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdUtilisateur;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Timestamp;
import java.time.LocalTime;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author valen
 */
public class CreerObjet extends GridPane{
    
    File img;  //fichier correspondant à l'image sélectionnée pour l'objet
    
    public CreerObjet(Stage stage, Connection con) throws FileNotFoundException{
              
        //AFFICHAGE DU CONTENU DE LA FENETRE
        Label logo = recupererLogo();
        logo.setStyle(" -fx-border-width:5px ;-fx-border-style: solid; -fx-border-color: orange; ");
        Label Lajout = new Label("Ajouter un nouvel objet");
        Lajout.setStyle("-fx-max-width: 50");
        Lajout.setStyle("-fx-font-weight: bold");
        Label Ltitre = new Label("Titre");
        TextField Ftitre = new TextField();
        Label Ldesc = new Label("Description");
        TextField Fdesc = new TextField();
        Label Ldebut = new Label("Début");
        DatePicker Ddebut = new DatePicker();
        Label Lfin = new Label("Fin");
        DatePicker Dfin = new DatePicker();
        Label Lprix = new Label("Prix");
        TextField Fprix = new TextField();
        Label Lcategorie = new Label("Catégorie");
        Label Limage = new Label("Image");
        Button Bimage = new Button("Sélectionner une image");
        
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
        
        //SELECTEUR HEURE DEBUT
        ComboBox listeHeureD = new ComboBox();
        listeHeureD.getItems().addAll("00:00:00","01:00:00","02:00:00","03:00:00","04:00:00","05:00:00","06:00:00","07:00:00","08:00:00","09:00:00","10:00:00","11:00:00","12:00:00","14:00:00","15:00:00","16:00:00","17:00:00","18:00:00","19:00:00","20:00:00","21:00:00","22:00:00","23:00:00","24:00:00");
        //SELECTEUR HEURE FIN
        ComboBox listeHeureF = new ComboBox();
        listeHeureF.getItems().addAll("00:00:00","01:00:00","02:00:00","03:00:00","04:00:00","05:00:00","06:00:00","07:00:00","08:00:00","09:00:00","10:00:00","11:00:00","12:00:00","14:00:00","15:00:00","16:00:00","17:00:00","18:00:00","19:00:00","20:00:00","21:00:00","22:00:00","23:00:00","24:00:00");
        
        Label panneau = new Label();
        Button Bcreerobj = new Button("Créer la vente d'objet");
        Button Bretour = new Button("Retour à l'accueil");
        
        //AJOUT DES COMPOSANTS AU GRIDPANE
        this.add(logo, 1, 0);
        this.add(Lajout,0,1);
        this.add(Ltitre,0,2);
        this.add(Ftitre,1,2);
        this.add(Ldesc,0,3);
        this.add(Fdesc,1,3);
        this.add(Ldebut,0,4);
        this.add(Ddebut,1,4);
        this.add(listeHeureD, 2, 4);
        this.add(Lfin,0,5);
        this.add(Dfin,1,5);
        this.add(listeHeureF, 2, 5);
        this.add(Lprix,0,6);
        this.add(Fprix,1,6);
        this.add(Lcategorie,0,7);
        this.add(listeCategorie,1,7);
        this.add(Limage,0,8);
        this.add(Bimage,1,8);
        this.add(panneau,1,9);
        this.add(Bcreerobj,1,10);
        this.add(Bretour,0,10);
        
        
        //action de l'appui sur le bouton ajout image
        Bimage.setOnAction((t) ->{
            //SELECTEUR DE FICHIERS (IMAGE)
            FileChooser imgChooser = new FileChooser();
//            ExtensionFilter ef = new ExtensionFilter("Image Files","*.png"); //ne fonctionne pas
            imgChooser.setTitle("Sélectionner une image");
            img = imgChooser.showOpenDialog(stage);
            String path = img.getAbsolutePath();
            if(img==null){
                panneau.setText("aucune image sélectionnée");
            }            
            else{
                panneau.setText("image ajoutée");
            }
            
        });
        
        //action de l'appuie sur le bouton creer objet
        Bcreerobj.setOnAction((t) ->{
            String titre = Ftitre.getText();
            String description = Fdesc.getText();
            
            //RECUPERATION DES DATES/HEURES D'ENCHERE
            LocalDate debut = Ddebut.getValue(); //récupère date picker
            String datedebut = debut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String heuredebut = (String) listeHeureD.getSelectionModel().getSelectedItem();
            LocalDate fin = Dfin.getValue();
            String datefin = fin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String heurefin = (String) listeHeureF.getSelectionModel().getSelectedItem();
            //crée les Timestamp correspondant au choix de l'utilisateur
            Timestamp timestampDebut = Timestamp.valueOf(datedebut + " " + heuredebut);
            Timestamp timestampFin = Timestamp.valueOf(datefin + " " + heurefin);
            
            System.out.println("debut Enchere : " + timestampDebut.toString());
            System.out.println("heure actuelle : " + LocalDate.now().atTime(LocalTime.now()).toString().replace("T", " "));
            
            int prix = Integer.parseInt(Fprix.getText());
            String categorie = (String) listeCategorie.getSelectionModel().getSelectedItem();
            
            String err; //message d'erreur à afficher
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
                err = "utilisateur non reconnu";
            }
            
            //vérifie que les dates entrées sont valides
            err = controleSaisieDates(timestampDebut, timestampFin);
            panneau.setText(err);
            
            if(categorieInt!=-1&&propose_par!=-1&&err==""){
            
                //utilisation de la fonction creeObjet
                try {
                    try {
                        creeObjetImage(con,titre,description,timestampDebut,timestampFin,prix,categorieInt, propose_par, img);
                    } catch (IOException ex) {
                        Logger.getLogger(CreerObjet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
                }

                Ftitre.setText("");
                Fdesc.setText("");
                Ddebut.setValue(null);
                Dfin.setValue(null);
                Fprix.setText("");

                panneau.setText("Ajout de l'objet effectué");
            }
            else{
                Ddebut.setValue(null);
                Dfin.setValue(null);

                panneau.setText(err); 
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
            } catch (IOException ex) {
                Logger.getLogger(CreerObjet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CreerObjet.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
        });
       
    }
        //Renvoie un message d'erreur si date début > date fin ou date début < aujourd'hui
        public static String controleSaisieDates(Timestamp deb, Timestamp fin){
            String err; // message de retour à afficher
            //récupère l'heure/date actuelle sous forme de timestamp :
            Timestamp now = Timestamp.valueOf(LocalDate.now().atTime(LocalTime.now()).toString().replace("T", " "));
            if(deb.after(fin)){
                err = "Choisissez date début inférieure à date fin";
            }
            else if (deb.before(now)){
               err = "Choisissez date début ultérieure à maintenant";        
            }
            else{
                err = "";
            }
            return err;
        }
}
