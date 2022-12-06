/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import fr.insa.encheresmiq3.modele.Objet;
import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheAllEnchere;
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
public class GererEnchere extends GridPane{

    public GererEnchere(Stage stage, Connection con) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException{
              
        //AFFICHAGE DU CONTENU DE LA FENETRE
        Label logo = recupererLogo();
        Label Lobj = new Label("Voici les enchères que vous avez proposés");
        Button Bretour = new Button("Retour à l'accueil");
        Button Bactualiser = new Button("Actualiser le tableau");
        
        //AJOUT DES COMPOSANTS AU GRIDPANE
        this.add(logo, 2, 0);
        this.add(Lobj,1,1);
        this.add(Bretour,1,2);
        this.add(Bactualiser,3,2);
        
        
        ObservableList<Objet> listeAllEnchere = rechercheAllEnchere(con);
        affichageResultats(con,listeAllEnchere);
        
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
                affichageResultats(con,listeAll=rechercheAllEnchere(con));
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
    public void affichageResultats(Connection con, ObservableList<Objet> listeAllEnchere){
        
        TableView<Objet> table = new TableView<Objet>();
            //remplissage de la table avec les objets
            table.setItems(listeAllEnchere);
            
            //configuration de la table
            table.setEditable(true);
            
            //création des colonnes du tableau
            TableColumn colquand = new TableColumn("Quand");
            TableColumn colmontant = new TableColumn("Montant");
            TableColumn colsur = new TableColumn("Sur");
            TableColumn colfin = new TableColumn("Clôture");
            TableColumn colpar = new TableColumn("Par");
            TableColumn colaction = new TableColumn("Action");
            colquand.setMinWidth(100);
            colmontant.setMinWidth(50);
            colsur.setMinWidth(100);
            colfin.setMinWidth(100);
            colpar.setMinWidth(225);
            colaction.setMinWidth(100);
            colquand.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("quand"));

            colmontant.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("montant"));
            
            colsur.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("objet"));
            colfin.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("fin"));
            colpar.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("par"));
            colaction.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("Bsupprimer"));
            table.getColumns().setAll(colquand, colmontant, colsur,colfin, colpar,colaction);
            
            table.setItems(listeAllEnchere);
            //ajout de la table à la fenêtre (sur 5 colonnes et 1 ligne)
            this.add(table, 0,5,5,1);
                           
    }
    
    
}
