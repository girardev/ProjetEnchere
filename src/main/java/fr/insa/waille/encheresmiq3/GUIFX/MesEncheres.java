/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import fr.insa.encheresmiq3.modele.Objet;
import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheEnchereParUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheObjetParUtilisateur;
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
public class MesEncheres extends GridPane{

    public MesEncheres(Stage stage, Connection con) throws FileNotFoundException, SQLException, IOException{
              
        //AFFICHAGE DU CONTENU DE LA FENETRE
        Label logo = recupererLogo();
        Label Lobj = new Label("Voici les enchères que vous avez proposés");
        Button Bretour = new Button("Retour à l'accueil");
        
        //AJOUT DES COMPOSANTS AU GRIDPANE
        this.add(logo, 1, 0);
        this.add(Lobj,1,1);
        this.add(Bretour,1,2);
        
        String email = getEmailUtilisateurEnCours(con);
        int idUser = getIdUtilisateur(con,email);
        ObservableList<Objet> listeAllEnchere = rechercheEnchereParUtilisateur(con,idUser);
        TableView<Objet> table = new TableView<Objet>();
            //remplissage de la table avec les objets
            table.setItems(listeAllEnchere);
            
            //configuration de la table
            table.setEditable(true);
            
            //création des colonnes du tableau
            TableColumn colquand = new TableColumn("Quand");
            TableColumn colmontant = new TableColumn("Montant");
            TableColumn colsur = new TableColumn("Sur");
            colquand.setMinWidth(200);
            colmontant.setMinWidth(200);
            colsur.setMinWidth(200);
            colquand.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("quand"));

            colmontant.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("montant"));
            
            colsur.setCellValueFactory(
                    new PropertyValueFactory<Objet, String>("objet"));

            table.getColumns().setAll(colquand, colmontant, colsur);
            
            table.setItems(listeAllEnchere);
            //ajout de la table à la fenêtre (sur 5 colonnes et 1 ligne)
            this.add(table, 0, 5,5,1);
        
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
        
    }
}