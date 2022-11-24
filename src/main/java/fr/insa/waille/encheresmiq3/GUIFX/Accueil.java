

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.defautConnect;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheObjetparMotCle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.layout.GridPane;

/**
 *
 * @author valen
 */
public class Accueil extends GridPane {
    public Accueil(){
        
        Label titre = new Label("LeMauvaisCoin");
        titre.setStyle("-fx-max-width: 100");
        titre.setStyle("-fx-font-weight: bold");
        Label Lrecherche = new Label("Entrez un mot clé");
        TextField Frecherche = new TextField();
        Button Brecherche = new Button("Rechercher");
        Label Lcategorie = new Label("Catégories");
        Label panneau = new Label();
        
        ComboBox listeCategorie = new ComboBox();
        listeCategorie.getItems().setAll("Alcool","Mode");

        this.add(titre, 1, 0);
        this.add(Lrecherche,0,1);
        this.add(Frecherche,1,1);
        this.add(Brecherche,2,1);
        this.add(Lcategorie,0,2);
        this.add(listeCategorie,1,2);
        this.add(panneau,0,4);
        
        //action de l'appuie sur le bouton connexion
        Brecherche.setOnAction((t) ->{
            String motcle = Frecherche.getText();
            
            //initialisation de Connection con
            Connection con = null;
            try {
                con = defautConnect();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            try {
                Statement st = con.createStatement();
                String query ="select * from objet where titre like '%"+motcle+"%' or description like '%"+motcle+"%'";
                ResultSet resultat2 = st.executeQuery(query);
                
                
                TableView table = new TableView();
                ObservableList<Objet> donnee=null;
                while(resultat2.next()){
                    String titreobj = resultat2.getString("titre");
                    String descriptionobj = resultat2.getString("description");            
                    String prix_base = resultat2.getString("prix_base");
                    donnee = FXCollections.observableArrayList(
                    new Objet(titreobj, descriptionobj, prix_base));
                    }
                
                
                table.setItems(donnee);
                
                table.setEditable(true);
                TableColumn coltitre = new TableColumn("Titre");
                TableColumn coldescription = new TableColumn("Description");
                TableColumn colprix = new TableColumn("Prix");

                coltitre.setMinWidth(99);
                coldescription.setMinWidth(99);
                colprix.setMinWidth(99);

                coltitre.setCellValueFactory(
                        new PropertyValueFactory<Objet, String>("titre"));

                coldescription.setCellValueFactory(
                        new PropertyValueFactory<Objet, String>("description"));

                colprix.setCellValueFactory(
                        new PropertyValueFactory<Objet, String>("prix"));
                
                

                table.getColumns().addAll(coltitre, coldescription, colprix);
                
                this.add(table, 0, 5);
                
                

                
                Frecherche.setText("");
                
                
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
              
        });
        
        
        
        
    }
    
}