package drs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Main is the entry point of the Disaster Response System (DRS) application.
// It extends Application which is required by JavaFX to start a desktop GUI program.
public class Main extends Application {

    // start() is called automatically by JavaFX when the application launches.
    // It loads the first screen and displays the main window.
    @Override
    public void start(Stage stage) throws Exception {
        // Load the Report Disaster screen as the first screen to display
        Parent root = FXMLLoader.load(getClass().getResource("/view/ReportDisaster.fxml"));

        // Set the title that appears at the top of the window
        stage.setTitle("Disaster Response System (DRS)");

        // Attach the loaded screen to the window
        stage.setScene(new Scene(root));

        // Show the window on screen
        stage.show();
    }

    // main() is the Java entry point — it calls launch() to start the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}
