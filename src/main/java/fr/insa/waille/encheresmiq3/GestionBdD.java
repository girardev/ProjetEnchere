/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test git
 * @author grego_9h4zwhb
 */
public class GestionBdD {
    
    public static Connection connectGeneralPostGres(String host,
            int port, String database,
            String user, String pass)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    public static Connection defautConnect()
            throws ClassNotFoundException, SQLException {
        return connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "0000");
    }
    
    public static void creeSchema(Connection con)
            throws SQLException {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            // creation des tables
            st.executeUpdate(
                    """
                    create table utilisateur (
                        id integer not null primary key
                        generated always as identity,
                        nom varchar(30) not null,
                        prenom varchar(30) not null,
                        pass varchar(30) not null,
                        email varchar(50) not null unique,
                        code_postal varchar(30) not null
                    )
                    """);
            st.executeUpdate(
                    """
                    create table objet (
                        id integer not null primary key
                        generated always as identity,
                        titre varchar(500) not null,
                        description text not null,
                        debut timestamp without time zone not null,
                        fin timestamp without time zone not null,
                        prix_base integer not null,
                        categorie integer not null,
                        propose_par integer not null
                    )
                    """);
            st.executeUpdate(
                    """
                    create table enchere (
                        id integer not null primary key
                        generated always as identity,
                        quand timestamp without time zone not null,
                        montant integer not null,
                        de integer not null,
                        sur integer not null
                    )
                    """);
            st.executeUpdate(
                   """
                    create table categorie (
                       id integer not null primary key
                       generated always as identity,
                       nom varchar(50) not null
                    )
                    """);
            // je defini les liens entre les clés externes et les clés primaires
            // correspondantes
            st.executeUpdate(
                    """
                    alter table enchere
                        add constraint fk_enchere_de
                        foreign key (de) references utilisateur(id)
                    
                    """);
            st.executeUpdate(
                    """
                    alter table enchere
                        add constraint fk_enchere_sur
                        foreign key (sur) references objet(id)
                    
                    """);
            st.executeUpdate(
                    """
                    alter table objet
                        add constraint fk_objet_categorie
                        foreign key (categorie) references categorie(id)
                    """);
            st.executeUpdate(
                    """
                    alter table objet
                        add constraint fk_objet_propose_par
                        foreign key (propose_par) references utilisateur(id)
                    """);           
            // si j'arrive jusqu'ici, c'est que tout s'est bien passé
            // je confirme (commit) la transaction
            con.commit();
            // je retourne dans le mode par défaut de gestion des transaction :
            // chaque ordre au SGBD sera considéré comme une transaction indépendante
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            // quelque chose s'est mal passé
            // j'annule la transaction
            con.rollback();
            // puis je renvoie l'exeption pour qu'elle puisse éventuellement
            // être gérée (message à l'utilisateur...)
            throw ex;
        } finally {
            // je reviens à la gestion par défaut : une transaction pour
            // chaque ordre SQL
            con.setAutoCommit(true);
        }
    }
    
    public static void deleteSchema(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            // pour être sûr de pouvoir supprimer, il faut d'abord supprimer les liens
            // puis les tables
            // suppression des liens
            try {
                st.executeUpdate(
                        """
                    alter table enchere
                        drop constraint fk_enchere_de
                             """);
                System.out.println("constraint fk_enchere_de dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        """
                    alter table enchere
                        drop constraint fk_enchere_sur
                    """);
                System.out.println("constraint fk_enchere_sur dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        """
                    alter table objet
                        drop constraint fk_objet_categorie
                    """);
                System.out.println("constraint fk_objet_categorie dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        """
                    alter table objet
                        drop constraint fk_objet_propose_par
                    """);
                System.out.println("constraint fk_objet_propose_par dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            
            //une fois les contraintes supprimées, on peut supprimer les tables :
            try {
                st.executeUpdate(
                        """
                    drop table utilisateur
                    """);
                System.out.println("table utilisateur dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate(
                        """
                    drop table objet
                    """);
                System.out.println("table objet dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate(
                        """
                    drop table enchere
                    """);
                System.out.println("table enchere dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate(
                        """
                    drop table categorie
                    """);
                System.out.println("table categorie dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
        }
    }
    
    public static void afficheUtilisateurs(Connection con)
            throws SQLException{
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            ResultSet resultats = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des utilisateurs:
                    select * from utilisateur
                    """
            );
            System.out.println("Liste des utilisateurs :");
            while(resultats.next()){
                int id = resultats.getInt("id");
                String nom = resultats.getString("nom");
                String prenom = resultats.getString("prenom");
                String code_postal = resultats.getString("code_postal");
                String email = resultats.getString("email");               
                String pass = resultats.getString("pass");
                System.out.println(id+" : "+nom+" "+prenom+" "+email+" "+pass+" "+code_postal);
            }
        }
        catch (SQLException ex) {
            // quelque chose s'est mal passé
            // j'annule la transaction
            con.rollback();
            // puis je renvoie l'exeption pour qu'elle puisse éventuellement
            // être gérée (message à l'utilisateur...)
            throw ex;
        } finally {
            // je reviens à la gestion par défaut : une transaction pour
            // chaque ordre SQL
            con.setAutoCommit(true);
        } 
    }
    
    public static void creeUtilisateur(Connection con)
            throws SQLException {
        con.setAutoCommit(false);
        try (PreparedStatement pst = con.prepareStatement(
        """
                    insert into utilisateur (nom, prenom, pass, email, code_postal)
                    values (?, ?, ?, ?, ?)
                    """)) {
            System.out.println("nom utilisateur :");
            String nom = Lire.S();
            System.out.println("prenom utilisateur:");
            String prenom = Lire.S();
            System.out.println("pass utilisateur :");
            String pass = Lire.S();
            System.out.println("email utilisateur :");
            String email = Lire.S();
            System.out.println("code_postal utilisateur :");
            String code_postal = Lire.S(); 
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setString(3, pass);
            pst.setString(4, email);
            pst.setString(5,code_postal);
            pst.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
    
    public static void menuTextuel(Connection con){
        //menu permettant à l'utilisateur de choisir une action à effectuer sur la BdD
        boolean stop = false; //condition d'arret
        while(stop==false){
            System.out.println("Entrez un nombre pour sélectionner une option :");
            System.out.println("1 - Creation du schéma");
            System.out.println("2 - Suppression du schéma");
            System.out.println("3 - Affichage liste utilisateurs");
            System.out.println("4 - Ajouter un utilisateur");
            System.out.println("99 - Quitter");
            int reponse=-1; //reponse entrée par l'utilisateur
            while(reponse<0){
                reponse = Lire.i();
            }
            try{
                switch (reponse){
                    case 1 :                 
                        creeSchema(con);
                        System.out.println("Création schéma ON");
                        break;
                    case 2 :
                        deleteSchema(con);
                        System.out.println("Suppression schéma ON");
                        break;
                    case 3 :
                        afficheUtilisateurs(con);
                        System.out.println("utilisateurs récupérés OK");
                        break;
                    case 4 :
                        creeUtilisateur(con);
                        System.out.println("utilisateur créé OK");
                        break;  
                    case 99 :
                        stop = true;
                        System.out.println("Vous avez quitté le menu");
                        break;
                    default: 
                        System.out.println("pas encore défini");
                        break;               
                }
            }
            catch (SQLException ex) {
                throw new Error(ex);
            }
        }
    }

    
    public static void main(String[] args) {
        try {
            Connection con = defautConnect();
            menuTextuel(con);
//            System.out.println("Connection ON");
//            deleteSchema(con);
//            System.out.println("Suppression schéma ON");
//            creeSchema(con);
//            System.out.println("Création schéma ON");
//            afficheUtilisateurs(con);
//            System.out.println("utilisateurs récupérés OK");

        } catch (ClassNotFoundException ex) {
            throw new Error(ex);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }
    
}
