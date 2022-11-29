

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import fr.insa.encheresmiq3.modele.Objet;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.defautConnect;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getCategories;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getRole;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheObjetParCategorie;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheObjetParMotCle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 *
 * @author valen
 */
public class Accueil extends GridPane {
    public Accueil(Connection con) throws FileNotFoundException{
              
        //AFFICHAGE DU CONTENU DE LA FENETRE
        Label logo = recupererLogo();
        Label titre = new Label("LeMauvaisCoin");
        titre.setStyle("-fx-max-width: 100");
        titre.setStyle("-fx-font-weight: bold");
        Label Lrecherche = new Label("Entrez un mot clé");
        TextField Frecherche = new TextField();
        Button Brecherche = new Button("Rechercher");
        Label Lcategorie = new Label("Catégories");
        Button Bcategorie = new Button("Par catégorie");
        Label panneau = new Label();
        Label essai = new Label("Essai");
        
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

        
        //AJOUT DES COMPOSANTS AU GRIDPANE
        this.add(logo, 0, 0);
        this.add(Lrecherche,0,1);
        this.add(Frecherche,1,1);
        this.add(Brecherche,2,1);
        this.add(Lcategorie,0,2);
        this.add(listeCategorie,1,2);
        this.add(Bcategorie,2,2);
        this.add(panneau,0,4);
        
        int role = 0;
        try {
            role = getRole(con);
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(role==1){
            this.add(essai,3,1);
        }
        
        //action de l'appui sur le bouton recherche par catégorie
        Bcategorie.setOnAction((t) ->{
            //recupere la catégorie sélectionnée par l'utilisateur
            String categorie = (String) listeCategorie.getSelectionModel().getSelectedItem();
            ObservableList<Objet> listeObj = null;
            try {
                //recupère la liste des objets de cette catégorie :
                listeObj = rechercheObjetParCategorie(con,categorie);
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.affichageResultats(con, listeObj);
            Frecherche.setText("");        
        });
        
        //action de l'appui sur le bouton recherche
        Brecherche.setOnAction((t) ->{
            //recupere le mot clé saisi par l'utilisateur
            String motcle = Frecherche.getText(); 
            ObservableList<Objet> listeObj = null;
            try {
                //recupère la liste des objets :
                listeObj = rechercheObjetParMotCle(con,motcle);
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.affichageResultats(con, listeObj);
            Frecherche.setText("");          
    });
        
        
    }
    
    
    //récupère le logo du site et l'enregistre dans un label pour être affiché par la suite
    public static Label recupererLogo() throws FileNotFoundException{
        FileInputStream image = new FileInputStream("src\\main\\java\\fr\\insa\\waille\\encheresmiq3\\GUIFX\\logo_lemauvaiscoin.png");
        Image i = new Image(image);
        ImageView imageView = new ImageView(i);
        imageView.setFitHeight(75);
        imageView.setFitWidth(185);
        Label logo = new Label("",imageView);
        return logo;
    }
    
    //affiche la liste des objets sélectionnés dans une TableView :
    public void affichageResultats(Connection con, ObservableList<Objet> listeObj){
                    
            TableView<Objet> table = new TableView<Objet>();
            //remplissage de la table avec les objets
            table.setItems(listeObj);
            
            //configuration de la table
            table.setEditable(true);
            
            //création des colonnes du tableau
            TableColumn coltitre = new TableColumn("Titre");
            TableColumn coldescription = new TableColumn("Description");
            TableColumn colprix = new TableColumn("Prix");
            coltitre.setMinWidth(200);
            coldescription.setMinWidth(200);
            colprix.setMinWidth(200);
            coltitre.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("titre"));

            coldescription.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("description"));

            colprix.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("prix_base"));

            table.getColumns().setAll(coltitre, coldescription, colprix);
            
            
            //ajout de la table à la fenêtre (sur 4 colonnes et 1 ligne)
            this.add(table, 0, 5,4,1);                  
    }
}