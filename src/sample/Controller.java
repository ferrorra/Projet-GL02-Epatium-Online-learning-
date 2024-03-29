package sample;

import Classes.Seance;
import Connectivity.ConnectionClass;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    private VBox vboxx;
    private Parent fxml = null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Seance.InitSuppSeance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vboxx);
        t.setToX(vboxx.getLayoutX() * 23);
        t.play();
        t.setOnFinished((e) -> {

            try {
                //fxml = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
                //final FXMLLoader fxmload = new FXMLLoader(Controller.class.getClassLoader().getResource("SignIn.fxml"));
                //  fxml = fxmload.load();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            vboxx.getChildren().removeAll();
            //vboxx.getChildren().setAll(fxml);


        });
    }

    @FXML
    private void signin(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vboxx);
        t.setToX(vboxx.getLayoutX() * 23);
        t.play();
        t.setOnFinished((e) -> {
            try {

                fxml = FXMLLoader.load(getClass().getResource("SignIn.fxml"));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            vboxx.getChildren().clear();

            vboxx.getChildren().setAll(fxml);

        });
    }

    @FXML
    private void signup(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vboxx);
        t.setToX(0);
        t.play();
        t.setOnFinished((e) -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("SignUp.fxml"));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            vboxx.getChildren().clear();
            vboxx.getChildren().setAll(fxml);
        });
    }

    @FXML
    private void quit() {

        ConnectionClass.killConnection();
        Platform.exit();
        System.exit(0);
    }
}
    