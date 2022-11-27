/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.encheresmiq3.modele;

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
    private String categorie;
    private int prix_base;
    private int propose_par;
    
    //constructeurs
    public Objet(int id){
        this.id=id;
    }
    public Objet(int id, String titre, int prix_base){
        this.id=id;
        this.titre=titre;
        this.prix_base=prix_base;
    }
    public Objet(int id, String titre, String description, String categorie, int prix_base){
        this.id=id;
        this.titre=titre;
        this.description=description;
        this.categorie=categorie;
        this.prix_base=prix_base;
    }

    public Objet(int id, String titre, String description, int prix_base) {
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

    public String getCategorie() {
        return categorie;
    }

    public int getPrix_base() {
        return prix_base;
    }

    public int getPropose_par() {
        return propose_par;
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

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setPrix_base(int prix_base) {
        this.prix_base = prix_base;
    }

    public void setPropose_par(int propose_par) {
        this.propose_par = propose_par;
    }
    
}
