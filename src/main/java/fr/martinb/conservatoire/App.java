package fr.martinb.conservatoire;

import fr.martinb.conservatoire.modele.Eleve;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Eleve eleve;
    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("connexion.fxml"));
        scene = new Scene(fxmlLoader.load(), 640, 540);
        stage.setTitle("Connexion");
        stage.setScene(scene);
        stage.show();
        App.stage = stage;
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(fxmlLoader.load());
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        App.scene = scene;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        App.stage = stage;
    }

    public static void main(String[] args) {
        launch();
    }

    public static Eleve getEleve() {
        return eleve;
    }

    public static void setEleve(Eleve eleve) {
        App.eleve = eleve;
    }
}