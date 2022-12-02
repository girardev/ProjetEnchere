/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.waille.encheresmiq3.bdd;

import fr.insa.encheresmiq3.modele.Objet;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.imageio.ImageIO;

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
        return connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "azerty");
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
                        code_postal varchar(30) not null,
                        role varchar(50) not null
                    )
                    """);
            st.executeUpdate(
                    """
                    create table objet (
                        id integer not null primary key
                        generated always as identity,
                        titre varchar(500) not null,
                        description text not null,
                        debut varchar(200) not null,
                        fin varchar(200) not null,
                        prix_base integer not null,
                        categorie integer not null,
                        propose_par integer not null,
                        image bytea
                    )
                    """);
            st.executeUpdate(
                    """
                    create table enchere (
                        id integer not null primary key
                        generated always as identity,
                        quand varchar(200) not null,
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
            st.executeUpdate(
                   """
                    create table UtilisateurEnCours (
                       id integer not null primary key
                       generated always as identity,
                       email varchar(50) not null,
                       pass varchar(30) not null,
                       role varchar(50) not null
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
            try {
                st.executeUpdate(
                        """
                    drop table UtilisateurEnCours
                    """);
                System.out.println("table UtilisateurEnCours dropped");
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
    
    public static void creeUtilisateur(Connection con,String nom,String prenom,String pass,String email,String code_postal,String role)
            throws SQLException {
        con.setAutoCommit(false);
        try (PreparedStatement pst = con.prepareStatement(
        """
                    insert into utilisateur (nom, prenom, pass, email, code_postal, role)
                    values (?, ?, ?, ?, ?, ?)
                    """)) {
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setString(3, pass);
            pst.setString(4, email);
            pst.setString(5,code_postal);
            pst.setString(6, role);
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
        public static void demandeUtilisateur(Connection con)
            throws SQLException {
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
            System.out.println("role utilisateur :");
            String role = Lire.S(); 
            creeUtilisateur(con,nom,prenom,pass,email,code_postal,role);
            
    }
    
    public static int getIdUtilisateur(Connection con, String email)
            throws SQLException{
        ResultSet resultat;
        int id = -1;
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
           resultat = st.executeQuery(
                   "select id from utilisateur where email like'"+email+"'"
           );
           //sauvegarde les résultats
            while(resultat.next()){
                id=resultat.getInt("id");
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
        return id;
    }
    
    public static String getRoleUtilisateur(Connection con, String email)
            throws SQLException{
        ResultSet resultat;
        String role = null;
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
           resultat = st.executeQuery(
                   "select role from utilisateur where email like'"+email+"'"
           );
           //sauvegarde les résultats
            while(resultat.next()){
                role=resultat.getString("role");
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
        return role;
    }
    
    public static String getUtilisateur(Connection con,int id)
            throws SQLException{
        ResultSet resultat;
        String nomprenom = null;
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            resultat = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des categories ;
                    select nom,prenom from utilisateur where id ="""+id+"""
                                                               
                    """
            );
            //sauvegarde les résultats
            while(resultat.next()){
               nomprenom = resultat.getString("nom");
               nomprenom = nomprenom+" "+resultat.getString("prenom");
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
        return nomprenom;
    }
    
    public static ArrayList getAllUsers(Connection con)
            throws SQLException{
        ResultSet resultat;
        ArrayList<String> listeUsers = new ArrayList<String>();
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            resultat = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des categories ;
                    select email from utilisateur
                    """
            );
            //sauvegarde les résultats
            while(resultat.next()){
                listeUsers.add(resultat.getString("email"));
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
        return listeUsers;
    }
    
    public static void ModifierRoleUtilisateur(Connection con,String email,String role)
            throws SQLException {
        con.setAutoCommit(false);
        try (PreparedStatement pst = con.prepareStatement(
        "update utilisateur set role = '"+role+"' where email like '"+email+"' "))
        {
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
    
    public static void creeUtilisateurEnCours(Connection con,String email,String pass,String role)
            throws SQLException {
        con.setAutoCommit(false);
        try (PreparedStatement pst = con.prepareStatement(
        """
                    insert into UtilisateurEnCours (email, pass, role)
                    values (?, ?, ?)
                    """)) {
            pst.setString(1, email);
            pst.setString(2, pass);
            pst.setString(3, role);
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

    public static String getRoleUtilisateurEnCours(Connection con)
            throws SQLException{
        ResultSet resultat;
        String role = null;
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            resultat = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des roles ;
                    select role from UtilisateurEnCours
                    """
            );
            //sauvegarde les résultats
            while(resultat.next()){
                role = resultat.getString("role");
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
        return role;
    }    
    
    public static String getEmailUtilisateurEnCours(Connection con)
            throws SQLException{
        ResultSet resultat;
        String email = null;
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            resultat = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des emails;
                    select email from UtilisateurEnCours
                    """
            );
            //sauvegarde les résultats
            while(resultat.next()){
                email = resultat.getString("email");
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
        return email;
    }
    
    public static void ModifierRoleUtilisateurEnCours(Connection con,String email,String role)
            throws SQLException {
        con.setAutoCommit(false);
        try (PreparedStatement pst = con.prepareStatement(
        "update UtilisateurEnCours set role = '"+role+"' where email like '"+email+"' "))
        {
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
    
    
    public static void afficheEncheres(Connection con)
                throws SQLException{
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            ResultSet resultats = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des encheres:
                    select * from enchere
                    """
            );
            System.out.println("Liste des encheres :");
            while(resultats.next()){
                int id = resultats.getInt("id");
                String quand = resultats.getString("quand");
                int montant = resultats.getInt("montant");
                int de = resultats.getInt("de");
                int sur = resultats.getInt("sur");              
                System.out.println(id+" : "+quand+" "+montant+"€ "+de+" "+sur);
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
    
    public static void creeEnchere(Connection con,String quand,int montant,int de,int sur)
                throws SQLException {
        con.setAutoCommit(false);
        try (PreparedStatement pst = con.prepareStatement(
        """
                    insert into enchere(quand, montant, de, sur)
                    values (?, ?, ?, ?)
                    """)) {
            pst.setString(1, quand);
            pst.setInt(2, montant);
            pst.setInt(3, de);
            pst.setInt(4, sur);
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
    
    public static int getPrixMaxSurObjet(Connection con, int idObj)
            throws SQLException{
        ResultSet resultat;
        int prixmax = 0;
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
           resultat = st.executeQuery(
                   "select MAX(montant) from enchere where sur="+idObj+" "
           );
           //sauvegarde les résultats
            while(resultat.next()){
                prixmax=resultat.getInt(1);
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
        return prixmax;
    }
    
    public static void demandeEnchere(Connection con)
                throws SQLException {
            System.out.println("quand :");
            String quand = Lire.S();
            System.out.println("montant:");
            int montant = Lire.i();
            System.out.println("de :");
            int de = Lire.i();
            System.out.println("sur :");
            int sur = Lire.i();
            creeEnchere(con, quand, montant, de, sur);
            
    }
    
    
    
    public static void afficheCategorie(Connection con)
            throws SQLException{
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            ResultSet resultats = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des categories ;
                    select * from categorie
                    """
            );
            System.out.println("Liste des categories :");
            while(resultats.next()){
                int id = resultats.getInt("id");
                String nom = resultats.getString("nom");
                System.out.println(id+" : "+nom);
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
    
    public static ArrayList getCategories(Connection con)
            throws SQLException{
        ResultSet resultat;
        ArrayList<String> listeCategories = new ArrayList<String>();
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            resultat = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des categories ;
                    select * from categorie
                    """
            );
            //sauvegarde les résultats
            while(resultat.next()){
                listeCategories.add(resultat.getString("nom"));
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
        return listeCategories;
    }
    
    public static String getNomCategorie(Connection con,int id)
            throws SQLException{
        ResultSet resultat;
        String nom = null;
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            resultat = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des categories ;
                    select nom from categorie where id ="""+id+"""
                                                               
                    """
            );
            //sauvegarde les résultats
            while(resultat.next()){
               nom = resultat.getString("nom");
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
        return nom;
    }
    
    public static int getIdCategorie(Connection con, String nom)
            throws SQLException{
        ResultSet resultat;
        int id = -1;
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
           resultat = st.executeQuery(
                   "select id from categorie where nom like'"+nom+"'"
           );
           //sauvegarde les résultats
            while(resultat.next()){
                id=resultat.getInt("id");
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
        return id;
    }
    
    public static void creeCategorie(Connection con,String nom)
            throws SQLException {
        con.setAutoCommit(false);
        try (PreparedStatement pst = con.prepareStatement(
        """
                    insert into categorie (nom)
                    values (?)
                    """)) {
            pst.setString(1, nom);
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
    
    public static ArrayList getObjets(Connection con)
            throws SQLException, IOException{
        ResultSet resultat;
        ArrayList<Objet> listeObjets = new ArrayList<Objet>();
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            resultat = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des objets ;
                    select * from objet
                    """
            );
            //sauvegarde les résultats
            while(resultat.next()){
                int id = resultat.getInt("id");
                String titre = resultat.getString("titre");
                String description = resultat.getString("description");
                String debut = resultat.getString("debut");
                String fin = resultat.getString("fin");
                int categorie = resultat.getInt("categorie");
                int prix_base = resultat.getInt("prix_base"); 
                int propose_par = resultat.getInt("propose_par");
                //conversion image array byte -> image
                byte[] byteImage = resultat.getBytes("image");
                ByteArrayInputStream inStreambj = new ByteArrayInputStream(byteImage);
                BufferedImage newImage = ImageIO.read(inStreambj);
                listeObjets.add(new Objet(id,titre,description,debut,fin,categorie,prix_base,newImage,propose_par));
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
        return listeObjets;
    }
    
    public static void demandeCategorie(Connection con)
            throws SQLException {
            System.out.println("nom categorie :");
            String nom = Lire.S();
            creeCategorie(con, nom);
    }    
    
    public static void afficheObjets(Connection con)
            throws SQLException{
        con.setAutoCommit(false);
        try(Statement st = con.createStatement()){
            ResultSet resultats = st.executeQuery(
                    """
                    ---ordre SQL pour récupérer la liste des objets:
                    select * from objet
                    """
            );
            System.out.println("Liste des objets :");
            while(resultats.next()){
                int id = resultats.getInt("id");
                String titre = resultats.getString("titre");
                String description = resultats.getString("description");
                String debut = resultats.getString("debut");
                String fin = resultats.getString("fin");              
                String prix_base = resultats.getString("prix_base");
                int categorie = resultats.getInt("categorie");
                int propose_par = resultats.getInt("propose_par");
                System.out.println(id+" : "+titre+" "+description+" "+debut+" "+fin+" "+prix_base+" "+categorie+" "+propose_par);
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
    
    public static ObservableList rechercheObjetParCategorie(Connection con,int categorie)
            throws SQLException, IOException{
        con.setAutoCommit(false);
        ObservableList<Objet> listeObj = FXCollections.observableArrayList();
        try(Statement st = con.createStatement()){
            String query = "select objet.id, titre, description, debut, fin, prix_base, propose_par from objet join categorie on objet.categorie = categorie.id where categorie.id = "+categorie+" ";

            ResultSet resultats = st.executeQuery(query);
            System.out.println("Liste des objets :");
            while(resultats.next()){
                //String nom_categorie = resultats.getString("categorie.nom");
                int id = resultats.getInt("id");
                String titre = resultats.getString("titre");
                String description = resultats.getString("description");
                String debut = resultats.getString("debut");
                String fin = resultats.getString("fin");
                int prix_base = resultats.getInt("prix_base");
                int propose_par = resultats.getInt("propose_par");
                System.out.println(" "+id+" : "+titre+" "+description+" "+debut+" "+fin+" "+prix_base+" "+propose_par);
                //conversion image array byte -> image
                byte[] byteImage = resultats.getBytes("image");
                ByteArrayInputStream inStreambj = new ByteArrayInputStream(byteImage);
                BufferedImage newImage = ImageIO.read(inStreambj);
                listeObj.add(new Objet(id,titre,description,debut, fin,categorie,prix_base,newImage, propose_par));
            }
            return listeObj;
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
    
    
    public static ObservableList rechercheObjetParMotCle(Connection con, String motCle)
            throws SQLException, IOException{
        con.setAutoCommit(false);
        //initialisation de la liste
        ObservableList<Objet> listeObjets = FXCollections.observableArrayList();
        try(Statement st = con.createStatement()){
            //concaténation pour former la requête SQL voulue :
            String query ="select * from objet where titre like '%"+motCle+"%' or description like '%"+motCle+"%'";
            ResultSet resultats = st.executeQuery(query);
            
            System.out.println("Résultats recherche :");
            while(resultats.next()){
                int id = resultats.getInt("id");
                String titre = resultats.getString("titre");
                String description = resultats.getString("description");
                String debut = resultats.getString("debut");
                String fin = resultats.getString("fin");              
                int prix_base = resultats.getInt("prix_base");
                int categorie = resultats.getInt("categorie");
                int propose_par = resultats.getInt("propose_par");
                System.out.println(id+" : "+titre+" "+description+" "+debut+" "+fin+" "+prix_base+" "+categorie+" "+propose_par);
                //conversion image array byte -> image
                byte[] byteImage = resultats.getBytes("image");
                ByteArrayInputStream inStreambj = new ByteArrayInputStream(byteImage);
                BufferedImage newImage = ImageIO.read(inStreambj);
                listeObjets.add(new Objet(id,titre,description,debut,fin,categorie,prix_base,newImage,propose_par));
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
        return listeObjets;
    }

    public static void creeObjet(Connection con,String titre,String description,String debut,String fin,int prix_base,int categorie, byte[]image, int propose_par)
            throws SQLException {
        con.setAutoCommit(false);
        try (PreparedStatement pst = con.prepareStatement(
        """
                    insert into objet (titre, description, debut, fin, prix_base, categorie, image, propose_par)
                    values (?, ?, ?, ?, ?, ?, ?, ?)
                    """)) {
            pst.setString(1, titre);
            pst.setString(2, description);
            pst.setString(3, debut);
            pst.setString(4, fin);
            pst.setInt(5,prix_base);
            pst.setInt(6,categorie);
            pst.setBytes(7, image);
            pst.setInt(8,propose_par);
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
    
        public static void creeObjetImage(Connection con,String titre,String description,String debut,String fin,int prix_base,int categorie,int propose_par, String path)
            throws SQLException, IOException {
        con.setAutoCommit(false);
        //test :
        File file = new File("src\\main\\java\\fr\\insa\\waille\\encheresmiq3\\GUIFX\\logo_lemauvaiscoin.png");
        BufferedImage img = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bos );
        byte [] data = bos.toByteArray();
        try (PreparedStatement pst = con.prepareStatement(
        """
                    insert into objet (titre, description, debut, fin, prix_base, categorie, propose_par, image)
                    values (?, ?, ?, ?, ?, ?, ?, ?)
                    """)) {
            pst.setString(1, titre);
            pst.setString(2, description);
            pst.setString(3, debut);
            pst.setString(4, fin);
            pst.setInt(5,prix_base);
            pst.setInt(6,categorie);
            pst.setInt(7,propose_par);
            pst.setBytes(8, data);
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
    
    public static void demandeObjet(Connection con)
            throws SQLException {
            System.out.println("titre objet:");
            String titre = Lire.S();
            System.out.println("description objet:");
            String description = Lire.S();
            System.out.println("debut vente:");
            String debut = Lire.S();
            System.out.println("fin vente:");
            String fin = Lire.S();
            System.out.println("prix de base objet :");
            int prix_base = Lire.i();
            System.out.println("categorie objet :");
            int categorie = Lire.i();
            System.out.println("objet proposé par :");
            int propose_par = Lire.i();
            //creeObjet(con,titre,description,debut,fin,prix_base,categorie,propose_par);
    }    
    
    public static void deleteAllUtilisateurs(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try {
                st.executeUpdate(
                        """
                    alter table enchere
                        drop constraint fk_enchere_de
                             """);
                System.out.println("constraint fk_enchere_de dropped");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                    alter table objet
                        drop constraint fk_objet_propose_par
                    """);
                System.out.println("constraint fk_objet_propose_par dropped");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        """
                    drop table utilisateur
                    """);
                System.out.println("table utilisateur dropped");
            } catch (SQLException ex) {
            }
        }
    }
    
    public static void deleteAllObjets(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
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
                    drop table objet
                    """);
                System.out.println("table objet dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
        }
    }
    
    public static void deleteAllEncheres(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
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
                    drop table enchere
                    """);
                System.out.println("table enchere dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
        }
    }
    
    public static void deleteAllCategories(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
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
                    drop table categorie
                    """);
                System.out.println("table categorie dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
        }
    }
    
    public static void creeSchemaDeBase(Connection con) throws SQLException, IOException {
            con.setAutoCommit(false);{
            deleteSchema(con);
            creeSchema(con);
            creeUtilisateur(con, "waille", "gregory", "0000", "gregory.waille@insa-strasbourg.fr", "69680" ,"Admin");
            creeUtilisateur(con, "varlet", "arthur", "azerty", "arthur.varlet@insa-strasbourg.fr", "37550","Admin" );
            creeUtilisateur(con, "girardet", "valentin", "pass", "valentin.girardet1@insa-strasbourg.fr", "38080","Admin" );
            creeCategorie(con, "meubles");
            creeCategorie(con, "habits");
            creeCategorie(con, "alcools");
            File file = new File("src\\main\\java\\fr\\insa\\waille\\encheresmiq3\\GUIFX\\imgDeBase.jpg");
            BufferedImage img = ImageIO.read(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", bos );
            byte [] data = bos.toByteArray();
            creeObjet(con, "oettinger", "biere de luxe et rentable", "lundi", "vendredi", 1, 3,data, 1);
            creeObjet(con, "jack_daniel", "whisky de luxe", "lundi", "jeudi", 20, 3,data, 2);
            creeObjet(con, "gin", "bien deg", "mardi", "jeudi", 15, 3,data, 3);
            creeObjet(con, "4 chaises", "robustes", "mardi", "vendredi", 50, 1,data, 1);
            creeObjet(con, "table", "bois massif parfaite pour les reichtags", "mercredi", "jeudi", 100, 1,data, 2);
            creeObjet(con, "2 girafes", "contenance 6L", "mardi", "samedi", 45, 1,data, 3);
            creeObjet(con, "pull INSAshop", "gris + vomis", "lundi", "vendredi", 10, 2,data, 1);
            creeObjet(con, "casquette POLO", "beige", "lundi", "dimanche", 30, 2,data, 2);
            creeObjet(con, "doudoune TNF", "rouge et noire", "mardi", "jeudi", 150, 2,data, 3);
        }
    }
    
    public static void menuTextuel(Connection con) throws IOException{
        //menu permettant à l'utilisateur de choisir une action à effectuer sur la BdD
        boolean stop = false; //condition d'arret
        while(stop==false){
            System.out.println("\nEntrez un nombre pour sélectionner une option :");
            System.out.println("1 - Creation du schéma");
            System.out.println("2 - Suppression du schéma");
            System.out.println("3 - Affichage liste utilisateurs");
            System.out.println("4 - Ajouter un utilisateur");
            System.out.println("5 - Affichage liste categories");
            System.out.println("6 - Ajouter une categorie");
            System.out.println("7 - Affichage liste encheres");
            System.out.println("8 - Ajouter une enchere");
            System.out.println("9 - Affichage liste objets");
            System.out.println("10 - Ajouter un objet");
            System.out.println("11 - Supprimmer tous les utilisateurs");
            System.out.println("12 - Supprimmer tous les objets");
            System.out.println("13 - Supprimmer toutes les encheres");
            System.out.println("14 - Supprimmer toutes les categories");
            System.out.println("15 - Rechercher un objet par catégorie");
            System.out.println("16 - Rechercher un objet par mot clé");
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
                        demandeUtilisateur(con);
                        System.out.println("utilisateur créé OK");
                        break;
                    case 5 :
                        afficheCategorie(con);
                        System.out.println("categories récupérées OK");
                        break;
                    case 6 :
                        demandeCategorie(con);
                        System.out.println(" categories créées OK");
                        break;
                    case 7 :
                        afficheEncheres(con);
                        System.out.println("encheres récupérées OK");
                        break;
                    case 8 :
                        demandeEnchere(con);
                        System.out.println(" encheres créées OK");
                        break;  
                    case 9 :
                        afficheObjets(con);
                        System.out.println("objets récupérés OK");
                        break;
                    case 10 :
                        demandeObjet(con);
                        System.out.println(" objet créés OK");
                        break;
                    case 11 :
                        deleteAllUtilisateurs(con);
                        System.out.println(" utilisateurs supprimés OK");
                        break;
                    case 12 :
                        deleteAllObjets(con);
                        System.out.println(" objets supprimés OK");
                        break;
                    case 13 :
                        deleteAllEncheres(con);
                        System.out.println(" encheres supprimées OK");
                        break;
                    case 14 :
                        deleteAllCategories(con);
                        System.out.println("categories supprimées OK");
                        break;
//                    case 15 :
//                        System.out.println(" Rentrer l'identifiant de la categorie recherchée");
//                        int id = Lire.i();
//                        rechercheObjetParCategorie(con,id);
//                        break;
                    case 16 :
                        System.out.println(" Rentrer le mot clé de l'objet recherché");
                        String motclé = Lire.S();
                        rechercheObjetParMotCle(con,motclé); 
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

    
    public static void main(String[] args) throws IOException {
        try {
            Connection con = defautConnect();
            creeSchemaDeBase(con);
            menuTextuel(con);


        } catch (ClassNotFoundException ex) {
            throw new Error(ex);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
}
    
}
