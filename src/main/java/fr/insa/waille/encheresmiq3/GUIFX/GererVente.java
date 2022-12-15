/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import fr.insa.encheresmiq3.modele.Objet;
import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheAllEnchere;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheObjetParMotCle;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author valen
 */
public class GererVente extends GridPane{

    public GererVente(Stage stage, Connection con) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException{
              
        //AFFICHAGE DU CONTENU DE LA FENETRE
        Label logo = recupererLogo();
        Label Lobj = new Label("Voici la liste de toutes les ventes d'objet");
        Button Bretour = new Button("Retour à l'accueil");
        Button Bactualiser = new Button("Actualiser le tableau");
        
        //AJOUT DES COMPOSANTS AU GRIDPANE
        this.add(logo, 2, 0);
        this.add(Lobj,1,1);
        this.add(Bretour,1,2);
        this.add(Bactualiser,3,2);
        
        
        ObservableList<Objet> listeAllObj = rechercheObjetParMotCle(con,"");
        affichageResultats(con,listeAllObj);
        
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
                Logger.getLogger(MesEncheres.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MesEncheres.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
        });
        
        //action de l'appui sur le bouton actualiser
        Bactualiser.setOnAction((t) ->{
            ObservableList listeAll;
            try {
                affichageResultats(con,listeAll=rechercheObjetParMotCle(con,""));
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
    }
    
    //affiche la liste des objets sélectionnés dans une TableView :
    public void affichageResultats(Connection con, ObservableList<Objet> listeAllObj){
        
        TableView<Objet> table = new TableView<Objet>();
            //remplissage de la table avec les objets
            table.setItems(listeAllObj);
            
            //configuration de la table
            table.setEditable(true);
            
            //création des colonnes du tableau
            TableColumn coltitre = new TableColumn("Titre");
            TableColumn coldescription = new TableColumn("Description");
            TableColumn colprix = new TableColumn("Prix de base");
            TableColumn colprixact = new TableColumn("Prix actuel");
            TableColumn colvoirplus = new TableColumn("Action");
            TableColumn colsuppr = new TableColumn("Action");
            
            coltitre.setMinWidth(150);
            coldescription.setMinWidth(150);
            colprix.setMinWidth(50);
            colprixact.setMinWidth(150);
            coltitre.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("titre"));

            coldescription.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("description"));

            colprix.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("prix_base"));
            
            colprixact.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("prix_actuel"));
            
            colvoirplus.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("Bvoirplus"));
            
            colsuppr.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("Bsuppr"));

            table.getColumns().setAll(coltitre, coldescription, colprix,colprixact, colvoirplus,colsuppr);
            
            table.setItems(listeAllObj);
            //ajout de la table à la fenêtre (sur 5 colonnes et 1 ligne)
            this.add(table, 0,5,5,1);
                           
    }
    
    
}

