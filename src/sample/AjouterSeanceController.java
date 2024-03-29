package sample;

import Classes.Module;

import Classes.Seance;
import Classes.Section;
import Classes.Utilisateur;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;

public class AjouterSeanceController implements Initializable {

    @FXML
    private ComboBox<String> module;
    @FXML
    private ComboBox<String> section;
    @FXML
    private ComboBox<String> type;
    @FXML
    private ComboBox<JourSemaine> jour;
    @FXML
    private ComboBox<String> horraire;
    @FXML
    private TextField link;

    @FXML
    private Label confirmation;
    @FXML
    private Button closeButton;

    Utilisateur u = new Utilisateur();


    public void initialize(URL location, ResourceBundle resources) {

        try {
            section.setItems(Module.SetComboSp(Module.ModuleProf(u.getIdd())));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        module.setDisable(true);
        type.setDisable(true);
        horraire.setDisable(true);
        link.setDisable(true);
        jour.setDisable(true);

    }

    public void SetupModuleCombo() throws SQLException, ClassNotFoundException {

        module.setDisable(false);
        module.setItems(Module.SetComboM(Module.ModuleProf(u.getIdd()),section.getSelectionModel().getSelectedItem()));
        type.setDisable(true);
        horraire.setDisable(true);
        link.setDisable(true);
        jour.setDisable(true);
        jour.setPromptText("");
        horraire.setPromptText("");
        link.setText("");
        confirmation.setText("");
    }

    public void SetupType() throws SQLException, ClassNotFoundException {
        type.setDisable(false);
        type.setItems(Module.SetComboT(Module.ModuleProf(u.getIdd()),section.getSelectionModel().getSelectedItem(),module.getSelectionModel().getSelectedItem(),u.getIdd()));
        horraire.setDisable(true);
        link.setDisable(true);
        jour.setDisable(true);
    }

    public void SetupJour(){
        jour.setDisable(false);
        jour.setItems(FXCollections.observableArrayList(JourSemaine.values()));
        horraire.setDisable(true);
        link.setDisable(true);
    }

    public void SetupH() throws SQLException {
        horraire.setDisable(false);
        horraire.setItems(Seance.getHorraireDispo(section.getSelectionModel().getSelectedItem(),u.getIdd(),jour.getSelectionModel().getSelectedItem().toString()));
    }

    public void LINK(){
        link.setDisable(false);
    }

    //Méthode qui permet d'ajouter une seance a la bdd
    public void Ajout() throws SQLException, ParseException {
        String id_setion = section.getSelectionModel().getSelectedItem();
        String id_module = Module.getIDMODULE(module.getSelectionModel().getSelectedItem(),id_setion);
        String j = jour.getSelectionModel().getSelectedItem().toString();
        //----------obtention de la date courante--------------------
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            String DateS = Seance.DateSeance(Seance.getNumJour(j));
        //-----------------------------------------------------------
        Seance.AjoutSeane(id_setion,id_module,type.getSelectionModel().getSelectedItem(),DateS,j,horraire.getSelectionModel().getSelectedItem(),link.getText(),u.getIdd());
        confirmation.setText("Seance Ajoutee");
        EmploiEnseignantController.refresh();
    }
    @FXML
    private void quit() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
