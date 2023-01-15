/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.encheresmiq3.modele;

import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.SupprimerEnchere;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.defautConnect;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getEmailUtilisateur;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getFinObjet;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.getPrixMaxSurObjet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;

/**
 *
 * @author valen
 */
public class Enchere {
    private int id;
    private Timestamp quand;
    private int montant;
    private int de;
    private int sur;
    private String objet;
    private Timestamp fin;
    private String meilleur;
    private Button Bsupprimer;
    Connection con;
    private String par;
        

    public Enchere(int id, Timestamp quand, int montant, int de, int sur,String objet) throws ClassNotFoundException, SQLException {
        this.con = defautConnect();
        this.id = id;
        this.quand = quand;
        this.montant = montant;
        this.de = de;
        this.sur = sur;
        this.objet= objet;
        this.fin=getFinObjet(con,sur);
        if(getPrixMaxSurObjet(con,sur)==montant){this.meilleur="Oui";}
        else{this.meilleur="Non";}
        this.Bsupprimer=new Button("Supprimer");
        this.par=getEmailUtilisateur(con,de);
        
        //action de l'appui sur le bouton supprimer
        Bsupprimer.setOnAction((t) ->{
            
            try {
                con = defautConnect();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Enchere.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Enchere.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                SupprimerEnchere(con,id);
            } catch (SQLException ex) {
                Logger.getLogger(Enchere.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
    }
    
    public int getId() {
        return id;
    }
    
    public Timestamp getQuand() {
        return quand;
    }
    
    public int getMontant() {
        return montant;
    }
    
    public int getDe() {
        return de;
    }
    
    public int getSur() {
        return sur;
    }
    
    public String getObjet() {
        return objet;
    }
    
    public Timestamp getFin() {
        return fin;
    }
    
    public String getMeilleur() {
        return meilleur;
    }
    
    public Button getBsupprimer() {
        return Bsupprimer;
    }
    
    public String getPar() {
        return par;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setQuand(Timestamp quand) {
        this.quand = quand;
    }
    
    public void setMontant(int montant) {
        this.montant = montant;
    }
    
    public void setDe(int de) {
        this.de = de;
    }
    
    public void setSur(int sur) {
        this.sur = sur;
    }
    
    public void setObjet(String objet) {
        this.objet = objet;
    }
    
    public void setFin(Timestamp fin) {
        this.fin = fin;
    }
    
    public void setMeilleur(String meilleur) {
        this.meilleur = meilleur;
    }
    
    public void setBsupprimer(Button Bsupprimer) {
        this.Bsupprimer = Bsupprimer;
    }
    
    public void setPar(String par) {
        this.par = par;
    }
    
}
