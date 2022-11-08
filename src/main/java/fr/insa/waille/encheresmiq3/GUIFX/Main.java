/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.guiFX;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author francois
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene sc = new Scene(new VuePrincipale());
//        Scene sc = new Scene(new TestFx());
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setScene(sc);
        stage.setTitle("Encheres");
          stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}