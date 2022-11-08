/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.guiFX;


import fr.insa.waille.encheresmiq3.bdd.GestionBdD;
import java.sql.SQLException;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * vue principale.
 *
 * @author francois
 */
public class VuePrincipale extends BorderPane {
    
    
    private HBox entete;
    
    private ScrollPane mainContent;
    
    public void setEntete(Node c) {
        this.setTop(c);
    }
    
    public void setMainContent(Node c) {
        this.mainContent.setContent(c);
    }
    
    public VuePrincipale() {
        this.mainContent = new ScrollPane();
        this.setCenter(this.mainContent);
        
    }
}
