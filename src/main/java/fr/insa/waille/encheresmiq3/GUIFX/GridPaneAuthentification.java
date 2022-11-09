/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author arvar
 */
public class GridPaneAuthentification extends GridPane {
    public GridPaneAuthentification(){
        //PARTIE INSCRIPTION
        //création des labels et champs de saisie
        Label titre = new Label("Inscription");
        Label labelNom = new Label("nom");
        TextField nom = new TextField();
        Label labelPrenom = new Label("prenom");
        TextField prenom = new TextField();
        Label labelPass = new Label("password");
        TextField pass = new TextField();
        Label labelEmail = new Label("email");
        TextField email = new TextField();
        //ajout des composants au GridPane (colonne,ligne)
        this.add(titre,0,0);
        this.add(labelNom, 0, 1);
        this.add(nom, 1, 1);
        this.add(labelPrenom, 0, 2);
        this.add(prenom, 1, 2);
        this.add(labelPass, 0, 3);
        this.add(pass, 1, 3);
        this.add(labelEmail, 0, 4);
        this.add(email, 1, 4);
        //PARTIE CONNEXION
        //création des labels et champs de saisie
        Label titre2 = new Label("Connexion");
        Label labelPass2 = new Label("password");
        TextField pass2 = new TextField();
        Label labelEmail2 = new Label("email");
        TextField email2 = new TextField();
        //ajout des composants au GridPane (colonne,ligne)
        this.add(titre2,2,0);
        this.add(labelPass2, 2, 1);
        this.add(pass2, 3, 1);
        this.add(labelEmail2, 2, 2);
        this.add(email2, 3, 2);

    }
}
