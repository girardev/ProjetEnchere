/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import fr.insa.encheresmiq3.modele.Enchere;
import static fr.insa.waille.encheresmiq3.GUIFX.Accueil.recupererLogo;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheEnchereParUtilisateur;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author valen
 */
public class MesEncheres extends GridPane{

    public MesEncheres(Stage stage, Connection con) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException{
         
        
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
        
        
        String email = getEmailUtilisateurEnCours(con);
        int idUser = getIdUtilisateur(con,email);
        ObservableList<Enchere> listeAllEnchere = rechercheEnchereParUtilisateur(con,idUser);
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
                affichageResultats(con,listeAll=rechercheEnchereParUtilisateur(con,idUser));
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
    public void affichageResultats(Connection con, ObservableList<Enchere> listeAllEnchere){
        TableView<Enchere> table = new TableView<Enchere>();
                //remplissage de la table avec les objets
                table.setItems(listeAllEnchere);

                //configuration de la table
                table.setEditable(true);

                //création des colonnes du tableau
                TableColumn colquand = new TableColumn("Quand");
                TableColumn colmontant = new TableColumn("Montant");
                TableColumn colsur = new TableColumn("Sur");
                TableColumn colfin = new TableColumn("Clôture");
                TableColumn colmeilleur = new TableColumn("Possession");
                TableColumn colaction = new TableColumn("Action");
                colquand.setMinWidth(125);
                colmontant.setMinWidth(100);
                colsur.setMinWidth(125);
                colfin.setMinWidth(125);
                colmeilleur.setMinWidth(100);
                colaction.setMinWidth(100);
                colquand.setCellValueFactory(
                        new PropertyValueFactory<Enchere, String>("quand"));

                colmontant.setCellValueFactory(
                        new PropertyValueFactory<Enchere, String>("montant"));

                colsur.setCellValueFactory(
                        new PropertyValueFactory<Enchere, String>("objet"));
                
                colfin.setCellValueFactory(
                        new PropertyValueFactory<Enchere, String>("fin"));
                
                colmeilleur.setCellValueFactory(
                        new PropertyValueFactory<Enchere, String>("meilleur"));
                
                colaction.setCellValueFactory(
                        new PropertyValueFactory<Enchere, String>("Bsupprimer"));

                table.getColumns().setAll(colquand, colmontant, colsur,colfin,colmeilleur,colaction);

                
                
                int n = listeAllEnchere.size();
                for (int i = 0; i<n ; i++){
                    Timestamp finito = listeAllEnchere.get(i).getFin();
                    Timestamp now = Timestamp.valueOf(LocalDate.now().atTime(LocalTime.now()).toString().replace("T", " "));
                    //if(now.after(finito)){
                       

                    //}
                
                table.setRowFactory(tv -> {
                         TableRow<Enchere> row = new TableRow<>();
                         if (row.getIndex() == 1) {
                            row.setStyle("-fx-background-color: red;");
                         }
                         return row ;
                });    
                
                table.setItems(listeAllEnchere);    
                   
                }
                 
                //ajout de la table à la fenêtre (sur 5 colonnes et 1 ligne)
                this.add(table, 0, 5,5,1);
    }
}