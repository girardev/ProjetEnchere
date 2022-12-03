

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import javafx.application.Application;
import fr.insa.encheresmiq3.modele.Objet;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.defautConnect;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getCategories;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getIdCategorie;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getPrixMaxSurObjet;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getRoleUtilisateurEnCours;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheObjetParCategorie;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.rechercheObjetParMotCle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.max;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javax.swing.Box;
import javax.swing.GroupLayout.Group;


/**
 *
 * @author valen
 */
public class Accueil extends GridPane {
    
    public Accueil(Stage stage, Connection con) throws FileNotFoundException, SQLException, IOException, ClassNotFoundException{
              
        //AFFICHAGE DU CONTENU DE LA FENETRE
        Label logo = recupererLogo();
        logo.setStyle(" -fx-border-width:5px ;-fx-border-style: solid; -fx-border-color: orange; ");
        Label Lrecherche = new Label("Entrez un mot clé");
        TextField Frecherche = new TextField();
        Button Brecherche = new Button("Rechercher");
        Label Lcategorie = new Label("Catégories");
        Button Bcategorie = new Button("Par catégorie");
        Label panneau = new Label();
        Button Bactualiser = new Button("Actualiser le tableau");
        
        
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
        
        ObservableList<Objet> listeAllObj = rechercheObjetParMotCle(con,"");
        affichageResultats(con,listeAllObj);
            
        // Create MenuBar
        MenuBar leftBar = new MenuBar();
        MenuBar rightBar = new MenuBar();
        
        // Create menus
        Menu creationMenu = new Menu("Création");
        Menu espaceMenu = new Menu("Mon espace");
        Menu gestionMenu = new Menu("Gestion");
        Menu quitterMenu = new Menu("Quitter");
        
        
        //create menu items
        MenuItem creerobj = new MenuItem("Créer une vente d'objet");
        MenuItem creercat = new MenuItem("Créer une catégorie");
        MenuItem mesencheres = new MenuItem("Mes enchères");
        MenuItem mesobjets = new MenuItem("Mes objets");
        MenuItem gestionrole = new MenuItem("Gestion des rôles");
        MenuItem deco = new MenuItem("Déconnexion");
        MenuItem fermer = new MenuItem("Fermer");
        
        
        // Add Menus to the MenuBar
        leftBar.getMenus().addAll(creationMenu,espaceMenu);
        
        rightBar.getMenus().addAll(quitterMenu);
        
        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menubars = new HBox(leftBar, spacer, rightBar);
        
        String role = null;
        try {
            role = getRoleUtilisateurEnCours(con);
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(role.equals("Admin")){
            leftBar.getMenus().addAll(gestionMenu);
            gestionMenu.getItems().addAll(gestionrole);
        }
        if(role.equals("Admin")||role.equals("Categorie")){
            creationMenu.getItems().addAll(creerobj,creercat);
            
        }
        else{
            creationMenu.getItems().addAll(creerobj);
        }
        
        espaceMenu.getItems().addAll(mesencheres,mesobjets);
        
        quitterMenu.getItems().addAll(deco,fermer);
        
        this.add(menubars,0,0,10,1);
        
        
        
        //AJOUT DES COMPOSANTS AU GRIDPANE
        this.add(logo, 2, 1);
        this.add(Lrecherche,0,2);
        this.add(Frecherche,1,2);
        this.add(Brecherche,2,2);
        this.add(Lcategorie,0,3);
        this.add(listeCategorie,1,3);
        this.add(Bcategorie,2,3);
        this.add(Bactualiser,3,3);
        this.add(panneau,0,5);
        
        
        
        
        //action de l'appui sur le bouton recherche par catégorie
        Brecherche.setOnAction((t) ->{
            //recupere la catégorie sélectionnée par l'utilisateur
            String motcle = Frecherche.getText();
            ObservableList<Objet> listeObjet = null;
            try {
                //recupère la liste des objets de cette catégorie :
                listeObjet = rechercheObjetParMotCle(con,motcle);
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.affichageResultats(con, listeObjet);
                
            
        });
        
        //action de l'appui sur le bouton recherche par catégorie
        Bcategorie.setOnAction((t) ->{
            //recupere la catégorie sélectionnée par l'utilisateur
            String categorie = (String) listeCategorie.getSelectionModel().getSelectedItem();
            int idcat = 0;
            try {
                idcat = getIdCategorie(con,categorie);
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
            ObservableList<Objet> listeObjet = null;
            try {
                //recupère la liste des objets de cette catégorie :
                listeObjet = rechercheObjetParCategorie(con,idcat);
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.affichageResultats(con, listeObjet);
                
            
        });
        
        //action de l'appui sur le bouton recherche
        Bactualiser.setOnAction((t) ->{
            ObservableList listeObjets;
            try {
                affichageResultats(con,listeObjets=rechercheObjetParMotCle(con,""));
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        //action de l'appui sur le bouton créer catégorie
        creercat.setOnAction((t) ->{
            
                
            try {
                Scene sc3 = new Scene(new CreerCat(stage,con));
                stage.setScene(sc3);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                
        });
        
        //action de l'appui sur le bouton gerer role
        gestionrole.setOnAction((t) ->{
            
                
            try {
                Scene sc4 = new Scene(new GererRole(stage,con));
                stage.setScene(sc4);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                
        });
        
        //action de l'appui sur le bouton créer OBJET
        creerobj.setOnAction((t) ->{
            
                
            try {
                Scene sc5 = new Scene(new CreerObjet(stage,con));
                stage.setScene(sc5);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                
        });
        
        //action de l'appui sur le bouton deconnexion
        deco.setOnAction((t) ->{
               
            Scene sc6 = new Scene(new GridPaneAuthentification(stage,con));
            stage.setScene(sc6);
    
        });
        
        //action de l'appui sur le bouton créer OBJET
        mesobjets.setOnAction((t) ->{
               
            Scene sc7 = null;
            try {
                sc7 = new Scene(new MesObjets(stage,con));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
            stage.setScene(sc7);
    
        });
        
        //action de l'appui sur le bouton créer OBJET
        mesencheres.setOnAction((t) ->{
               
            Scene sc8 = null;
            try {
                sc8 = new Scene(new MesEncheres(stage,con));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }
            stage.setScene(sc8);
    
        });
        
        //action de l'appui sur le bouton fermer
        fermer.setOnAction((t) ->{
               
           System.exit(0);
    
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
        logo.setStyle(" -fx-border-width:5px ;-fx-border-style: solid; -fx-border-color: orange; ");
        return logo;
    }
    
    //affiche la liste des objets sélectionnés dans une TableView :
    public void affichageResultats(Connection con, ObservableList<Objet> listeObjet){
                    
            TableView<Objet> table = new TableView<Objet>();
            //remplissage de la table avec les objets
            table.setItems(listeObjet);
            
            //configuration de la table
            table.setEditable(true);
            
            //création des colonnes du tableau
            TableColumn coltitre = new TableColumn("Titre");
            TableColumn coldescription = new TableColumn("Description");
            TableColumn colprix = new TableColumn("Prix de base");
            TableColumn colprixact = new TableColumn("Prix actuel");
            TableColumn colvoirplus = new TableColumn("Action");
            coltitre.setMinWidth(150);
            coldescription.setMinWidth(150);
            colprix.setMinWidth(150);
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

            table.getColumns().setAll(coltitre, coldescription, colprix,colprixact, colvoirplus);
            
            table.setItems(listeObjet);
            //ajout de la table à la fenêtre (sur 4 colonnes et 1 ligne)
            this.add(table, 0, 5,5,1);                  
    }
    
    
}
