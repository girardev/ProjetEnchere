/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.encheresmiq3.modele;

import fr.insa.waille.encheresmiq3.GUIFX.Accueil;
import fr.insa.waille.encheresmiq3.GUIFX.GridPaneAuthentification;
import fr.insa.waille.encheresmiq3.GUIFX.ObjetPlus;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.defautConnect;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getPrixMaxSurObjet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.max;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 *
 * @author arvar
 */
public class Objet {
    
    private int id;
    private String titre;
    private String description;
    private String debut;
    private String fin;
    private int categorie;
    public int prix_base;
    private int propose_par;
    private BufferedImage image;
    private Button Bvoirplus;
    private int prix_actuel;
    Connection con;
    
    //constructeurs

    public Objet(int id, String titre, String description, String debut, String fin, int categorie, int prix_base,BufferedImage image, int propose_par) throws ClassNotFoundException, SQLException{
        this(id, titre, description, debut, fin, categorie, prix_base, image, propose_par, prix_base);
        
    }

    //constructeurs
    public Objet(int id, String titre, String description, String debut, String fin, int categorie, int prix_base, BufferedImage image, int propose_par, int prix_base2) throws ClassNotFoundException, SQLException {
        try {
            this.con = defautConnect();
        } catch (SQLException ex) {
            Logger.getLogger(Objet.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.debut = debut;
        this.fin = fin;
        this.categorie = categorie;
        this.prix_base = prix_base;
        this.propose_par = propose_par;
        this.image = image;
        this.Bvoirplus=new Button("Voir plus");
        this.prix_actuel = max(prix_base,getPrixMaxSurObjet(con, id));
        //action de l'appui sur le bouton voir plus
        Bvoirplus.setOnAction((t) ->{
            Connection con = null;
            try {
                con = defautConnect();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GridPaneAuthentification.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Stage stage = new Stage();
            Scene sc2 = null;
            try {
                sc2 = new Scene(new ObjetPlus(stage,con, (Objet) this));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Objet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Objet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Objet.class.getName()).log(Level.SEVERE, null, ex);
            }
            stage.setWidth(700);
            stage.setHeight(500);
            stage.setScene(sc2);
            stage.setTitle("Encheres");
            stage.show();
            
        });
    }

    public Objet(int id, String titre, String description, int prix_base) throws ClassNotFoundException, SQLException {
        this.con = defautConnect();
        this.id=id;
        this.titre=titre;
        this.description=description;
        this.prix_base=prix_base;
    }
    
    public String toString(){
        return "id : "+this.id+" titre : "+this.titre+" description : "+this.description;
    }
    //get/set :
    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getDebut() {
        return debut;
    }

    public String getFin() {
        return fin;
    }

    public int getCategorie() {
        return categorie;
    }

    public int getPrix_base() {
        return prix_base;
    }

    public int getPropose_par() {
        return propose_par;
    }
    
    public BufferedImage getImage(){
        return image;
    }
    
    public Button getBvoirplus() {
        return Bvoirplus;
    }
    
    public int getPrix_actuel() {
        return prix_actuel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }

    public void setPrix_base(int prix_base) {
        this.prix_base = prix_base;
    }

    public void setPropose_par(int propose_par) {
        this.propose_par = propose_par;
    }
    
    public void setBvoirplus(Button Bvoirplus) {
        this.Bvoirplus = Bvoirplus;
    }
    
    public void setPrix_actuel(int prix_actuel) {
        this.prix_base = prix_actuel;
    }
}
