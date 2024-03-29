package Classes;

import Connectivity.ConnectionClass;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sample.Dialogue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Etudiant{

    private StringProperty matricule, nom, prenom;


    public Etudiant(){
        this.matricule = new SimpleStringProperty();
        this.nom = new SimpleStringProperty();
        this.prenom = new SimpleStringProperty();
    }
    public String getMatricule() {
        return matricule.get();
    }

    public StringProperty matriculeProperty() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule.set(matricule);
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getPrenom() {
        return prenom.get();
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }
    static Connection conn= ConnectionClass.c;

    //Methode permettant d'obtenir l'id de la section de l'etudiant cette methode est regulierement utiliser
    public static String getSectionE(String id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "Select id_section From etudiant where matricule = ?";

        ps = conn.prepareStatement(sql);
        ps.setString(1,id);

        rs = ps.executeQuery();
        if(rs.next())
        {
            return rs.getString(1);
        }
        return "";
    }
    //Cette methode permet d'obtenir lla liste des module que l'etudiant etudie
    public static LinkedList<Module> getModule(String id) throws SQLException {

        LinkedList<Module> llm = new LinkedList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "Select * From Module where id_section = ?";

        ps = conn.prepareStatement(sql);
        ps.setString(1,id);

        rs = ps.executeQuery();

        while (rs.next())
        {
            Module m = new Module();
            m.setId_module(rs.getString(1));
            m.setNom_module(rs.getString(2));
            m.getSection().setId_Section(id);
            m.getProf_cours().setId(rs.getString(4));
            m.getProf_td().setId(rs.getString(5));
            m.getProf_tp().setId(rs.getString(6));

            llm.add(m);
        }
        return llm;
    }

    //Cette methode permet d'obtenir la liste des enseignants qui enseigne le dite etudiant
    public static LinkedList<Enseignant> getListeEnseignant(LinkedList<Module> llm) throws SQLException {
        LinkedList<Enseignant> llen = new LinkedList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM enseignant where id_prof = ANY (?)";

        /*Pour utiliser ANY en java sql il faut creer un tableau contenant l'ensemble des valeurs*/
        String[] id = new String[100];
        int i =0;

        for(Module m : llm)
        {
            id[i] = m.getProf_cours().getId();
            i++;
            id[i] = m.getProf_td().getId();
            i++;
            id[i] = m.getProf_tp().getId();
            i++;
        }
        /*********************************************************************************************/

        ps = conn.prepareStatement(sql);

        ps.setArray(1, conn.createArrayOf("text", id));

        rs = ps.executeQuery();

        while (rs.next())
        {
            Enseignant e = new Enseignant();

            e.setId(rs.getString(1));
            e.setNom(rs.getString(2));
            e.setPrenom(rs.getString(3));
            e.setEmail(rs.getString("email"));

            llen.add(e);
        }

        return llen;
    }


    public void consulterDevoir(String titreDevoir) {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            Connection conn = ConnectionClass.c;

            String sql = "Select * From devoir where titre_devoir = ?";  //aficher tous les d?tails du devoir pour l'?tudiant

            ps = conn.prepareStatement(sql);
            ps.setString(1, titreDevoir);

            rs = ps.executeQuery();

            while (rs.next()) {
                Devoir.setTitreDevoir(titreDevoir);
                Module.setId_module(rs.getString("id_module"));
                Devoir.setDateEnvoi(rs.getDate("date_envoi"));
                Devoir.setDateRemise(rs.getDate("date_remise"));
                Devoir.setExplication(rs.getString("explication"));
            }

        } catch (Exception e) {
            Dialogue.afficherDialogue(e.getMessage());

        } finally {
            try {
                assert ps != null;
                ps.close();
                assert rs != null;
                rs.close();
            } catch (SQLException e) {

                e.printStackTrace();
            }

        }


    }

    public ArrayList<String> voirListeDevoirs(String idSection) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
        String titreDevoir = "";
        String idModu = "";
        final ArrayList<String>  devoirsattribues = new ArrayList<>();

        try {
            Connection conn = ConnectionClass.c;


            String sq = "Select * From module where id_section = ?";
            String sql = "Select * From Devoir where id_Module = ? and date_remise >=CURRENT_DATE and date_envoi >=CURRENT_DATE  "; //afficher les devoirs desquels le delai ne s'est pas ecroulee  //delimitation par date
            ps = conn.prepareStatement(sq);
            ps.setString(1,idSection);
            rs = ps.executeQuery();

            while  (rs.next()) {
                idModu = rs.getString("id_module");
                ps2 = conn.prepareStatement(sql);
                ps2.setString(1,idModu);
                rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    titreDevoir = rs2.getString("titre_devoir");
                    devoirsattribues.add(titreDevoir);
                }
            }




        } catch (Exception e) {

            Dialogue.afficherDialogue(e.getMessage());
        } finally {
            try {
                assert ps != null;
                ps.close();
                assert rs != null;
                rs.close();
                assert ps2 != null;
                ps2.close();
                assert rs2 != null;
                rs2.close();
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
        return devoirsattribues;
    }

    //Cette methode perme d'obtenir l'ensemble des infos de l'etudiant via sont matricule
    public static Etudiant getEtudiant(String mat) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "Select * from etudiant where matricule = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1,mat);
        rs = ps.executeQuery();

        Etudiant e = new Etudiant();
        if(rs.next())
        {
            e.setMatricule(mat);
            e.setNom(rs.getString("nom_etudiant"));
            e.setPrenom(rs.getString("prenom_etudiant"));
        }
        ps.close();
        rs.close();
        return e;
    }


}
