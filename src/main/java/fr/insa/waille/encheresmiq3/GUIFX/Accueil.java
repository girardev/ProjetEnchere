

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author valen
 */
public class Accueil extends GridPane {
    public Accueil(){
        
        Label titre = new Label("LeMauvaisCoin");
        titre.setStyle("-fx-max-width: 100");
        titre.setStyle("-fx-font-weight: bold");
        Label Lrecherche = new Label("Entrez un mot clé");
        TextField Trecherche = new TextField();
        Button Brecherche = new Button("Rechercher");
        Label Lcategorie = new Label("Catégories");
        
        ComboBox listeCategorie = new ComboBox();
        listeCategorie.getItems().setAll("Alcool","Mode");

        this.add(titre, 1, 0);
        this.add(Lrecherche,0,1);
        this.add(Trecherche,1,1);
        this.add(Brecherche,2,1);
        this.add(Lcategorie,0,2);
        this.add(listeCategorie,1,2);
    }
    
}