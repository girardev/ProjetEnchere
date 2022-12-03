/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.encheresmiq3.modele;

import fr.insa.waille.encheresmiq3.GUIFX.GridPaneAuthentification;
import static fr.insa.waille.encheresmiq3.bdd.GestionBdD.defautConnect;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author valen
 */
public class Enchere {
    private int id;
    private String quand;
    private int montant;
    private int de;
    private int sur;
    private String objet;
        

    public Enchere(int id, String quand, int montant, int de, int sur,String objet) {
        this.id = id;
        this.quand = quand;
        this.montant = montant;
        this.de = de;
        this.sur = sur;
        this.objet= objet;
    }
    
    public int getId() {
        return id;
    }
    
    public String getQuand() {
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
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setQuand(String quand) {
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
    
}
