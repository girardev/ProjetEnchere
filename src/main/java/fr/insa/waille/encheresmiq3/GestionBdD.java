/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
//            st.executeUpdate(
//                    """
//                    create table aime (
//                        u1 integer not null,
//                        u2 integer not null
//                    )
//                    """);
//            // je defini les liens entre les clés externes et les clés primaires
//            // correspondantes
//            st.executeUpdate(
//                    """
//                    alter table aime
//                        add constraint fk_aime_u1
//                        foreign key (u1) references utilisateur(id)
//                    """);
//            st.executeUpdate(
//                    """
//                    alter table aime
//                        add constraint fk_aime_u2
//                        foreign key (u2) references utilisateur(id)
//                    """);
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
//            try {
//                st.executeUpdate(
//                        """
//                    alter table aime
//                        drop constraint fk_aime_u1
//                             """);
//                System.out.println("constraint fk_aime_u1 dropped");
//            } catch (SQLException ex) {
//                // nothing to do : maybe the constraint was not created
//            }
//            try {
//                st.executeUpdate(
//                        """
//                    alter table aime
//                        drop constraint fk_aime_u2
//                    """);
//                System.out.println("constraint fk_aime_u2 dropped");
//            } catch (SQLException ex) {
//                // nothing to do : maybe the constraint was not created
//            }
//            // je peux maintenant supprimer les tables
//            try {
//                st.executeUpdate(
//                        """
//                    drop table aime
//                    """);
//                System.out.println("dable aime dropped");
//            } catch (SQLException ex) {
//                // nothing to do : maybe the table was not created
//            }
            try {
                st.executeUpdate(
                        """
                    drop table utilisateur
                    """);
                System.out.println("table utilisateur dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
        }
    }

    public static void main(String[] args) {
        try {
            Connection con = defautConnect();
            System.out.println("Connection ON");
            //creeSchema(con);
            System.out.println("Création schéma ON");
            deleteSchema(con);
            System.out.println("Suppression schéma ON");
        } catch (ClassNotFoundException ex) {
            throw new Error(ex);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }
    
}
