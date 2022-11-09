/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.GUIFX;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
/**
 *
 * @author arvar
 */
public class HBoxTest extends HBox {
    public HBoxTest(){
        Button b1 = new Button("bouton1");
        Button b2 = new Button("bouton2");
        Button b3 = new Button("bouton3");
        this.getChildren().add(b1);
        this.getChildren().add(b2);
        this.getChildren().add(b3);
    }
    
}
